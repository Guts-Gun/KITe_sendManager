package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.read.SendReplace;
import gutsandgun.kite_sendmanager.entity.read.SendingBlock;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingBlockRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingReplaceRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingBlockRepository;
import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SendingBlockServiceImpl implements SendingBlockService{

    @Autowired
    private final ReadSendingBlockRepository readSendingBlockRepository;

    @Autowired
    private final WriteSendingBlockRepository writeSendingBlockRepository;

    @Autowired
    private final ReadSendingReplaceRepository readSendingReplaceRepository;

    @Autowired
    private final SendEmailService sendEmailService;

    @Autowired
    private final ModelMapper mapper;

    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public void insertSendingBlock(String userId, SendingBlockDTO sendingBlockDTO) {
        writeSendingBlockRepository.save(dtoToEntity(userId, sendingBlockDTO));
    }

    @Override
    public List<SendingBlockDTO> getBlockList(String sender) {
        List<SendingBlock> sendingBlockList = readSendingBlockRepository.findBySender(sender);
        List<SendingBlockDTO> sendingBlockDTOList = new ArrayList<>();
        sendingBlockList.forEach(sendingBlock -> {
            sendingBlockDTOList.add(mapper.map(sendingBlock,SendingBlockDTO.class));
        });
        return sendingBlockDTOList;
    }

    @Override
    public List<SendingMsgDTO> replaceSending(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList) {

        // 발신자에 등록된 수신거부 리스트
        List<SendingBlockDTO> sendingBlockDTOList = getBlockList(sendingDTO.getSender());


        // 수신자 리스트 중 수신거부 번호 리스트
        List<SendingMsgDTO> replaceMsgList = sendingMsgDTOList.stream().filter(_this ->
                sendingBlockDTOList.stream().anyMatch(target -> _this.getReceiver().equals(target.getReceiver()))
        ).collect(Collectors.toList());


        // 수신거부 플랫폼 대체발송
        replaceMsgList.forEach(sendingMsgDTO -> {
            if(sendingDTO.getReplaceYn().equals("Y")) {
                SendReplaceDTO sendReplaceDTO = getReplaceInfo(sendingMsgDTO.getId(), sendingDTO.getSendingType());
                sendEmailService.sendMsgReplaceEmail(sendingDTO, sendReplaceDTO, sendingMsgDTO);
            }else{
                rabbitMQProducer.logSendQueue("Service: sendingManager, type: blocking, success: fail, " + "sendingId: "+sendingDTO.getId()+", sendingType: "+sendingDTO.getSendingType()+", TXId: "+sendingMsgDTO.getId()+", time: "+new Date().getTime()+"@");
            }
        });

        // 수신거부 필터 발송 메시지 리스트
        List<SendingMsgDTO> resultList = sendingMsgDTOList.stream().filter(_this ->
                sendingBlockDTOList.stream().noneMatch(target -> _this.getReceiver().equals(target.getReceiver()))
        ).collect(Collectors.toList());

        return resultList;
    }

    @Override
    public SendReplaceDTO getReplaceInfo(Long txId, SendingType sendingType) {
        SendReplace sendReplace = readSendingReplaceRepository.findById(txId).get();
        SendReplaceDTO sendReplaceDTO = mapper.map(sendReplace, SendReplaceDTO.class);
        return  sendReplaceDTO;
    }




}

package gutsandgun.kite_sendmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.cache.annotation.Cacheable;
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

    private final SendingCache sendingCache;

    ObjectMapper objectMapper = new ObjectMapper();

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

        // ???????????? ????????? ???????????? ?????????
        List<SendingBlockDTO> sendingBlockDTOList = getBlockList(sendingDTO.getSender());


        // ????????? ????????? ??? ???????????? ?????? ?????????
        List<SendingMsgDTO> replaceMsgList = sendingMsgDTOList.stream().filter(_this ->
                sendingBlockDTOList.stream().anyMatch(target -> _this.getReceiver().equals(target.getReceiver()))
        ).collect(Collectors.toList());


        // ???????????? ????????? ????????????
        replaceMsgList.forEach(sendingMsgDTO -> {
            if(sendingDTO.getReplaceYn().equals("Y")) {
                SendReplaceDTO sendReplaceDTO = getReplaceInfo(sendingMsgDTO.getId(), sendingDTO.getSendingType());
                sendEmailService.sendMsgReplaceEmail(sendingDTO, sendReplaceDTO, sendingMsgDTO);
            }else{
                rabbitMQProducer.logSendQueue("Service: sendingManager, type: blocking, success: fail, " + "sendingId: "+sendingDTO.getId()+", sendingType: "+sendingDTO.getSendingType()+", TXId: "+sendingMsgDTO.getId()+", time: "+new Date().getTime()+"@");
            }
        });

        // ???????????? ?????? ?????? ????????? ?????????
        List<SendingMsgDTO> resultList = sendingMsgDTOList.stream().filter(_this ->
                sendingBlockDTOList.stream().noneMatch(target -> _this.getReceiver().equals(target.getReceiver()))
        ).collect(Collectors.toList());

        return resultList;
    }

    @Override
    public SendReplaceDTO getReplaceInfo(Long txId, SendingType sendingType) {
        SendReplaceDTO sendReplaceDTO = null;
        try {
            sendReplaceDTO = objectMapper.readValue(sendingCache.getSendReplaceInfo(txId), SendReplaceDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return sendReplaceDTO;
    }



}

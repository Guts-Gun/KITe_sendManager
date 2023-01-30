package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRepository;
import gutsandgun.kite_sendmanager.type.SendingRuleType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService{

    @Autowired
    WriteSendingRepository writeSendingRepository;

    @Autowired
    ReadSendingMsgRepository readSendingMsgRepository;

    @Autowired
    private final ModelMapper mapper;


    @Override
    public Long insertSending(SendingDTO sendingDTO, String userId) {

        if (sendingDTO.getReservationTime() != null){
            sendingDTO.setScheduleTime(sendingDTO.getReservationTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        sendingDTO.setInputTime(new Date().getTime());
        Sending sending = writeSendingRepository.save(mapper.map(sendingDTO, Sending.class));
        return sending.getId();
    }

    @Override
    public SendingDTO startSending(Long sendingId) {
       Sending sending =  writeSendingRepository.findById(sendingId).get();
        SendingDTO sendingDTO = mapper.map(sending, SendingDTO.class);
        return sendingDTO;
    }


    public List<SendingMsgDTO> selectSendMsgList(Long sendingId){
        List<SendingMsg> sendingMsgList =  readSendingMsgRepository.findBySendingId(sendingId);
        List<SendingMsgDTO> sendingMsgDTOList = new ArrayList<>();
        sendingMsgList.forEach(SendingMsg -> {
            sendingMsgDTOList.add(mapper.map(SendingMsg,SendingMsgDTO.class));
        });
        return sendingMsgDTOList;
    }
}

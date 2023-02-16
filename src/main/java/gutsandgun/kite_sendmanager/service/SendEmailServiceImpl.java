package gutsandgun.kite_sendmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.read.SendingBlock;
import gutsandgun.kite_sendmanager.entity.read.SendingEmail;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingEmailRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Log4j2
public class SendEmailServiceImpl implements SendEmailService{

    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    private final SendingCache sendingCache;



    @Autowired
    private final ModelMapper mapper;

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<SendingMsgDTO> getSendMsgList(Long sendingId){
        List<String> list = sendingCache.getSendingEmailDTOList(sendingId);
        List<SendingMsgDTO> sendingMsgList =  new ArrayList<>();
        list.forEach(str -> {
            try {
                SendingMsgDTO sendingMsgDTO = objectMapper.readValue(str, SendingMsgDTO.class);
                sendingMsgList.add(sendingMsgDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return sendingMsgList;
    }


    @Override
    public void sendMsgReplaceEmail(SendingDTO sendingDTO, SendReplaceDTO sendReplaceDTO, SendingMsgDTO sendingMsgDTO) {
        SendingEmailDTO sendingEmailDTO = new SendingEmailDTO();
        sendingEmailDTO.setSendingId(sendingMsgDTO.getSendingId());
        sendingEmailDTO.setId(sendingMsgDTO.getId());
        sendingEmailDTO.setName(sendingMsgDTO.getName());
        sendingEmailDTO.setSender(sendReplaceDTO.getSender());
        sendingEmailDTO.setReceiver(sendReplaceDTO.getReceiver());
        sendingEmailDTO.setRegId("SYSTEM");
        sendingEmailDTO.setReplaceYn("Y");

        replaceProduceQueue(sendingDTO, sendingEmailDTO);
    }

    public void replaceProduceQueue(SendingDTO sendingDTO, SendingEmailDTO sendingEmailDTO) {
        rabbitMQProducer.sendEmailQueue1Message(sendingEmailDTO, sendingDTO.getId(), SendingType.EMAIL);
    }


}

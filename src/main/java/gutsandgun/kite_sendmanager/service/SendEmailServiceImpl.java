package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.read.SendingBlock;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Log4j2
public class SendEmailServiceImpl implements SendEmailService{

    private final RabbitMQProducer rabbitMQProducer;

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
//        log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingDTO.getId() + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 1, TXId: " + sendingEmailDTO.getId() + ", time: " + new Date().getTime());
        rabbitMQProducer.sendEmailQueue1Message(sendingEmailDTO, sendingDTO.getId(), SendingType.EMAIL);
    }


}

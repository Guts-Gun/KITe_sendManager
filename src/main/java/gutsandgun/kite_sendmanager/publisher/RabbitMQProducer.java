package gutsandgun.kite_sendmanager.publisher;

import gutsandgun.kite_sendmanager.dto.SendingEmailDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Log4j2
public class RabbitMQProducer {

    @Value("${rabbitmq.sms.queue1.exchange}")
    private String exchange1;

    @Value("${rabbitmq.sms.queue2.exchange}")
    private String exchange2;

    @Value("${rabbitmq.sms.queue3.exchange}")
    private String exchange3;


    @Value("${rabbitmq.sms.routing.key.queue1}")
    private String routingKey1;

    @Value("${rabbitmq.sms.routing.key.queue2}")
    private String routingKey2;

    @Value("${rabbitmq.sms.routing.key.queue3}")
    private String routingKey3;


    @Value("${rabbitmq.email.queue1.exchange}")
    private String emailExchange;

    @Value("${rabbitmq.email.routing.key.queue1}")
    private String emailRoutingKey1;

    @Value("${rabbitmq.email.routing.key.queue2}")
    private String emailRoutingKey2;


    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendQueue1Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        logSendQueue("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " +sendingType + ", brokerId: 1, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime()+"@");
        rabbitTemplate.convertAndSend(exchange1, routingKey1, sendingMsgDTO);
    }

    public void sendQueue2Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        logSendQueue("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " + sendingType.toString() + ", brokerId: 2, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime()+"@");
        rabbitTemplate.convertAndSend(exchange2, routingKey2, sendingMsgDTO);
    }

    public void sendQueue3Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        logSendQueue("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " + sendingType.toString() + ", brokerId: 3, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime()+"@");
        rabbitTemplate.convertAndSend(exchange3, routingKey3, sendingMsgDTO);
    }

    public void sendEmailQueue1Message(SendingEmailDTO sendingEmailDTO, Long sendingId, SendingType sendingType){
        logSendQueue("Service: sendingManager, type: pushQueue" + ", sendingId: " +sendingId + ", sendingType: " + sendingType.toString() + ", brokerId: 4, TXId: " + sendingEmailDTO.getId() + ", time: " + new Date().getTime()+"@");
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey1, sendingEmailDTO);
    }

    public void sendEmailQueue2Message(SendingEmailDTO sendingEmailDTO, Long sendingId, SendingType sendingType){
        logSendQueue("Service: sendingManager, type: pushQueue" + ", sendingId: " +sendingId + ", sendingType: " + sendingType.toString() + ", brokerId: 5, TXId: " + sendingEmailDTO.getId() + ", time: " + new Date().getTime()+"@");
        rabbitTemplate.convertAndSend(exchange3, emailRoutingKey2, sendingEmailDTO);
    }

    @Value("${rabbitmq.log.exchange}")
    private String logExchange;

    @Value("${rabbitmq.routing.key.log}")
    private String logRoutingKey;

    public void logSendQueue(String msg){
        rabbitTemplate.convertAndSend(logExchange, logRoutingKey, msg);
    }
}

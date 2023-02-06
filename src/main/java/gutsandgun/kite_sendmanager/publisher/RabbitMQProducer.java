package gutsandgun.kite_sendmanager.publisher;

import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.queue1.exchange}")
    private String exchange1;

    @Value("${rabbitmq.queue2.exchange}")
    private String exchange2;

    @Value("${rabbitmq.queue3.exchange}")
    private String exchange3;

    @Value("${rabbitmq.routing.key.queue1}")
    private String routingKey1;

    @Value("${rabbitmq.routing.key.queue2}")
    private String routingKey2;

    @Value("${rabbitmq.routing.key.queue3}")
    private String routingKey3;


    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendQueue1Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        rabbitTemplate.convertAndSend(exchange1, routingKey1, sendingMsgDTO);
    }

    public void sendQueue2Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        rabbitTemplate.convertAndSend(exchange2, routingKey2, sendingMsgDTO);
    }

    public void sendQueue3Message(SendingMsgDTO sendingMsgDTO, Long sendingId, SendingType sendingType){
        rabbitTemplate.convertAndSend(exchange3, routingKey3, sendingMsgDTO);
    }
}
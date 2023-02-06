package gutsandgun.kite_sendmanager.controller;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.service.SendingRuleService;
import gutsandgun.kite_sendmanager.service.SendingService;
import gutsandgun.kite_sendmanager.type.SendingRuleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="sending")
@Log4j2
@RequiredArgsConstructor
public class SendingController {

    private final SendingService sendingService;

    private final SendingRuleService sendingRuleService;

    @Autowired
    RabbitMQProducer rabbitMQProducer;

    /**
     * 발송 요청 시 sending 등록
     *
     * @author solbiko
     * @param userId 유저 ID
     * @param sendMsgRequestDTO 발송 요청 정보
     * @return long sendingId
     */
    @Transactional
    @PostMapping("/req")
    public ResponseEntity<Long> requestMsg(String userId, @RequestBody SendMsgRequestDTO sendMsgRequestDTO) {

        SendingDTO sendingDTO = sendMsgRequestDTO.getSendingDTO();
        sendingDTO.setRegId(userId);
        sendingDTO.setUserId(userId);

        Long sendingId = sendingService.insertSending(sendingDTO, userId);

        // 중계사 비율 CUSTOM
        if (sendingDTO.getSendingRuleType().equals(SendingRuleType.CUSTOM)) {
            sendingRuleService.insertSendingRule(sendMsgRequestDTO.getBrokerList(), userId, sendingId);
        }
        return new ResponseEntity<>(sendingId, HttpStatus.OK);
    }

    /**
     * 발송 시작
     *
     * @author solbiko
     * @param map - 발송 번호
     * @return long sendingId
     */
    @PostMapping("/start")
    public ResponseEntity<Long> startSending(@RequestBody Map<String, Long> map) {

        Long sendingId = map.get("sendingId");

        // 발송 정보
        SendingDTO sendingDTO = sendingService.getSending(sendingId);
        log.info("Service: sendingManager, type: sendingStart, " + "sendingId: "+sendingDTO.getId()+", sendingType: "+sendingDTO.getSendingType()+", time: "+new Date().getTime());

        // 발송 메시지 리스트
        List<SendingMsgDTO> sendingMsgDTOList = sendingService.getSendMsgList(sendingId);

        // 분배 리스트
        List<Map<Long, List<SendingMsgDTO>>> list = new ArrayList<>();

        switch (sendingDTO.getSendingRuleType()) {
            case CUSTOM:
                // 중계사 발송 분배 비율 리스트
                List<SendingRuleDTO> sendingRuleDTOList = sendingRuleService.selectSendingRule(sendingId);
                list = sendingService.distributeMessageCustom(sendingRuleDTOList, sendingMsgDTOList);
                break;

            case SPEED:
                list = sendingService.distributeMessageSpeed(sendingMsgDTOList);
                break;

            case PRICE:
                list = sendingService.distributeMessagePrice(sendingMsgDTOList);
                break;
        }

        // MQ produce
        list.forEach(listMap ->{
            // SKT
            List<SendingMsgDTO> broker1SendingMsgDTOList = listMap.get(1L);
            if(broker1SendingMsgDTOList != null){
                broker1SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 1, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue1Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

            // KT
            List<SendingMsgDTO> broker2SendingMsgDTOList = listMap.get(2l);
            if(broker2SendingMsgDTOList != null) {
                broker2SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 2, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue2Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

            // LG
            List<SendingMsgDTO> broker3SendingMsgDTOList = listMap.get(3L);
            if (broker3SendingMsgDTOList != null){
                broker3SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingId + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 3, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue3Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

        });

        return new ResponseEntity<>(sendingId, HttpStatus.OK);
    }



}

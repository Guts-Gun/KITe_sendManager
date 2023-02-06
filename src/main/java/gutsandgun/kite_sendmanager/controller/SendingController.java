package gutsandgun.kite_sendmanager.controller;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.service.SendingBlockService;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="sending")
@Log4j2
@RequiredArgsConstructor
public class SendingController {

    private final SendingService sendingService;

    private final SendingRuleService sendingRuleService;

    private final SendingBlockService sendingBlockService;

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
        sendingDTO.setSender(sendMsgRequestDTO.getSender());

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

        // 발송 정보
        SendingDTO sendingDTO = sendingService.getSending(map.get("sendingId"));
        log.info("Service: sendingManager, type: sendingStart, " + "sendingId: "+sendingDTO.getId()+", sendingType: "+sendingDTO.getSendingType()+", time: "+new Date().getTime());

        // 발송 메시지 리스트
        List<SendingMsgDTO> sendingMsgDTOList = sendingService.getSendMsgList(sendingDTO.getId());

        // 수신거부 필터링 발송 메시지 리스트
        List<SendingMsgDTO> resultList = sendingBlockService.replaceSending(sendingDTO, sendingMsgDTOList);

        switch (sendingDTO.getSendingRuleType()) {
            case CUSTOM:
                List<SendingRuleDTO> sendingRuleDTOList = sendingRuleService.selectSendingRule(sendingDTO.getId()); // 중계사 발송 분배 비율 리스트
                sendingService.distributeMessageCustom(sendingDTO, sendingRuleDTOList, resultList);
                break;

            case SPEED:
                sendingService.distributeMessageSpeed(resultList);
                break;

            case PRICE:
                sendingService.distributeMessagePrice(resultList);
                break;
        }
        return new ResponseEntity<>(sendingDTO.getId(), HttpStatus.OK);
    }





}

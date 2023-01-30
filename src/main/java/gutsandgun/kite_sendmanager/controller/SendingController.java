package gutsandgun.kite_sendmanager.controller;

import com.google.common.collect.Lists;
import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

        log.info(" [send manager] sending start  ::: sendingId :" + sendingId);
        log.info("-------------------------------------------------");

        // 발송
        SendingDTO sendingDTO = sendingService.startSending(sendingId);

        // 중계사 발송 분배 비율 리스트
        List<SendingRuleDTO> sendingRuleDTOList = null;
        if(sendingDTO.getSendingRuleType().equals(SendingRuleType.CUSTOM)){
            sendingRuleDTOList = sendingRuleService.selectSendingRule(sendingId);
        }


        // 발송 메시지
        List<SendingMsgDTO> sendingMsgDTOList = sendingService.selectSendMsgList(sendingId);
        Integer totalMsgCount = sendingMsgDTOList.size();



        Stream<SendingRuleDTO> stream = sendingRuleDTOList.stream();
        Stream<SendingRuleDTO> filtered = stream.filter(dto -> dto.getWeight()>0);

        // 분배
//        sendingRuleDTOList.forEach(sendingRuleDTO -> {
//            if(sendingRuleDTO.getWeight() >0){
//                Double percent = (double) sendingRuleDTO.getWeight().intValue() / (double) 100;
//                Long sendCnt = Math.round(percent * totalMsgCount);
//                List<List<List<SendingMsgDTO>>> sliceList = Lists.partition(Arrays.asList(sendingMsgDTOList), Math.toIntExact(sendCnt));
//            }
//        });


        sendingMsgDTOList.forEach(sendingMsgDTO -> {
            rabbitMQProducer.sendQueue1Message(sendingMsgDTO);
        });
        return new ResponseEntity<>(sendingId, HttpStatus.OK);
    }



}

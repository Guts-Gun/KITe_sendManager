package gutsandgun.kite_sendmanager.controller;

import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.entity.SendingRuleType;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.service.SendingRuleService;
import gutsandgun.kite_sendmanager.service.SendingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(value="sending")
@Log4j2
public class SendingController {

    @Autowired
    SendingService sendingService;

    @Autowired
    SendingRuleService sendingRuleService;


    @Transactional
    @PostMapping("/req")
    public ResponseEntity<Long> requestMsg(Principal principal, @RequestBody SendMsgRequestDTO sendMsgRequestDTO) {

        // JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        // String userId = token.getTokenAttributes().get("preferred_username").toString();
        Integer userId = 1;

        Long sendingId = sendingService.insertSending(sendMsgRequestDTO, userId);

        SendingDTO sendingDTO = sendMsgRequestDTO.getSendingDTO();

        // 중계사 비율 CUSTOM
        if (sendingDTO.getRuleType().equals(SendingRuleType.CUSTOM)) {
            sendingRuleService.insertSendingRule(sendMsgRequestDTO.getBrokerList(), userId, sendingId);
        }
        return new ResponseEntity<>(sendingId, HttpStatus.OK);
    }
}

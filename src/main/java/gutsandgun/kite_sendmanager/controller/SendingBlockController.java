package gutsandgun.kite_sendmanager.controller;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.service.SendingBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value="sendingBlock")
@Log4j2
@RequiredArgsConstructor
public class SendingBlockController {

    private final SendingBlockService sendingBlockService;


    /**
     * 수신 거부 등록
     *
     * @author solbiko
     * @param principal 로그인 객체
     * @param sendingBlockDTO 수신거부 정보
     * @return String
     */
    @Transactional
    @PostMapping("/reg")
    public ResponseEntity<String> insertSendingBlock(Principal principal, @RequestBody SendingBlockDTO sendingBlockDTO) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userId = token.getTokenAttributes().get("preferred_username").toString();

        sendingBlockService.insertSendingBlock(userId, sendingBlockDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


}

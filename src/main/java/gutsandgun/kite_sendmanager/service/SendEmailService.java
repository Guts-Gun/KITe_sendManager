package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;

import java.util.Map;

public interface SendEmailService {

    // 메세지 -> 이메일 플랫폼 대체발송
    void sendMsgReplaceEmail (SendingDTO sendingDTO, SendReplaceDTO sendReplaceDTO, SendingMsgDTO sendingMsgDTO);

}


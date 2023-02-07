package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;

import java.util.Map;

public interface SendEmailService {

    // 이메일 대체 발송 요청 정보 조회
    void getMsgReplaceInfo(Map<String, Long> map);

    // 메세지 -> 이메일 플랫폼 대체발송
    void sendMsgReplaceEmail (SendingDTO sendingDTO, SendReplaceDTO sendReplaceDTO, SendingMsgDTO sendingMsgDTO);

}


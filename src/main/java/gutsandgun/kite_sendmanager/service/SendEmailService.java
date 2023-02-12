package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;

import java.util.List;
import java.util.Map;

public interface SendEmailService {

    // 발송 메시지 리스트 조회
    List<SendingMsgDTO> getSendMsgList(Long sendingId);


    // 메세지 -> 이메일 플랫폼 대체발송
    void sendMsgReplaceEmail (SendingDTO sendingDTO, SendReplaceDTO sendReplaceDTO, SendingMsgDTO sendingMsgDTO);

}


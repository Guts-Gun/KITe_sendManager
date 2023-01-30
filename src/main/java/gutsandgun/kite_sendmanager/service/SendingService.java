package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;

import java.util.List;

public interface SendingService {

    Long insertSending(SendingDTO sendingDTO, String userId);    // 발송 저장

    SendingDTO startSending(Long sendingId);   // 발송 시작

    List<SendingMsgDTO> selectSendMsgList(Long sendingID);
}

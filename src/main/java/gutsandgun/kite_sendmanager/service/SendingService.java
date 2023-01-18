package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.entity.write.Sending;

import java.util.Date;

public interface SendingService {

    Long insertSending(SendingDTO sendingDTO, String userId);    // 발송 저장

    void startSending(Long sendingId);   // 발송 시작
}

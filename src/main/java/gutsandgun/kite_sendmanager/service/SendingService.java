package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;

import java.util.List;
import java.util.Map;

public interface SendingService {

    // 발송 저장
    Long insertSending(SendingDTO sendingDTO, String userId);

    // 발송 정보 조회
    SendingDTO getSending(Long sendingId);


}

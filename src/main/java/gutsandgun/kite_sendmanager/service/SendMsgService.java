package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;

import java.util.List;

public interface SendMsgService {

    // 발송 메시지 리스트 조회
    List<SendingMsgDTO> getSendMsgList(Long sendingId);

    // 발송 메시지 조회
    SendingMsgDTO getSendMsg(Long sendingId, Long txId);

    // 중계사 분배 비율에 따른 메시지 분배
    void distributeMessageCustom(SendingDTO sendingDTO, List<SendingRuleDTO> sendingRuleDTOList, List<SendingMsgDTO> sendingMsgDTOList);

    // 속도 우선 중계사 비율 분배
    void distributeMessageSpeed(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList);

    // 가격 우선 중계사 비율 분배
    void distributeMessagePrice(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList);

}

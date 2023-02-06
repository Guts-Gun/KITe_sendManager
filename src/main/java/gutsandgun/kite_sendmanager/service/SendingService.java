package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendReplaceDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;
import gutsandgun.kite_sendmanager.type.SendingType;

import java.util.List;
import java.util.Map;

public interface SendingService {

    // 발송 저장
    Long insertSending(SendingDTO sendingDTO, String userId);

    // 발송 정보 조회
    SendingDTO getSending(Long sendingId);

    // 발송 메시지 리스트 조회
    List<SendingMsgDTO> getSendMsgList(Long sendingID);

    // 중계사 분배 비율에 따른 메시지 분배
    void distributeMessageCustom(SendingDTO sendingDTO, List<SendingRuleDTO> sendingRuleDTOList, List<SendingMsgDTO> sendingMsgDTOList);

    // 속도 우선 중계사 비율 분배
    void distributeMessageSpeed(List<SendingMsgDTO> sendingMsgDTOList);

    // 가격 우선 중계사 비율 분배
    void distributeMessagePrice(List<SendingMsgDTO> sendingMsgDTOList);


}

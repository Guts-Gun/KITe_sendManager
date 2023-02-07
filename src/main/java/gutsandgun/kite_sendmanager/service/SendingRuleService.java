package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;

import java.util.List;

public interface SendingRuleService {

    void insertSendingRule(List<SendingRuleDTO> sendingRuleDTO, String userId, Long sendingId);    // 분배규칙리스트 저장

    List<SendingRuleDTO> selectSendingRule(Long sendingId); // 분배규칙리스트 조회

    default SendingRule dtoToEntity(SendingRuleDTO dto, String userId, Long sendingId) {
        SendingRule sendingRule = SendingRule.builder()
                .sendingId(sendingId)
                .brokerId(dto.getBrokerId())
                .weight(dto.getWeight())
                .userId(userId)
                .build();
        return sendingRule;
    }
}

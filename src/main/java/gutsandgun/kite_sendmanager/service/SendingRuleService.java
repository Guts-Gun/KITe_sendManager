package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;

import java.util.List;

public interface SendingRuleService {

    void insertSendingRule(List<SendingRuleDTO> sendingRuleDTO, Integer userId, Long sendingId);    // 분배규칙리스트 저장


    default SendingRule dtoToEntity(SendingRuleDTO dto, Integer userId, Long sendingId) {
        SendingRule sendingRule = SendingRule.builder()
                .sendingId(sendingId)
                .brokerId(dto.getBrokerId())
                .weight(dto.getWeight())
                .userId(userId.longValue())
                .build();
        return sendingRule;
    }
}

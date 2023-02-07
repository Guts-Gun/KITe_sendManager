package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendingRuleServiceImpl implements SendingRuleService{

    @Autowired
    private final WriteSendingRuleRepository writeSendingRuleRepository;

    @Autowired
    private final ModelMapper mapper;

    public void insertSendingRule(List<SendingRuleDTO> brokerList, String userId, Long sendingId){

        brokerList.forEach(sendingRuleDTO -> {
            writeSendingRuleRepository.save(dtoToEntity(sendingRuleDTO, userId, sendingId));
        });
    }

    public List<SendingRuleDTO> selectSendingRule(Long sendingId){
        List<SendingRule> sendingRuleList = writeSendingRuleRepository.findBySendingId(sendingId);
        List<SendingRuleDTO> sendingRuleDTOList = new ArrayList<>();
        sendingRuleList.forEach(sendingRule -> {
            sendingRuleDTOList.add(mapper.map(sendingRule, SendingRuleDTO.class));
        });
        return sendingRuleDTOList;
    }

}

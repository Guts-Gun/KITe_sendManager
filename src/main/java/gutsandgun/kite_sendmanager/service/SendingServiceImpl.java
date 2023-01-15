package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.SendingRuleType;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService{

    @Autowired
    WriteSendingRepository writeSendingRepository;

    @Autowired
    WriteSendingRuleRepository writeSendingRuleRepository;

    @Autowired
    private final ModelMapper mapper;

    @Override
    public Long insertSending(SendMsgRequestDTO sendMsgRequestDTO, Integer userId) {

        Sending sending = writeSendingRepository.save(dtoToEntity(sendMsgRequestDTO.getSendingDTO(), userId));
        Long sendingId = sending.getId();

        return sendingId;
    }


}

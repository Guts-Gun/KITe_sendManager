package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingBlockDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.read.SendingBlock;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingBlockRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingBlockRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendingBlockServiceImpl implements SendingBlockService{

    @Autowired
    ReadSendingBlockRepository readSendingBlockRepository;

    @Autowired
    WriteSendingBlockRepository writeSendingBlockRepository;

    @Autowired
    private final ModelMapper mapper;


    @Override
    public void insertSendingBlock(String userId, SendingBlockDTO sendingBlockDTO) {
        writeSendingBlockRepository.save(dtoToEntity(userId, sendingBlockDTO));
    }

    @Override
    public List<SendingBlockDTO> getBlockList(String sender) {
        List<SendingBlock> sendingBlockList = readSendingBlockRepository.findBySender(sender);
        List<SendingBlockDTO> sendingBlockDTOList = new ArrayList<>();
        sendingBlockList.forEach(sendingBlock -> {
            sendingBlockDTOList.add(mapper.map(sendingBlock,SendingBlockDTO.class));
        });
        return sendingBlockDTOList;
    }
}

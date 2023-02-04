package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingBlockDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.write.SendingBlock;
import gutsandgun.kite_sendmanager.entity.write.SendingRule;

import java.util.Date;
import java.util.List;

public interface SendingBlockService {

    void insertSendingBlock(String userId, SendingBlockDTO sendingBlockDTO);

    List<SendingBlockDTO> getBlockList(String sender);
    default SendingBlock dtoToEntity(String userId, SendingBlockDTO sendingBlockDTO) {
        SendingBlock sendingBlock = SendingBlock.builder()
                .blockTime(new Date().getTime())
                .sender(sendingBlockDTO.getSender())
                .receiver(sendingBlockDTO.getReceiver())
                .regId(userId)
                .build();
        return sendingBlock;
    }
}

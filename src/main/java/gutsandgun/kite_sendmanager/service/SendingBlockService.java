package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.write.SendingBlock;
import gutsandgun.kite_sendmanager.type.SendingType;

import java.util.Date;
import java.util.List;

public interface SendingBlockService {

    // 수신거부 등록
    void insertSendingBlock(String userId, SendingBlockDTO sendingBlockDTO);

    // 수신거부 목록 조회
    List<SendingBlockDTO> getBlockList(String sender);


    // 수신거부번호 제외 리스트 조회
    List<SendingMsgDTO> replaceSending(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList);

    // 대체발송 정보 조회
    SendReplaceDTO getReplaceInfo(Long txId, SendingType sendingType);

    default SendingBlock dtoToEntity(String userId, SendingBlockDTO sendingBlockDTO) {
        SendingBlock sendingBlock = SendingBlock.builder()
                .blockTime(new Date().getTime())
                .sender(sendingBlockDTO.getSender())
                .receiver(sendingBlockDTO.getReceiver())
                .regId(userId)
                .isDeleted(false)
                .build();
        return sendingBlock;
    }
}

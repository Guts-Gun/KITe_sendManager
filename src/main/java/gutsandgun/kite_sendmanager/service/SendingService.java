package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendMsgRequestDTO;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.entity.write.Sending;

import java.util.Date;

public interface SendingService {

    Long insertSending(SendMsgRequestDTO sendMsgRequestDTO, Integer userId);    // 발송 저장


    default Sending dtoToEntity(SendingDTO dto, Integer userId) {
        Sending sending = Sending.builder()
                .userId(userId.longValue())
                .ruleType(dto.getRuleType())
                .sendingType(dto.getSendingType())
                .replaceYn(dto.getReplaceYn())
                .totalSending(dto.getTotalSendingCnt())
                .scheduleTime(dto.getScheduleTime())
                .title(dto.getTitle())
                .mediaLink(dto.getMediaLink())
                .content(dto.getContent())
                .requestTime(new Date().getTime())
                .regId(userId.longValue())
                .build();
        return sending;
    }
}

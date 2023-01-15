package gutsandgun.kite_sendmanager.dto;

import gutsandgun.kite_sendmanager.entity.SendingRuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendingDTO {

    private Long id;
    private Long userId;
    private SendingRuleType ruleType;       // 중계사 규칙 타입
    private String sendingType;             // 발송 타입
    private String replaceYn;               // 대체발송 여부
    private Long totalSendingCnt;           // 메세지 갯수
    private Long requestTime;               // 등록 시간
    private Long scheduleTime;              // 예약 시간
    private String title;                   // 제목
    private String mediaLink;               // 미디어 링크
    private String content;                 // 메세지 내용

}



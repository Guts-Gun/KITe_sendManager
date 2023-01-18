package gutsandgun.kite_sendmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gutsandgun.kite_sendmanager.entity.SendingRuleType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SendingDTO {

    private Long id;

    private String userId;

    private SendingRuleType ruleType;       // 중계사 규칙 타입

    private String sendingType;             // 발송 타입

    private String replaceYn;               // 대체발송 여부

    private Long totalSending;              // 메세지 갯수

    private Long requestTime;               // 등록 시간

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationTime;  // 예약 시간

    private Long scheduleTime;              // 예약 시간

    private String title;                   // 제목

    private String mediaLink;               // 미디어 링크

    private String content;                 // 메세지 내용

    private String regId;

    private String modId;

}


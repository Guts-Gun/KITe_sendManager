package gutsandgun.kite_sendmanager.entity.write;

import com.fasterxml.jackson.annotation.JsonFormat;
import gutsandgun.kite_sendmanager.entity.BaseTimeEntity;
import gutsandgun.kite_sendmanager.entity.SendingRuleType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending SET is_deleted=true WHERE id = ?")
@Table(name="sending")
@DynamicInsert
public class Sending extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fk_user_id")
    private Long userId;

    @Comment("분배규칙타입")
    @Enumerated(EnumType.STRING)
    private SendingRuleType ruleType;

    @Comment("발송타입")
    private String sendingType;

    @Comment("대체발송 여부")
    private String replaceYn;

    @Comment("메세지 갯수")
    private Long totalSending;

    @Comment("입력시각")
    private Long requestTime;

    @Comment("예약시각")
    private Long scheduleTime;

    @Comment("제목")
    private String title;

    @Comment("미디어링크")
    private String mediaLink;

    @Comment("문자 내용")
    private String content;

    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @Comment("생성자")
    @Column(name = "reg_id", nullable = false, length = 20)
    private Long regId;

    @Comment("수정자")
    @Column(name = "mod_id", length = 20)
    private Long ModId;

}

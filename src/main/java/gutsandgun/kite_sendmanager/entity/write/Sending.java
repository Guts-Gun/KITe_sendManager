package gutsandgun.kite_sendmanager.entity.write;

import gutsandgun.kite_sendmanager.entity.BaseTimeEntity;
import gutsandgun.kite_sendmanager.type.SendingRuleType;
import gutsandgun.kite_sendmanager.type.SendingType;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending SET is_deleted=true WHERE id = ?")
@Table(name = "sending",
        indexes = {
                @Index(name = "idx_sending_user_id", columnList = "fk_user_id")
        })@DynamicInsert
public class Sending extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fk_user_id")
    private String userId;

    @Comment("분배규칙타입")
    @Enumerated(EnumType.STRING)
    private SendingRuleType sendingRuleType;

    @Comment("발송타입")
    @Enumerated(EnumType.STRING)
    private SendingType sendingType;

    @Comment("대체발송 여부")
    private String replaceYn;

    @Comment("메세지 갯수")
    private Long totalMessage;

    @Comment("입력시각")
    private Long inputTime;

    @Comment("예약시각")
    private Long scheduleTime;

    @Comment("제목")
    private String title;

    @Comment("미디어링크")
    private String mediaLink;

    @Comment("문자 내용")
    private String content;

    @Comment("발신자")
    private String sender;

    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @Comment("생성자")
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    @Comment("수정자")
    @Column(name = "mod_id", length = 20)
    private String modId;

}

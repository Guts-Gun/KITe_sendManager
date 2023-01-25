package gutsandgun.kite_sendmanager.entity.read;

import gutsandgun.kite_sendmanager.type.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending SET is_deleted=true WHERE id = ?")
@Table(name="sending")
public class Sending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * user id
     */
    @Column(name = "fk_user_id")
    @Comment("user id")
    private String userId;

    /**
     * 분배 규칙 타입
     */
    @Comment("분배 규칙 타입")
    private SendingRuleType SendingRuleType;

    /**
     * 발송 메시지 타입
     */
    @Comment("발송 메시지 타입")
    private SendingType sendingType;

    @Comment("대체발송 여부")
    private String replaceYn;

    /**
     * 전체 메시지 개수
     */
    @Comment("전체 메시지 개수")
    private Long totalMessage;

    /**
     * 입력 시간 unix time
     */
    @Comment("입력 시간")
    private Long inputTime;

    /**
     * 예약 시간 unix time
     */
    @Comment("예약 시간")
    private Long scheduleTime;
    /**
     * 메시지 제목
     */
    @Comment("제목")
    private String title;

    /**
     * 미디어 호스팅 주소
     */
    @Comment("미디어 호스팅 주소")
    private String mediaLink;

    /**
     * 메시지 내용
     */
    @Comment("메시지 내용")
    private String content;

    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @Comment("생성자")
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    @Comment("수정자")
    @Column(name = "mod_id", length = 20)
    private String ModId;
}

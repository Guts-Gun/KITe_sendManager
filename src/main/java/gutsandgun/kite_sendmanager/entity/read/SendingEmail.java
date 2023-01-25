package gutsandgun.kite_sendmanager.entity.read;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * 발송할 e mail tx 테이블
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE sending_email SET is_deleted=true WHERE id = ?")
public class SendingEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * sending id
	 */
	@Column(name = "fk_sending_id")
	@Comment("sending id")
	private Long sendingId;

	/**
	 * 발신자 주소
	 */
	@Comment("발신자 주소")
	private String sender;

	/**
	 * 수신자 주소
	 */
	@Comment("수신자 주소")
	private String receiver;

	/**
	 * 사용자지정 변수 - 이름
	 */
	@Comment("사용자지정 변수 - 이름")
	private String name;

	/**
	 * 사용자지정 변수 - 1번
	 */
	@Comment("사용자지정 변수 - 1번")
	private String var1;

	/**
	 * 사용자지정 변수 - 2번
	 */
	@Comment("사용자지정 변수 - 2번")
	private String var2;

	/**
	 * 사용자지정 변수 - 2번
	 */
	@Comment("사용자지정 변수 - 2번")
	private String var3;


	    @ColumnDefault("false")
	private Boolean isDeleted = false;
}

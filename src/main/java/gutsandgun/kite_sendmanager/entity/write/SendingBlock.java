package gutsandgun.kite_sendmanager.entity.write;

import gutsandgun.kite_sendmanager.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending_block SET is_deleted=true WHERE id = ?")
@Builder
@Table(name = "sending_block",
        indexes = {
                @Index(name = "idx_sending_block_sender", columnList = "sender"),
                @Index(name = "idx_sending_block_receiver", columnList = "receiver")

        })
public class SendingBlock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String sender;

    private String receiver;

    private Long blockTime;

    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @Comment("생성자")
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    @Comment("수정자")
    @Column(name = "mod_id", length = 20)
    private String modId;

}

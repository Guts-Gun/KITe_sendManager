package gutsandgun.kite_sendmanager.entity.write;

import gutsandgun.kite_sendmanager.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending_rule SET is_deleted=true WHERE id = ?")
@Table(name = "sending_rule",
        indexes = {
                @Index(name = "idx_sending_rule_user_id", columnList = "fk_user_id"),
                @Index(name = "idx_sending_rule_sending_id", columnList = "fk_sending_id")
        })
@DynamicInsert
public class SendingRule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fk_user_id")
    private String userId;

    @Column(name = "fk_sending_id")
    private Long sendingId;

    @Column(name = "fk_broker_id")
    private Long brokerId;

    private Long weight;

    @ColumnDefault("false")
    private Boolean isDeleted = false;
}

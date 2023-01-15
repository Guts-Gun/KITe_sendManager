package gutsandgun.kite_sendmanager.entity.write;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@Entity
@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE sending_rule SET is_deleted=true WHERE id = ?")
@Table(name="sending_rule")
@DynamicInsert
public class SendingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fk_user_id")
    private Long userId;

    @Column(name = "fk_sending_id")
    private Long sendingId;

    @Column(name = "fk_broker_id")
    private Long brokerId;

    private Long weight;

    @ColumnDefault("false")
    private Boolean isDeleted = false;
}

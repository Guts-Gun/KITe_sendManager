package gutsandgun.kite_sendmanager.repository.write;

import gutsandgun.kite_sendmanager.entity.write.SendingRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteSendingRuleRepository extends JpaRepository<SendingRule, Long> {
}

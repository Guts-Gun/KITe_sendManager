package gutsandgun.kite_sendmanager.repository.write;

import gutsandgun.kite_sendmanager.entity.write.SendingBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteSendingBlockRepository extends JpaRepository<SendingBlock, Long> {
}

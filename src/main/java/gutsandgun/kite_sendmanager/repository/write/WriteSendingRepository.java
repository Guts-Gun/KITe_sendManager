package gutsandgun.kite_sendmanager.repository.write;

import gutsandgun.kite_sendmanager.entity.write.Sending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteSendingRepository extends JpaRepository<Sending, Long> {
}

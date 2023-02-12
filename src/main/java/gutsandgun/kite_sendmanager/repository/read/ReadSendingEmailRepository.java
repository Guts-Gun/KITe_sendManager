package gutsandgun.kite_sendmanager.repository.read;

import gutsandgun.kite_sendmanager.entity.read.SendingEmail;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadSendingEmailRepository extends JpaRepository<SendingEmail, Long> {

    List<SendingEmail> findBySendingId(Long sendingId);
}

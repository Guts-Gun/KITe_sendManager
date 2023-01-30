package gutsandgun.kite_sendmanager.repository.read;

import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadSendingMsgRepository extends JpaRepository<SendingMsg, Long> {

    List<SendingMsg> findBySendingId(Long sendingId);
}

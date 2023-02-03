package gutsandgun.kite_sendmanager.repository.read;

import gutsandgun.kite_sendmanager.entity.read.SendingBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadSendingBlockRepository extends JpaRepository<SendingBlock, Long> {

    List<SendingBlock> findBySender(String Sender);
}

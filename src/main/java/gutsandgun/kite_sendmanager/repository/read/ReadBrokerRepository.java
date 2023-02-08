package gutsandgun.kite_sendmanager.repository.read;

import gutsandgun.kite_sendmanager.entity.read.Broker;
import gutsandgun.kite_sendmanager.type.SendingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadBrokerRepository extends JpaRepository<Broker, Long> {

    List<Broker> findBySendingType(SendingType sendingType);
}

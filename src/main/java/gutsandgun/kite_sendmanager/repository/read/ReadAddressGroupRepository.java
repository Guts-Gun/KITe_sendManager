package gutsandgun.kite_sendmanager.repository.read;

import gutsandgun.kite_sendmanager.entity.read.AddressGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadAddressGroupRepository extends JpaRepository<AddressGroup, Long> {
}

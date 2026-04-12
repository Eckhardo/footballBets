package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import sportbets.persistence.entity.tipps.TippConfig;

public interface TippConfigRepository extends JpaRepository<TippConfig, Integer> {

}

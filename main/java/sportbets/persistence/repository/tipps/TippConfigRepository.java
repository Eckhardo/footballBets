package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.tipps.TippConfig;

public interface TippConfigRepository extends JpaRepository<TippConfig, Integer> {

}

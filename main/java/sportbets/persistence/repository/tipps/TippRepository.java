package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.tipps.Tipp;

public interface TippRepository extends JpaRepository<Tipp, Long> {

}
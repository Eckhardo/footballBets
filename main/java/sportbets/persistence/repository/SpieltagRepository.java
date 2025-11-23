package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.Spieltag;

public interface SpieltagRepository extends JpaRepository<Spieltag, Long> {
}

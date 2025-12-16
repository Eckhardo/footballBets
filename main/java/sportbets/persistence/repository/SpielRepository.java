package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.Spiel;
import sportbets.persistence.entity.Spieltag;

import java.util.List;

public interface SpielRepository extends JpaRepository<Spiel, Long> {

}

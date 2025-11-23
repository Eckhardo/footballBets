package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.Team;

public interface TeamRepository  extends JpaRepository<Team, Long> {
}

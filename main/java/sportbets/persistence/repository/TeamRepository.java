package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.Team;

import java.util.Optional;

public interface TeamRepository  extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
}

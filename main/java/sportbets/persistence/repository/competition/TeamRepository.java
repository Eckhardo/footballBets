package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Team;

import java.util.Optional;
@Repository

public interface TeamRepository  extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
    void deleteByName(String name);
}

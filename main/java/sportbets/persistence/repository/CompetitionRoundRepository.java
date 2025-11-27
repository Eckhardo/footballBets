package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionRound;

import java.util.Optional;

public interface CompetitionRoundRepository extends JpaRepository<CompetitionRound, Long> {
    Optional<CompetitionRound> findByName(String name);

}

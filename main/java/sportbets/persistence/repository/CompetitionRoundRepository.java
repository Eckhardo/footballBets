package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.CompetitionRound;

public interface CompetitionRoundRepository extends JpaRepository<CompetitionRound, Long> {
}

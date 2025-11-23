package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.CompetitionTeam;

public interface CompetitionTeamRepository  extends JpaRepository<CompetitionTeam, Long> {
}

package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.competition.CompetitionMembership;

public interface CompetitionMembershipRepository extends JpaRepository<CompetitionMembership, Long> {
}
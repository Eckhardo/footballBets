package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.CompetitionGroup;

public interface CompetitionGroupRepository extends JpaRepository<CompetitionGroup, Long> {
}

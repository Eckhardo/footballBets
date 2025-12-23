package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionGroup;

import java.util.Optional;
@Repository
public interface CompetitionGroupRepository extends JpaRepository<CompetitionGroup, Long> {
    Optional<CompetitionGroup> findByName(String name);
}

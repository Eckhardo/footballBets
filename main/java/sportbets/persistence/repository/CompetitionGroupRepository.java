package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.CompetitionGroup;

import java.util.Optional;

public interface CompetitionGroupRepository extends JpaRepository<CompetitionGroup, Long> {
    Optional<CompetitionGroup> findByName(String name);
}

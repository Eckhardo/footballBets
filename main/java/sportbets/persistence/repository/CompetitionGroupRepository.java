package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.CompetitionGroup;

import java.util.Optional;
@Repository
public interface CompetitionGroupRepository extends JpaRepository<CompetitionGroup, Long> {
    Optional<CompetitionGroup> findByName(String name);
}

package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.SpielFormula;
@Repository
public interface SpielFormulaRepository extends JpaRepository<SpielFormula, Long> {
}
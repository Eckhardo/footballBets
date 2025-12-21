package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.CompetitionRound;

import java.util.List;
import java.util.Optional;

public interface CompetitionRoundRepository extends JpaRepository<CompetitionRound, Long> {
    Optional<CompetitionRound> findByName(String name);

    @Query("select  cr from CompetitionRound cr"
            + " join fetch cr.competition c"
            + " join fetch c.competitionFamily cf"
            + " where cr.competition.id=c.id"
            + " and c.competitionFamily.id=cf.id "
            + " and cr.id=:roundId")
    Optional<CompetitionRound> findByIdWithParents(Long roundId);
    void deleteByName(String name);

}

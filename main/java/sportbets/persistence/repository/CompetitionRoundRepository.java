package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionRound;

import java.util.Optional;
@Repository
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

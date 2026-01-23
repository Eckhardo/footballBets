package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionTeam;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionTeamRepository extends JpaRepository<CompetitionTeam, Long> {


    @Query("select  ct from CompetitionTeam ct join ct.competition c"
            + " where  c.id=:compId")
    List<CompetitionTeam> getAllFormComp(Long compId);

    @Query(" select ct from CompetitionTeam ct join ct.team t join ct.competition c " +
            "where t.id=:teamId and c.id=:compId  ")
    Optional<CompetitionTeam> findByTeamIdAndCompId(Long teamId, Long compId);
}

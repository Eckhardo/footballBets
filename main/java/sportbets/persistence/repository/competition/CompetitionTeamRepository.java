package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionTeam;

import java.util.List;

@Repository
public interface CompetitionTeamRepository extends JpaRepository<CompetitionTeam, Long> {


    @Query("select  ct from CompetitionTeam ct join fetch  ct.competition c"
            + " where  c.id=:compId")
    List<CompetitionTeam> getAllFormComp(Long compId);

}

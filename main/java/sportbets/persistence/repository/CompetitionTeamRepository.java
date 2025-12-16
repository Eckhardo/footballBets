package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.CompetitionTeam;
import sportbets.persistence.entity.Spiel;

import java.util.List;

public interface CompetitionTeamRepository  extends JpaRepository<CompetitionTeam, Long> {


    @Query("select  ct from CompetitionTeam ct join fetch  ct.competition c"
            + " where  c.id=:compId")
    List<CompetitionTeam> getAllFormComp(Long compId);

}

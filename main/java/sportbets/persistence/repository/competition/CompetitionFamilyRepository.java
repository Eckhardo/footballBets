package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionFamilyRepository extends JpaRepository<CompetitionFamily, Long> {

    Optional<CompetitionFamily> findByName(String name);

    @Query("select  ct.team from CompetitionTeam ct join  ct.competition c join c.competitionFamily cf "
            + " where cf.id =:id"
            + " order by ct.team.name asc")
    List<Team> findTeamsForCompFamily(Long id);

    void deleteByName(String name);
}

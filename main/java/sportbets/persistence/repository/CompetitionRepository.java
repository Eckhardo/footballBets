package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.rowObject.CompRecord;

import java.util.List;
import java.util.Optional;
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Optional<Competition> findByName(String name);
    void deleteByName(String name);
    @Query("select  c from Competition c join fetch  c.competitionFamily cf"
            + " where c.name =:name"
            + " and cf.id=:id"
            + " order by c.name asc")
    Optional<Competition> findByNameWithFamily(String name, Long id);

    @Query("select  c from Competition c join fetch c.competitionRounds"
            + " where c.name =:name"
            + " order by c.name asc")
    Optional<Competition> findByNameJoinFetchRounds(String name);


    @Query("select  c from Competition c join fetch c.competitionRounds"
            + " where c.id =:id"
            + " order by c.name asc")
    Competition findByIdJoinFetchRounds(Long id);

    @Query("select  c from Competition c join fetch c.competitionRounds r"
            + " join fetch r.spieltage  where c.name =:name"
            + " order by c.name asc")
    Optional<Competition> findByNameJoinFetchRoundsAndSpieltage(String name);

    @Query("select  t from Team t  "
            + " join fetch t.competitionTeams ct"
            + " join fetch ct.competition c "
            + " where c.id =:compId"
            + " order by t.acronym asc")
    List<Team> findTeamsForComp(Long compId);


    @Query(" select new sportbets.persistence.rowObject.CompRecord ("
            + "  c.name , c.description, cf.id )"
            + "from Competition c join c.competitionFamily cf"
            + " where c.name = :name and cf.id=:id ")
    CompRecord findCompByNameAndFamily(String name, Long id);

    @Query("select cr from CompetitionRound cr join cr.competition c where c.id=:compId")
    List<CompetitionRound> findAllForComp(Long compId);
}

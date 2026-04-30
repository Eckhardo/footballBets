package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.rowObject.CompRow;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Optional<Competition> findByName(String name);

    void deleteByName(String name);

    @EntityGraph(attributePaths = {"competitionRounds"})
    @Query("select  c from Competition c  "
            + " where c.name =:name"
            + " order by c.name asc")
    Optional<Competition> findByNameJoinFetchRounds(String name);


    @Query("select  c from Competition c  join fetch c.competitionRounds"
            + " where c.id =:compId"
            + " order by c.name asc")
    Competition findByIdJoinFetchRounds(@Param("compId") Long id);

    @Query("select  t from Team t  "
            + " join fetch t.competitionTeams ct"
            + " join fetch ct.competition c "
            + " where c.id =:compId"
            + " order by t.acronym asc")
    List<Team> findTeamsForComp( @Param("compId")Long id);


    @Query(" select new sportbets.persistence.rowObject.CompRow("
            + "  c.name , c.description, cf.id )"
            + "from Competition c join c.competitionFamily cf"
            + " where c.name = :name and cf.id=:familyId ")
    CompRow findCompByNameAndFamily(String name, @Param("familyId") Long id);

    @Query("select cr from CompetitionRound cr join cr.competition c where c.id=:compId")
    List<CompetitionRound> findAllForComp(@Param("compId") Long id);

    @Query(" select c from Competition c join c.competitionFamily cf   "
            + " where  cf.id=:familyId ")
    List<Competition> findByFamilyId(@Param("familyId") Long id);

    @Query("select  c from Competition c join fetch c.competitionRounds r"
            + " join fetch r.spieltage sp where sp.id= :spieltagId")
    Optional<Competition> findBySpieltagId(@Param("spieltagId") Long id);


    @Query("select  c from Competition c join c.competitionRounds r"
            + " where r.id= :roundId")
    Optional<Competition> findByRoundId( @Param("roundId")Long id);

    @Query(" select c from Competition c join fetch c.competitionFamily   ")
    List<Competition> findAllComps();
}

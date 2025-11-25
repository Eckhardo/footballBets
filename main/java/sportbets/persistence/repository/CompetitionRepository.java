package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.rowObject.CompRecord;

import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Optional<Competition> findByName(String name);

    @Query("select  c from Competition c join fetch  c.competitionFamily cf"
            + " where c.name =:name"
            + " and cf.id=:id"
            + " order by c.name asc")
    Competition findByNameWithFamily(String name, Long id);

    @Query("select  c from Competition c join fetch c.competitionRounds"
            + " where c.name =:name"
            + " order by c.name asc")
    Competition findByNameJoinFetchRounds(String name);


    @Query("select  c from Competition c join fetch c.competitionRounds r"
            + " join fetch r.spieltage  where c.name =:name"
            + " order by c.name asc")
    Competition findByNameJoinFetchRoundsAndSpieltage(String name);


    @Query(" select new sportbets.persistence.rowObject.CompRecord ("
            + "  c.name , c.description, cf.id )"
            + "from Competition c join c.competitionFamily cf"
            + " where c.name = :name and cf.id=:id ")
    CompRecord findCompByNameAndFamily(String name, Long id);

}

package sportbets.persistence.repository.competition;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Spieltag;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpieltagRepository extends JpaRepository<Spieltag, Long> {


    @Query("select  sp from Spieltag sp join fetch  sp.competitionRound cr"
            + " where sp.spieltagNumber =:number"
            + " and cr.id=:id")
    Optional<Spieltag> findByNumberWithRoundId(int number, Long id);

    @Query("select  sp from Spieltag sp "
            + " where sp.spieltagNumber =:number")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Spieltag findByNumber(int number);


    @Query("select  sp from Spieltag sp join sp.competitionRound cr "
            + " where sp.spieltagNumber =:number and cr.id= :roundId")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<Spieltag> findByNumberAndRound(int number, Long roundId);


    @Query("select  sp from Spieltag sp join fetch  sp.competitionRound cr"
            + " where  cr.id=:id")
    List<Spieltag> findAllByRoundId(Long id);


    @Query("select  sp from Spieltag sp "
            + " join fetch sp.competitionRound cr"
            + " join fetch cr.competition c"
            + " join fetch c.competitionFamily cf"
            + " where sp.competitionRound.id = cr.id"
            + " and cr.competition.id=c.id"
            + " and c.competitionFamily.id=cf.id "
            + " and sp.id=:spieltagId")
    Optional<Spieltag> findByIdWithParents(Long spieltagId);


}

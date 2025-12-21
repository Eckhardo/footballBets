package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.Spiel;
import sportbets.persistence.entity.Spieltag;

import java.util.List;
import java.util.Optional;

public interface SpieltagRepository extends JpaRepository<Spieltag, Long> {


    @Query("select  sp from Spieltag sp join fetch  sp.competitionRound cr"
            + " where sp.spieltagNumber =:number"
            + " and cr.id=:id")
    Optional<Spieltag> findByNumberWithRoundId(int number, Long id);


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

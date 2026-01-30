package sportbets.persistence.repository.competition;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Spiel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {

    @Query("select  s from Spiel s join fetch  s.spieltag sp"
            + " where  sp.id=:matchDayId")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Spiel> findAllForMatchday(Long matchDayId);


    @Query("select  s from Spiel s join fetch  s.spieltag sp"
            + " where s.spielNumber =:number"
            + " and sp.id=:id")
    Optional<Spiel> findByNumberWithSpieltagId(int number, Long id);
}

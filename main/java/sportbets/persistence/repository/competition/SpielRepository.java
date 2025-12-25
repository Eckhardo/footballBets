package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Spiel;

import java.util.List;

@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {

    @Query("select  s from Spiel s join fetch  s.spieltag sp"
            + " where  sp.id=:matchDayId")
    List<Spiel> findAllForMatchday(Long matchDayId);
}

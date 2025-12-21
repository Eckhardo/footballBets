package sportbets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.Spiel;

import java.util.List;

public interface SpielRepository extends JpaRepository<Spiel, Long> {

    @Query("select  s from Spiel s join fetch  s.spieltag sp"
            + " where  sp.id=:matchDayId")
    List<Spiel> findAllForMatchday(Long matchDayId);
}

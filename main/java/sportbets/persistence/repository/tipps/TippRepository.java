package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.tipps.Tipp;

import java.util.Optional;

public interface TippRepository extends JpaRepository<Tipp, Long> {

    @Query("select t from Tipp t"
            + " join  t.communityMembership cm join t.tippModus join t.spiel"
            + " where t.communityMembership.id=:commMembId and t.tippModus.id= :tippModusId"
            + " and t.spiel.id=:spielId")
    Optional<Tipp> findByParents(Long commMembId, Long tippModusId, Long spielId);
}
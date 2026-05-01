package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;

import java.util.List;
import java.util.Optional;

public interface TippModusRepository extends JpaRepository<TippModus, Long> {

    @Query("SELECT tm FROM TippModus tm join tm.community c  WHERE TYPE(tm) = TippModusToto and c.id=:id")
    List<TippModusToto> findTippModusToto(@Param("id") Long id);
    @Query("SELECT tm FROM TippModus tm join tm.community c  WHERE TYPE(tm) = TippModusResult and c.id=:id")
    List<TippModusResult> findTippModusResult(@Param("id") Long id);
    @Query("SELECT tm FROM TippModus tm join tm.community c  WHERE TYPE(tm) = TippModusPoint and c.id=:id")
    List<TippModusPoint> findTippModusPoint(@Param("id") Long id);



    @Query("select  tm from TippModus tm join tm.community c where c.id=:id")
    List<TippModus> findAllForCommunity(Long id);
    @Query("select  tm from TippModus tm join tm.community c where c.id=:commId and tm.name=:name")
    Optional<TippModus> findByName(Long commId, String name);

    @Query("select  tm from TippModus tm join tm.community c where c.id=:commId and tm.name=:name")
    Optional<TippModus> findByNameAndCommunity(String name, Long commId);
}

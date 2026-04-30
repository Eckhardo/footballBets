package sportbets.persistence.repository.tipps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.web.dto.tipps.TippConfigDto;

import java.util.List;
import java.util.Optional;

public interface TippConfigRepository extends JpaRepository<TippConfig, Long> {


    @Query(" select new sportbets.persistence.rowObject.TippConfigRow"
            + " ("
            + "  c.name, cr.id , cr.name, tm.name,cm.id, sp.spieltagNumber"
            + ") "
            + " from TippConfig tc  join tc.competitionMembership cm join tc.spieltag sp " +
            "  join sp.competitionRound cr join cr.competition c join tc.tippModus tm"
            + " where cm.id= :compMembId order by tc.id ")
    List<TippConfigRow> findTippConfigRows(Long compMembId);


    @Query(" select new sportbets.persistence.rowObject.TippConfigRow"
            + " ("
            + "  c.name, cr.id , cr.name, tm.name,cm.id, sp.spieltagNumber"
            + ") "
            + " from TippConfig tc  join tc.competitionMembership cm join tc.spieltag sp " +
            "  join sp.competitionRound cr join cr.competition c join tc.tippModus tm"
            + " where cm.id= :compMembId and cr.id=:compRoundId order by tc.id ")
    List<TippConfigRow> findAllForRound(Long compRoundId, Long compMembId);

    @Query(" select new sportbets.persistence.rowObject.TippConfigRow"
            + " ("
            + "  c.name, cr.id , cr.name, tm.name,cm.id, sp.spieltagNumber"
            + ") "
            + " from TippConfig tc  join tc.competitionMembership cm join tc.spieltag sp " +
            "  join sp.competitionRound cr join cr.competition c join tc.tippModus tm"
            + " where cm.id= :compMembId and sp.id=:spieltagId order by tc.id ")
    Optional<TippConfigRow>  findTippConfig(Long spieltagId, Long compMembId);
}

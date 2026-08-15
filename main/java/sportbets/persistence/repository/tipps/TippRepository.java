package sportbets.persistence.repository.tipps;

import org.hibernate.HibernateException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TippRepository extends JpaRepository<Tipp, Long> {

    @Query("select t from Tipp t"
            + " join  t.communityMembership cm join t.tippModus join t.spiel"
            + " where t.communityMembership.id=:commMembId and t.tippModus.id= :tippModusId"
            + " and t.spiel.id=:spielId")
    Optional<Tipp> findByParents(Long commMembId, Long tippModusId, Long spielId);


    @Query("select new sportbets.persistence.rowObject.TippRow"
            + " ("
            + "   s.id,s.anpfiffdate,s.heimTore,s.gastTore,s.stattgefunden, ht.acronym, gt.acronym,st.spieltagNumber, cr.name, c.name "
            + ") "
            + " from Spiel s join s.spieltag st join s.heimTeam ht  join s.gastTeam gt   "
            + " join st.competitionRound cr  join cr.competition c  "
            + "  where st.id=:spieltagId   order by s.id asc  ")
    List<TippRow> findEmptyTippRowsForTipper(Long spieltagId);


    @Query("select new sportbets.persistence.rowObject.TippRow"
            + " ("
            + "   s.id,s.anpfiffdate,s.heimTore,s.gastTore,s.stattgefunden, ht.acronym, gt.acronym,st.spieltagNumber, cr.name, c.name, "
            + " t.id, t.heimTipp, t.remisTipp, t.gastTipp,t.winPoints, cm.id "
            + ") "
            + " from Tipp t join t.spiel s join t.communityMembership cm join cm.tipper ti "
            + " join cm.community comm join s.spieltag st join s.heimTeam ht  join s.gastTeam gt   "
            + " join st.competitionRound cr  join cr.competition c "
            + "  where st.id=:spieltagId  and cm.id=:commMembId order by s.id asc  ")
    List<TippRow> findTippRowsForTipper(Long spieltagId, Long commMembId);




@Query("select new sportbets.persistence.rowObject.TippsRow"
        + " ("
        + "   s.id, s.anpfiffdate, s.heimTore, s.gastTore, ti.username, ht.acronym, gt.acronym, cr.name, "
        + " t.id, t.heimTipp, t.remisTipp, t.gastTipp, t.winPoints "
        + ") "
        + " from Tipp t join t.spiel s join t.communityMembership cm join cm.tipper ti "
        + " join cm.community comm join s.spieltag st join s.heimTeam ht  join s.gastTeam gt   "
        + " join st.competitionRound cr  join cr.competition c "
        + "  where st.id=:spieltagId  and comm.id=:commId order by s.id asc  ")
List<TippsRow> findTippsRowsForCommunity(Long spieltagId, Long commId);

}
package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.SpielFormula;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;

import java.util.List;

@Repository
public interface SpielFormulaRepository extends JpaRepository<SpielFormula, Long> {

    @Query(" select new sportbets.persistence.rowObject.TeamPositionSummaryRow"
            + " ("
            + "  sf.teamName, sf.teamNameAcronym , count(st), sum(sf.points), sum(sf.heimTore),  sum(sf.gastTore),"
            + " sum(sf.diffTore),sum(sf.won),sum(sf.remis),sum(sf.lost)"
            + ") "
            + " from SpielFormula sf  join sf.spiel s join s.spieltag st join s.heimTeam ht join s.gastTeam gt  "
            + " join st.competitionRound cr  join cr.competition c  "
            + " where c.id=:compId  and sf.isHeimTeam=:isHeimTeam  and s.stattgefunden=true and st.spieltagNumber between :firstSp and :lastSp"
            + "   group by sf.teamName  ")
    List<TeamPositionSummaryRow> findTableForLigaModus(Long compId, boolean isHeimTeam, long firstSp, Long lastSp);

}
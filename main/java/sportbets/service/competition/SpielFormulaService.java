package sportbets.service.competition;

import sportbets.persistence.rowObject.TeamPositionSummaryRow;

import java.util.List;

public interface SpielFormulaService {


   List< TeamPositionSummaryRow> findSpielFormulaForLigaModus(Long compId, boolean isHeimTeam, long firstSp, Long lastSp);
}

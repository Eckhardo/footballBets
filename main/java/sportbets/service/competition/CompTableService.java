package sportbets.service.competition;

import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.List;

public interface CompTableService {
   List< TeamPositionSummaryRow> findTableHeimOrGastForLigaModus(TableSearchCriteria searchCriteria);

   List< TeamPositionSummaryRow> findTableForLigaModus(TableSearchCriteria searchCriteria);
   List<TeamPositionSummaryRow> findTableForRoundForLigaModus(Long compId, int spieltagStart, int spieltagEnd);
}

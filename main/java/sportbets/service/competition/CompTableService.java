package sportbets.service.competition;

import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.List;

public interface CompTableService {
   List< TeamPositionSummaryRow> findTableHeimOrGastForLigaModus(TableSearchCriteria searchCriteria);
   List< TeamPositionSummaryRow> findTableHeimOrGastForLigaModus2(Long compId, boolean isHeimGast, int spieltagStart, int spieltagEnd);

   List< TeamPositionSummaryRow> findTableForLigaModus(TableSearchCriteria searchCriteria);
}



package sportbets.service.tipps;

import sportbets.persistence.rowObject.SumWinPointsRow;
import sportbets.persistence.rowObject.SumWinPointsSummaryRow;
import sportbets.web.dto.tipps.TippVO;

import java.util.List;

public interface TippTableService {
    List<SumWinPointsRow> findSumWinPointsRows(Long spieltagId, Long commId);
    List<SumWinPointsRow> findSumWinPointsRowsForMatchdays(Integer startSpieltag,Integer stopSpieltag,  Long commId);
    List<SumWinPointsSummaryRow> retrieveTippTable(TippVO tippVO);

}

/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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

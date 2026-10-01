

package sportbets.service.tipps.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.persistence.rowObject.SumWinPointsRow;
import sportbets.persistence.rowObject.SumWinPointsSummaryRow;
import sportbets.service.tipps.TippTableService;
import sportbets.web.dto.tipps.TippVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
public class TippTableServiceImpl implements TippTableService {


    private static final Logger log = LoggerFactory.getLogger(TippTableServiceImpl.class);
    private final TippRepository tippRepo;
    private final CommunityMembershipRepository commMembRepo;

    public TippTableServiceImpl(TippRepository tippRepo, CommunityMembershipRepository commMembRepo) {
        this.tippRepo = tippRepo;
        this.commMembRepo = commMembRepo;
    }


    @Override
    public List<SumWinPointsRow> findSumWinPointsRows(Long spieltagId, Long commId) {
        return tippRepo.findSumWinPointsRows(spieltagId, commId);
    }

    @Override
    public List<SumWinPointsRow> findSumWinPointsRowsForMatchdays(Integer startSpieltag, Integer stopSpieltag, Long commId) {
        return tippRepo.findSumWinPointsRowsForMatchdays(startSpieltag, stopSpieltag, commId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SumWinPointsSummaryRow> retrieveTippTable(TippVO tippVO) {
        log.debug("retrieveTippTable TippVO:: {}", tippVO);

        assert tippVO != null;
        assert tippVO.commId() != null;
        assert tippVO.startSpieltag() != null;
        assert tippVO.stopSpieltag() != null;
        List<String> usernames = new ArrayList<>();
        if (tippVO.stopSpieltag() == 1) {
            return fillTippTableForMatchdayOne(tippVO.commId(), tippVO.startSpieltag(), tippVO.stopSpieltag());
        }


        // fill summary rows with data from matchday before
        List<SumWinPointsSummaryRow> result = preFillTippTable(tippVO, usernames);

        fillTippTable(tippVO, result);
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPoints).reversed());
        log.debug(" postFill size:: {}", result.size());
        log.debug("");
        return result;
    }

    private void fillTippTable(TippVO tippVO, List<SumWinPointsSummaryRow> sumRows) {
        // fill result rows with data from matchday now (just one field)
        List<SumWinPointsRow> latest = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag(), tippVO.commId());
        log.debug("sumWinPoints size : {}", latest.size());
        latest.sort(Comparator.comparing(SumWinPointsRow::getUsername));
        Iterator<SumWinPointsRow> nowIterator = latest.iterator();
        sumRows.sort(Comparator.comparing(SumWinPointsSummaryRow::getUsername));
        Iterator<SumWinPointsSummaryRow> resultIterator = sumRows.iterator();
        int winPointsFirst = 0;
        int winPointsNow = 0;
        int winPointsLatest = 0;
        boolean isFirstRow = true;
        while (nowIterator.hasNext() && resultIterator.hasNext()) {
            SumWinPointsRow winPointsRow = nowIterator.next();
            log.debug("winPointsRow  : {}", winPointsRow);
            SumWinPointsSummaryRow sumWinPointsSummaryRow = resultIterator.next();
            if (isFirstRow) {
                winPointsFirst = winPointsRow.getSumWinPoints()==null?0:winPointsRow.getSumWinPoints().intValue();
                winPointsLatest = winPointsRow.getSumWinPoints()==null?0:winPointsRow.getSumWinPoints().intValue();

                sumWinPointsSummaryRow.setSumWinPoints(winPointsFirst);
                sumWinPointsSummaryRow.setDiffAbsolute(0);
                sumWinPointsSummaryRow.setDiffRelative(0);
                isFirstRow = false;
            } else {
                winPointsNow= winPointsRow.getSumWinPoints().intValue();
                sumWinPointsSummaryRow.setSumWinPoints(winPointsNow);
                sumWinPointsSummaryRow.setDiffAbsolute(winPointsNow - winPointsFirst);
                sumWinPointsSummaryRow.setDiffRelative(winPointsNow - winPointsLatest);
                winPointsLatest = winPointsNow;
            }


        }
        sumRows.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPoints).reversed());
        winPointsNow = 0;
        winPointsFirst = 0;
        winPointsLatest = 0;
        int position = 1;
        int samePosition = 0;
        isFirstRow = true;
        for (SumWinPointsSummaryRow sumRow : sumRows) {
            if (isFirstRow) {
                sumRow.setPosition(position);
                isFirstRow = false;
                winPointsLatest=sumRow.getSumWinPoints();
            }
            else {
                winPointsNow = sumRow.getSumWinPoints();
                if (winPointsNow < winPointsLatest) {
                    position = ++position + samePosition;
                } else if (winPointsNow == winPointsLatest) {
                    ++samePosition;
                }
                sumRow.setPosition(position);
                winPointsLatest = winPointsNow;
            }
        }


    }

    private void fillDummies(TippVO tippVO, List<String> usernames, int sumWinPointsFirst, int sumWinPointsBefore, List<SumWinPointsSummaryRow> result) {
        List<Tipper> tippers = commMembRepo.findTippers(tippVO.commId());

        // if no tipps are present for one of tippers of the community, set dummy
        for (Tipper tipper : tippers) {
            String username = tipper.getUsername();
            if (!usernames.contains(username)) {
                SumWinPointsSummaryRow dummy = new SumWinPointsSummaryRow(username, 0, 0, 1);
                dummy.setDiffAbsolute(sumWinPointsFirst);
                dummy.setDiffRelative(sumWinPointsBefore);
                result.add(dummy);
                usernames.add(username);

            }

        }
    }

    /**
     * Pre-fill TippTable rows with data from matchday before current matchday
     *
     * @param tippVO
     * @param usernames
     * @return
     */
    private List<SumWinPointsSummaryRow> preFillTippTable(TippVO tippVO, List<String> usernames) {
        log.debug("preFillTippTable tippVO:: {}", tippVO);
        List<SumWinPointsSummaryRow> tippTableRows = new ArrayList<>();
        List<SumWinPointsRow> latestMinusOne = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag() - 1, tippVO.commId());
        latestMinusOne.sort(Comparator.comparing(SumWinPointsRow::getSumWinPoints).reversed());

        boolean isFirstRow = true;
        int winPointsNow = 1;
        int winPointsBefore = 1;
        int position = 1;
        for (SumWinPointsRow sumRow : latestMinusOne) {
            SumWinPointsSummaryRow row = null;
            if (isFirstRow) {
                winPointsNow = sumRow.getSumWinPoints()==null? 0:sumRow.getSumWinPoints().intValue();
                winPointsBefore = sumRow.getSumWinPoints()==null? 0:sumRow.getSumWinPoints().intValue();
                row = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, 0, position);
                isFirstRow = false;
                tippTableRows.add(row);
                usernames.add(sumRow.getUsername());
            } else {
                winPointsNow = sumRow.getSumWinPoints().intValue();
                int latestPosition = calculatePosition(winPointsNow, winPointsBefore, position);
                row = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, 0, latestPosition);
                tippTableRows.add(row);
                winPointsBefore = sumRow.getSumWinPoints().intValue();
                position = latestPosition;
                usernames.add(sumRow.getUsername());
            }

        }
        log.debug("finished preFillTippTable tippVO");
        return tippTableRows;
    }

    /**
     * @param commId
     * @param startSpieltag
     * @param stopSpieltag
     * @return
     */
    private List<SumWinPointsSummaryRow> fillTippTableForMatchdayOne(Long commId, Integer startSpieltag, Integer stopSpieltag) {
        log.debug("fillTippTableForMatchdayOne:");
        List<SumWinPointsSummaryRow> tippTableRows = new ArrayList<>();
        List<SumWinPointsRow> sumWinPointsRows = tippRepo.findSumWinPointsRowsForMatchdays(startSpieltag, stopSpieltag, commId);

        List<String> usernames = new ArrayList<>();
        if (!sumWinPointsRows.isEmpty()) {
            sumWinPointsRows.sort(Comparator.comparing(SumWinPointsRow::getSumWinPoints).reversed());
            int winPointsFirst = 0;
            int winPointsNow = 0;
            int winPointsLatest = 0;
            int position = 1;
            int samePosition = 0;
            boolean firstRow = true;
            for (SumWinPointsRow sumRow : sumWinPointsRows) {
                if (firstRow) {
                    winPointsFirst = sumRow.getSumWinPoints()==null?0: sumRow.getSumWinPoints().intValue();
                    winPointsLatest =sumRow.getSumWinPoints()==null?0: sumRow.getSumWinPoints().intValue();
                    SumWinPointsSummaryRow summary = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsFirst, position, 1);
                    summary.setDiffAbsolute(0);
                    summary.setDiffRelative(0);
                    tippTableRows.add(summary);
                } else {
                    winPointsNow =sumRow.getSumWinPoints()==null?0: sumRow.getSumWinPoints().intValue();
                    if (winPointsNow < winPointsLatest) {
                        position = ++position + samePosition;
                    } else if (winPointsNow == winPointsLatest) {
                        ++samePosition;
                    }
                    log.debug("winPointsLatest {} winPointsNow {}", winPointsLatest, winPointsNow);
                    SumWinPointsSummaryRow summaryRow = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, position, 1);
                    summaryRow.setDiffAbsolute(winPointsNow - winPointsFirst);
                    summaryRow.setDiffRelative(winPointsNow - winPointsLatest);
                    tippTableRows.add(summaryRow);
                    winPointsLatest = winPointsNow;
                }
                firstRow = false;
                usernames.add(sumRow.getUsername());
            }
        }

        List<Tipper> tippers = commMembRepo.findTippers(commId);
        for (Tipper tipper : tippers) {
            String username = tipper.getUsername();
            if (!usernames.contains(username)) {
                tippTableRows.add(new SumWinPointsSummaryRow(username, 0, tippers.size(), 1));
                usernames.add(username);
            }
        }
        return tippTableRows;
    }

    /**
     * @param winPointsNow
     * @param winPointsBefore
     * @param position
     * @return
     */
    private int calculatePosition(int winPointsNow, int winPointsBefore, int position) {
        if (winPointsNow == winPointsBefore) {
            return position;
        } else {
            return position + 1;
        }
    }


}

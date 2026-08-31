/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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

        // fill result rows with data from matchday before
        if (tippVO.stopSpieltag() == 1) {
            return fillTippTableForMatchdayOne(tippVO);
        }

        List<String> usernames = new ArrayList<>();

        List<SumWinPointsSummaryRow> result = preFillTippTable(tippVO, usernames);
        fillTippTable(tippVO, result);

        // now set diffAbs and diffRel

        int sumWinPointsFirst = 0;
        int sumWinPointsBefore = 0;
        boolean isFirstRow = true;
        for (SumWinPointsSummaryRow sumRow : result) {
            if (isFirstRow) {
                sumRow.setDiffAbsolute(0);
                sumRow.setDiffRelative(0);
                isFirstRow = false;
                sumWinPointsFirst = sumRow.getSumWinPoints();
                sumWinPointsBefore = sumRow.getSumWinPoints();

            } else {
                int sumWinPointsNow = sumRow.getSumWinPoints();
                sumRow.setDiffAbsolute(sumWinPointsNow - sumWinPointsFirst);
                sumRow.setDiffRelative(sumWinPointsNow - sumWinPointsBefore);
                sumWinPointsBefore = sumWinPointsNow;
            }

        }

        fillDummies(tippVO, usernames, sumWinPointsFirst, sumWinPointsBefore, result);
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPoints).reversed());
        return result;
    }

    private void fillTippTable(TippVO tippVO, List<SumWinPointsSummaryRow> result) {
        // fill result rows with data from matchday now (just one field)
        List<SumWinPointsRow> latest = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag(), tippVO.commId());
        log.debug("sumWinPoints size : {}", latest.size());
        latest.sort(Comparator.comparing(SumWinPointsRow::getUsername));
        Iterator<SumWinPointsRow> nowIterator = latest.iterator();
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getUsername));
        Iterator<SumWinPointsSummaryRow> resultIterator = result.iterator();

        while (nowIterator.hasNext() && resultIterator.hasNext()) {
            SumWinPointsRow winPointsRow = nowIterator.next();
            log.debug("winPointsRow  : {}", winPointsRow);
            SumWinPointsSummaryRow sumWinPointsSummaryRow = resultIterator.next();
            Long sumWinPoints = winPointsRow.getSumWinPoints() != null ? winPointsRow.getSumWinPoints() : 0L;

            sumWinPointsSummaryRow.setSumWinPoints(sumWinPoints.intValue());
        }
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPoints).reversed());
    }

    private void fillDummies(TippVO tippVO, List<String> usernames, int sumWinPointsFirst, int sumWinPointsBefore, List<SumWinPointsSummaryRow> result) {
        List<Tipper> tippers = commMembRepo.findTippers(tippVO.commId());

        // if no tipps are present for one of tipppers of the community, set dummy
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
                winPointsNow = sumRow.getSumWinPoints().intValue();
                winPointsBefore = sumRow.getSumWinPoints().intValue();
                row = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, winPointsBefore, position);
                isFirstRow = false;
                tippTableRows.add(row);
                usernames.add(sumRow.getUsername());
            } else {
                winPointsNow = sumRow.getSumWinPoints().intValue();
                int latestPosition = calculatePosition(winPointsNow, winPointsBefore, position);
                row = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, winPointsBefore, latestPosition);
                tippTableRows.add(row);
                winPointsBefore = sumRow.getSumWinPoints().intValue();
                position = latestPosition;
                usernames.add(sumRow.getUsername());
            }

        }
        return tippTableRows;
    }

    /**
     * fill TippTable rows with data from current matchday
     *
     * @param tippVO
     * @return
     */
    private List<SumWinPointsSummaryRow> fillTippTableForMatchdayOne(TippVO tippVO) {
        log.debug("fillTippTableForMatchdayOne:");
        List<SumWinPointsSummaryRow> tippTableRows = new ArrayList<>();
        List<SumWinPointsRow> sumWinPointsRows = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag(), tippVO.commId());

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
                    winPointsFirst = sumRow.getSumWinPoints().intValue();
                    winPointsLatest = sumRow.getSumWinPoints().intValue();
                    SumWinPointsSummaryRow summary = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsFirst, position, 1);
                    summary.setDiffAbsolute(0);
                    summary.setDiffRelative(0);
                    tippTableRows.add(summary);
                } else {
                    winPointsNow = sumRow.getSumWinPoints().intValue();
                    if (winPointsNow < winPointsLatest) {
                        position = ++position + samePosition;
                    } else if (winPointsNow == winPointsLatest) {
                        ++samePosition;
                    }
                    log.debug("winPointsLatest {} winPointsNow {}", winPointsLatest, winPointsNow);
                    SumWinPointsSummaryRow summaryRow = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, position, 1);
                    summaryRow.setDiffAbsolute(winPointsNow-winPointsFirst);
                    summaryRow.setDiffRelative(winPointsNow-winPointsLatest);
                    tippTableRows.add(summaryRow);
                    winPointsLatest = winPointsNow;
                }
                firstRow = false;


                usernames.add(sumRow.getUsername());
            }
        }

        List<Tipper> tippers = commMembRepo.findTippers(tippVO.commId());
        for (Tipper tipper : tippers) {
            String username = tipper.getUsername();
            if (!usernames.contains(username)) {
                tippTableRows.add(new SumWinPointsSummaryRow(username, 0, 0, 1));
                usernames.add(username);
            }
        }
        return tippTableRows;
    }

    private int calculatePosition(int winPointsNow, int winPointsBefore, int position) {
        if (winPointsNow == winPointsBefore) {
            return position;
        } else {
            return position + 1;
        }
    }


}

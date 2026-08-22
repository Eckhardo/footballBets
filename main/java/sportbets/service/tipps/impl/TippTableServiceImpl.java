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
    public List<SumWinPointsSummaryRow> retrieveTippTable(TippVO tippVO) {
        log.debug("retrieveTippTable TippVO:: {}", tippVO);
        List<SumWinPointsSummaryRow> result = new ArrayList<>();

        List<Tipper> tippers = commMembRepo.findTippers(tippVO.commId());
        List<String> usernames = new ArrayList<>();

        // fill result rows with data from matchday before
        if (tippVO.stopSpieltag() == 1) {
            List<SumWinPointsRow> latest = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag(), tippVO.commId());

            if (!latest.isEmpty()) {
                latest.sort(Comparator.comparing(SumWinPointsRow::getSumWinPoints));
                for (SumWinPointsRow sumRow : latest) {
                    result.add(new SumWinPointsSummaryRow(sumRow.getUsername(), sumRow.getSumWinPoints().intValue(), 0, 1));
                    usernames.add(sumRow.getUsername());
                }
            }
            for (Tipper tipper : tippers) {
                String username = tipper.getUsername();
                if (!usernames.contains(username)) {
                    result.add(new SumWinPointsSummaryRow(username, 0, 0, 1));
                    usernames.add(username);

                }

            }
            return result;
        } else {

            List<SumWinPointsRow> latestMinusOne = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag() - 1, tippVO.commId());
            latestMinusOne.sort(Comparator.comparing(SumWinPointsRow::getSumWinPoints).reversed());
            log.debug("SumWinPointsRow size:: {}", latestMinusOne.size());
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
                    result.add(row);
                    usernames.add(sumRow.getUsername());
                } else {
                    winPointsNow = sumRow.getSumWinPoints().intValue();
                    int latestPosition = calculatePosition(winPointsNow, winPointsBefore, position);
                    row = new SumWinPointsSummaryRow(sumRow.getUsername(), winPointsNow, winPointsBefore, latestPosition);
                    result.add(row);
                    winPointsBefore = sumRow.getSumWinPoints().intValue();
                    position = latestPosition;
                    usernames.add(sumRow.getUsername());
                }

            }
        }


        // fill result rows with data from matchday now (just one field)
        List<SumWinPointsRow> latest = tippRepo.findSumWinPointsRowsForMatchdays(tippVO.startSpieltag(), tippVO.stopSpieltag(), tippVO.commId());
        latest.sort(Comparator.comparing(SumWinPointsRow::getUsername));
        Iterator<SumWinPointsRow> nowIterator = latest.iterator();
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getUsername));
        Iterator<SumWinPointsSummaryRow> resultIterator = result.iterator();

        while (nowIterator.hasNext() && resultIterator.hasNext()) {
            SumWinPointsRow winPointsRow = nowIterator.next();
            SumWinPointsSummaryRow sumWinPointsSummaryRow = resultIterator.next();

            sumWinPointsSummaryRow.setSumWinPointsNow(winPointsRow.getSumWinPoints().intValue());
        }
        result.forEach(System.out::println);
        // now set diffAbs and diffRel
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPointsNow).reversed());
        int sumWinPointsFirst = 0;
        int sumWinPointsBefore = 0;
        boolean isFirstRow = true;
        for (SumWinPointsSummaryRow sumRow : result) {
            if (isFirstRow) {
                sumRow.setDiffAbsolute(0);
                sumRow.setDiffRelative(0);
                isFirstRow = false;
                sumWinPointsFirst = sumRow.getSumWinPointsNow();
                sumWinPointsBefore = sumRow.getSumWinPointsNow();

            } else {
                int sumWinPointsNow = sumRow.getSumWinPointsNow();
                sumRow.setDiffAbsolute(sumWinPointsNow - sumWinPointsFirst);
                sumRow.setDiffRelative(sumWinPointsNow - sumWinPointsBefore);
                sumWinPointsBefore = sumWinPointsNow;
            }

        }
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
        result.sort(Comparator.comparing(SumWinPointsSummaryRow::getSumWinPointsNow).reversed());
        log.debug("final result: \n");
        for (SumWinPointsSummaryRow sumRow : result) {

            log.debug("name, sum {},{}: ", sumRow.getUsername(), sumRow.getSumWinPointsNow());
        }
        log.debug("return: \n");
        return result;
    }

    private int calculatePosition(int winPointsNow, int winPointsBefore, int position) {
        if (winPointsNow == winPointsBefore) {
            return position;
        } else {
            return position + 1;
        }
    }


}

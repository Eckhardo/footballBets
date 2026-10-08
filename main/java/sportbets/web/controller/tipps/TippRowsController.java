/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.controller.tipps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;
import sportbets.service.tipps.TippService;
import sportbets.web.dto.tipps.TippVO;
import sportbets.web.dto.tipps.TippsContainerDto;

import java.util.List;

@RestController
@RequestMapping("/tippRows")
public class TippRowsController {


    private static final Logger log = LoggerFactory.getLogger(TippRowsController.class);

    private final TippService tippService;

    public TippRowsController(TippService tippService) {
        this.tippService = tippService;
    }

    @GetMapping()
    public List<TippsRow> findTippRowsForCommunity(@ModelAttribute TippVO tippVO) {
        log.debug(":findTippRowsForCommunity tippVO::{}", tippVO);
        return tippService.findTippsRowsForCommunity(tippVO.spieltagId(), tippVO.commId());

    }

    @GetMapping("{spieltagId}/container/{commMembId}")
    public TippsContainerDto findTippContainerForTipper(@PathVariable Long spieltagId, @PathVariable Long commMembId) {
        log.debug(":findTippContainerForTipper r::{} {}", spieltagId, commMembId);
        TippsContainerDto container;
        List<TippRow> updateableRows = tippService.findTippRowsForTipper(spieltagId, commMembId);
        log.debug(":updateableRows:: {}",updateableRows.size());
        if (updateableRows.isEmpty()) {
            log.debug(":empty::{} {}", spieltagId, commMembId);
            List<TippRow> rows = tippService.findEmptyTippRowsForTipper(spieltagId);
            log.debug(":emptyRows:: {}",rows.size());
            rows.forEach(row -> row.setCommMembId(commMembId));
            container = new TippsContainerDto(rows, false, commMembId, spieltagId);
        } else {
            container = new TippsContainerDto(updateableRows, true, commMembId, spieltagId);
            log.debug(":full::{} {}", container.getTippRows().size(), container.isUpdate());
        }
        return container;
    }


}

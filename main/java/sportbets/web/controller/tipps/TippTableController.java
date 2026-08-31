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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sportbets.persistence.rowObject.SumWinPointsSummaryRow;
import sportbets.service.tipps.TippTableService;
import sportbets.web.dto.tipps.TippVO;

import java.util.List;

@RestController
@RequestMapping("/tippTable")
public class TippTableController {


    private static final Logger log = LoggerFactory.getLogger(TippTableController.class);
    private final TippTableService tippTableService;

    public TippTableController(TippTableService tippTableService) {
        this.tippTableService = tippTableService;
    }

    @GetMapping()
    public List<SumWinPointsSummaryRow> retrieveTippTable(@ModelAttribute  TippVO tippVO){
        log.debug(":retrieveTippTable tippVO::{}", tippVO);
        return tippTableService.retrieveTippTable(tippVO);
    }
}

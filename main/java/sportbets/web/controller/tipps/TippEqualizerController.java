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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportbets.persistence.rowObject.TippRow;
import sportbets.service.tipps.TippEqualizerService;

import java.util.List;

@RestController
@RequestMapping("/equalize")
public class TippEqualizerController {

    private static final Logger log = LoggerFactory.getLogger(TippEqualizerController.class);
    private final TippEqualizerService equalizerService;


    public TippEqualizerController(TippEqualizerService equalizerService) {
        this.equalizerService = equalizerService;
    }


    @GetMapping("/{spieltagId}")
    public ResponseEntity<Void> equalize(@PathVariable Long spieltagId) {
        log.debug("equalize tipps  {}", spieltagId);

        equalizerService.equalizeTippsForMatchday(spieltagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}

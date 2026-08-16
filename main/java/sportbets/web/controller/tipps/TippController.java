package sportbets.web.controller.tipps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;
import sportbets.service.tipps.TippEqualizerService;
import sportbets.service.tipps.TippService;
import sportbets.web.dto.tipps.TippVO;
import sportbets.web.dto.tipps.TippsContainerDto;

import java.util.List;

@RestController
@RequestMapping("/tipps")
public class TippController {

    private static final Logger log = LoggerFactory.getLogger(TippController.class);

    private final TippService tippService;

    public TippController(TippService tippService) {
        this.tippService = tippService;

    }

    @PostMapping()
    public ResponseEntity<Void> postRowList(@RequestBody List<TippRow> rows) {
        log.debug("save tipp rows   {}", rows.size());
        rows.forEach(System.out::println);
        tippService.createOrUpdateRowList(null, rows);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PutMapping("{spieltagId}")
    public ResponseEntity<Void> updateRowList(@PathVariable Long spieltagId, @RequestBody List<TippRow> rows) {
        log.debug("update tipp rows   {}", rows.size());
        rows.forEach(System.out::println);
        tippService.createOrUpdateRowList(spieltagId, rows);
        log.debug("saved tipp rows  ");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping()
    public   List<TippsRow> findTippRowsForCommunity(@ModelAttribute TippVO tippVO) {
        log.debug(":findTippRowsForCommunity tippVO::{}", tippVO);
        return tippService.findTippsRowsForCommunity(tippVO.spieltagId(), tippVO.commId());

    }

    @GetMapping("{spieltagId}/container/{commMembId}")
    public TippsContainerDto findTippContainerForTipper(@PathVariable Long spieltagId, @PathVariable Long commMembId) {
        log.debug(":findTippContainerForTipper r::{} {}", spieltagId, commMembId);
        TippsContainerDto container = null;
        List<TippRow> updateableRows = tippService.findTippRowsForTipper(spieltagId, commMembId);
        if (updateableRows.isEmpty()) {
            log.debug(":empty::{} {}", spieltagId, commMembId);
            List<TippRow> rows = tippService.findEmptyTippRowsForTipper(spieltagId);
            rows.forEach(row -> row.setCommMembId(commMembId));
            container = new TippsContainerDto(rows, false, commMembId, spieltagId);
        } else {

            container = new TippsContainerDto(updateableRows, true, commMembId, spieltagId);
            log.debug(":full::{} {}", spieltagId, container.isUpdate());
        }
        return container;
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug(".delete::{}", id);
        tippService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

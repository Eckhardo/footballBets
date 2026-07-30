package sportbets.web.controller.tipps;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.rowObject.TippRow;
import sportbets.service.tipps.TippService;
import sportbets.web.dto.tipps.TippDto;

import java.util.List;

@RestController
@RequestMapping("/tipps")
public class TippController {

    private static final Logger log = LoggerFactory.getLogger(TippController.class);

    private final TippService tippService;

    public TippController(TippService tippService) {
        this.tippService = tippService;
    }


    @GetMapping("/{id}")
    public TippDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        return tippService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/rows/{spieltagId}")
    public List<TippRow> findEmptyTippRowsForTipper(@PathVariable Long spieltagId) {
        log.debug(":find empty Rows::{}", spieltagId);
        return tippService.findEmptyTippRowsForTipper(spieltagId);
    }

    @GetMapping("/rows/{spieltagId}/commMemb/{commMembId}")
    public List<TippRow> findTippRowsForTipper(@PathVariable Long spieltagId,@PathVariable Long commMembId) {
        log.debug(":findRows fpr tipper::{} {}", spieltagId,commMembId);
        return tippService.findTippRowsForTipper(spieltagId,commMembId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TippDto post(@RequestBody @Valid TippDto newDto) {
        log.debug("save tipp config  {}", newDto);
        TippDto saved = tippService.saveOne(newDto);
        log.debug("saved tipp   {}", saved);
        return saved;
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug(".delete::{}", id);

        tippService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}

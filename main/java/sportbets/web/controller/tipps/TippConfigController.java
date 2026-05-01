package sportbets.web.controller.tipps;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.service.tipps.TippConfigService;
import sportbets.service.tipps.TippModusService;
import sportbets.web.dto.tipps.TippConfigDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;

@RestController
@RequestMapping("/tippConfig")
public class TippConfigController {

    private static final Logger log = LoggerFactory.getLogger(TippConfigController.class);

    private final TippConfigService tippConfigService;


    public TippConfigController(TippConfigService tippConfigService) {
        this.tippConfigService = tippConfigService;
    }

    @GetMapping("/{id}")
    public TippConfigDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        return tippConfigService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



    @GetMapping("/rows/{id}")
    public List<TippConfigRow> findRows(@PathVariable Long id) {
        log.debug(":findRows::{}", id);
        return tippConfigService.findTippConfigRows(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TippConfigDto post(@RequestBody @Valid TippConfigDto newDto) {
        log.debug("save tipp config  {}", newDto);
      TippConfigDto saved= tippConfigService.save(newDto);
        log.debug("saved tipp config  {}", saved);
        return saved;
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.debug(".delete::{}", id);
        try {
            tippConfigService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

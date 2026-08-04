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
import sportbets.web.dto.tipps.TippConfigDto;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{matchdayId}/compMemb/{compMembId}")
    public TippConfigDto findByMatchdayAndCompMemb(@PathVariable Long matchdayId,@PathVariable Long compMembId) {
        log.debug(":findByMatchdayAndCompMemb::{} {}", matchdayId,compMembId);
        TippConfigDto dto=tippConfigService.findByMatchdayAndCompMemb(matchdayId,compMembId);
        log.debug(":return dto::{}", dto);
        return dto;

    }


    @GetMapping("/rows/{id}")
    public List<TippConfigRow> findRowsByCompMembId(@PathVariable Long id) {
        log.debug(":findRows::{}", id);
        return tippConfigService.findTippConfigRows(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TippConfigDto post(@RequestBody @Valid TippConfigDto newDto) {
        log.debug("save tipp config  {}", newDto);
        TippConfigDto saved = tippConfigService.save(newDto);
        log.debug("saved tipp config  {}", saved);
        return saved;
    }


    @PutMapping("/{id}")
    public TippConfigDto update(@PathVariable Long id, @RequestBody @Valid TippConfigRow dto) {
        log.debug("update tipp config  {}", dto);
        TippConfigDto saved = tippConfigService.update(id, dto).orElseThrow();
        log.debug("updated tipp config  {}", saved);
        return saved;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug(".delete::{}", id);

        tippConfigService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

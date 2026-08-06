/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.controller.tipps;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.service.tipps.TippConfigService;
import sportbets.web.dto.tipps.TippConfigDto;

import java.util.List;

@RestController
@RequestMapping("/tippConfig")
public class TippConfigRowController {


    private static final Logger log = LoggerFactory.getLogger(TippConfigRowController.class);


    private final TippConfigService tippConfigService;


    public TippConfigRowController(TippConfigService tippConfigService) {
        this.tippConfigService = tippConfigService;
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

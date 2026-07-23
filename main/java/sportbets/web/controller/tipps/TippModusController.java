package sportbets.web.controller.tipps;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.tipps.TippModusService;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;

@RestController
@RequestMapping("/tippModus")
class TippModusController {

    private static final Logger log = LoggerFactory.getLogger(TippModusController.class);
    private final TippModusService tippModusService;

    public TippModusController(TippModusService tippModusService) {
        this.tippModusService = tippModusService;
    }


    @GetMapping("/community/{id}")
    public List<TippModusDto> getAllForCommunity(@PathVariable Long id) {
        log.debug("TippModusController:getAllForCommunity::{}", id);
      List<TippModusDto> result= tippModusService.getAllForCommunity(id);

      for(TippModusDto dto: result){
          log.debug("TippModusController:getAllForCommunity::{}",dto);
      }
      return result;
    }

    // for tippmodus Toto
    @GetMapping("/{id}")
    public TippModusDto findOne(@PathVariable Long id) {
        log.debug("TippModusController:findOneToto::{}", id);
        return tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/toto")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusDto postToto(@RequestBody @Valid TippModusTotoDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return tippModusService.save(newToto);
    }

    @PutMapping("/toto/{id}")
    public TippModusDto updateToto(@PathVariable Long id, @RequestBody TippModusTotoDto totoDto) {
        log.debug("update tipp modus toto  {}", totoDto);
        return tippModusService.update(id, totoDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



    @PostMapping("/result")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusDto postToto(@RequestBody @Valid TippModusResultDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return tippModusService.save(newToto);
    }

    @PutMapping("/result/{id}")
    public TippModusDto updateResult(@PathVariable Long id, @RequestBody TippModusResultDto resultDto) {
        log.debug("update tipp modus toto  {}", resultDto);
        return tippModusService.update(id, resultDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/point")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusDto postPoint(@RequestBody @Valid TippModusPointDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return tippModusService.save(newToto);
    }

    @PutMapping("/point/{id}")
    public TippModusDto updatePoint(@PathVariable Long id, @RequestBody TippModusPointDto pointDto) {
        log.debug("update tipp modus toto  {}", pointDto);
        return tippModusService.update(id, pointDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("TippModusController.delete::{}", id);

            tippModusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/types")
    public List<TippModusDto> findTypes() {
        log.debug("TippModusController:findTypes::");
        return tippModusService.findTipModusTypes();
    }
}

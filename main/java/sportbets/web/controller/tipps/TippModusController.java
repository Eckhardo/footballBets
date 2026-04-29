package sportbets.web.controller.tipps;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.tipps.TippModusService;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

@RestController
@RequestMapping("/tippModus")
class TippModusController {

    private static final Logger log = LoggerFactory.getLogger(TippModusController.class);
    private final TippModusService tippModusService;

    public TippModusController(TippModusService tippModusService) {
        this.tippModusService = tippModusService;
    }

    // for tippmodus Toto
    @GetMapping("/toto/{id}")
    public TippModusTotoDto findOneToto(@PathVariable Long id) {
        log.debug("TippModusController:findOneToto::{}", id);
        return (TippModusTotoDto) tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/toto")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusTotoDto postToto(@RequestBody @Valid TippModusTotoDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return (TippModusTotoDto) tippModusService.save(newToto);
    }
    @PutMapping("/toto/{id}")
    public TippModusTotoDto updateToto(@PathVariable Long id, @RequestBody TippModusTotoDto totoDto) {
        log.debug("update tipp modus toto  {}", totoDto);
        return (TippModusTotoDto) tippModusService.update(id,totoDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    // for tippmodus Result
    @GetMapping("/result/{id}")
    public TippModusResultDto findOneResult(@PathVariable Long id) {
        log.debug("TippModusController:findOneResult::{}", id);
        return (TippModusResultDto) tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/result")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusResultDto postToto(@RequestBody @Valid TippModusResultDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return (TippModusResultDto) tippModusService.save(newToto);
    }
    @PutMapping("/result/{id}")
    public TippModusResultDto updateResult(@PathVariable Long id, @RequestBody TippModusResultDto resultDto) {
        log.debug("update tipp modus toto  {}", resultDto);
        return (TippModusResultDto) tippModusService.update(id, resultDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    // for tippmodus Point
    @GetMapping("/point/{id}")
    public TippModusPointDto findOnePoint(@PathVariable Long id) {
        log.debug("TippModusController:findOneResult::{}", id);
        return (TippModusPointDto) tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/point")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusPointDto postPoint(@RequestBody @Valid TippModusPointDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);
        return (TippModusPointDto) tippModusService.save(newToto);
    }
    @PutMapping("/point/{id}")
    public TippModusPointDto updatePoint(@PathVariable Long id, @RequestBody TippModusPointDto pointDto) {
        log.debug("update tipp modus toto  {}", pointDto);
        return (TippModusPointDto) tippModusService.update(id,pointDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.debug("TippModusController.delete::{}", id);
        try {
            tippModusService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

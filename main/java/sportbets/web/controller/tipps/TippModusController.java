package sportbets.web.controller.tipps;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.tipps.TippModusService;
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

    @GetMapping("/toto{id}")
    public TippModusTotoDto findOneToto(@PathVariable Long id) {
        log.debug("TippModusController:findOneToto::{}", id);
        return (TippModusTotoDto) tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/result{id}")
    public TippModusResultDto findOneResult(@PathVariable Long id) {
        log.debug("TippModusController:findOneResult::{}", id);
        return (TippModusResultDto) tippModusService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/toto")
    @ResponseStatus(HttpStatus.CREATED)
    public TippModusTotoDto postToto(@RequestBody @Valid TippModusTotoDto newToto) {
        log.debug("New tipp modus toto  {}", newToto);

        return (TippModusTotoDto) tippModusService.save(newToto);

    }
}

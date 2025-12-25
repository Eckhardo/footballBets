package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.competition.SpielService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.competition.SpielDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.List;

@RestController
public class SpieltagController {

    private static final Logger log = LoggerFactory.getLogger(SpieltagController.class);
    private final SpieltagService spieltagService;
    private final SpielService spielService;

    public SpieltagController(SpieltagService spieltagService, SpielService spielService) {
        this.spieltagService = spieltagService;
        this.spielService = spielService;
    }
    @GetMapping("/matchdays")
    public List<SpieltagDto> findAll() {
        log.info(" SpieltagDto:findAll::");
        List< SpieltagDto> matchdays = spieltagService.getAll();
        log.info("SpieltagDto found with {}", matchdays);
        return matchdays;
    }
    @GetMapping("/matchdays/{matchdayId}/matches")
    public List<SpielDto> findAllForMatchday(@PathVariable Long matchdayId) {
        log.info(" SpielDto:findAll::");
        List< SpielDto> matchdays = spielService.getAllForMatchday(matchdayId);
        log.info("SpielDto found with {}", matchdays);
        return matchdays;
    }

    @GetMapping("/matchdays/{id}")
    public SpieltagDto findOne(@PathVariable Long id) {
        log.info("SpieltagDto:findOne::" + id);
        SpieltagDto spieltagDto = spieltagService.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("SpieltagDto found with {}", spieltagDto);
        return spieltagDto;
    }


    @PostMapping("/matchdays")
    @ResponseStatus(HttpStatus.CREATED)
    public SpieltagDto post(@RequestBody @Valid SpieltagDto spieltagDto) {
        log.info("New match day {}", spieltagDto);

        SpieltagDto createdModel = this.spieltagService.save(spieltagDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        log.info("Created match day {}", createdModel);
        return createdModel;
    }

    @PutMapping(value = "/matchdays/{id}")
    public SpieltagDto update(@PathVariable Long id, @RequestBody SpieltagDto spieltagDto) {
        log.info("Update match day {}", spieltagDto);

        SpieltagDto createdModel = this.spieltagService.updateMatchDay(id, spieltagDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated match day {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/matchdays/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            spieltagService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

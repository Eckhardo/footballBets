package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.competition.CompRoundService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.List;

@RestController
public class CompRoundController {


    private static final Logger log = LoggerFactory.getLogger(CompRoundController.class);
    private final CompRoundService roundService;
    private final SpieltagService spieltagService;

    public CompRoundController(CompRoundService roundService, SpieltagService spieltagService) {
        this.roundService = roundService;
        this.spieltagService = spieltagService;
    }

    @GetMapping("/rounds/{id}")
    public CompetitionRoundDto findOne(@PathVariable Long id) {
        log.info("CompetitionRoundDto:findOne::" + id);
        CompetitionRoundDto compDto = roundService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("CompetitionRoundDto found with {}", compDto);
        return compDto;
    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionRoundDto post(@RequestBody @Valid CompetitionRoundDto roundDto) {
        log.info("New round {}", roundDto);

        CompetitionRoundDto createdModel = this.roundService.save(roundDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        log.info("Created round {}", createdModel);
        return createdModel;
    }

    @GetMapping("/rounds/{roundId}/matchdays")
    public List<SpieltagDto> findAll(@PathVariable Long roundId) {
        log.info("SpieltagDto:findAll::" + roundId);
        List<SpieltagDto> spieltagDto = spieltagService.getAllForRound(roundId);
        log.info("SpieltagDto found with {}", spieltagDto);
        return spieltagDto;
    }

    @GetMapping("/rounds")
    public List<CompetitionRoundDto> findAll() {
        log.info(" CompetitionRoundDto:findAll::");
        List<CompetitionRoundDto> compRounds = roundService.getAll();
        log.info("CompetitionRoundDto found with {}", compRounds);
        return compRounds;
    }

    @PutMapping(value = "/rounds/{id}")
    public CompetitionRoundDto update(@PathVariable Long id, @RequestBody CompetitionRoundDto roundDto) {
        log.info("Update round {}", roundDto);

        CompetitionRoundDto createdModel = this.roundService.updateRound(id, roundDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated round {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/rounds/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            roundService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

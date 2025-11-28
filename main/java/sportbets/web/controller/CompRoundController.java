package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.CompRoundService;
import sportbets.web.dto.CompetitionRoundDto;

@RestController
public class CompRoundController {


    private static final Logger log = LoggerFactory.getLogger(CompRoundController.class);
    private final CompRoundService roundService;

    public CompRoundController(CompRoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/rounds/{id}")
    public CompetitionRoundDto findOne(@PathVariable Long id) {
        log.info("CompetitionRoundDto:findOne::" + id);
        CompetitionRoundDto compDto = roundService.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("CompetitionRoundDto found with {}", compDto);
        return compDto;
    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionRoundDto post(@RequestBody @Valid CompetitionRoundDto roundDto) {
        log.info("New round {}", roundDto);

        CompetitionRoundDto createdModel = this.roundService.save(roundDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));;
        log.info("Created round {}", createdModel);
        return createdModel;
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

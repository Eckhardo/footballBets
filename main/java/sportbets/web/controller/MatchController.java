package sportbets.web.controller;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.competition.SpielService;
import sportbets.web.dto.competition.SpielDto;

@RestController
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final SpielService spielService;

    public MatchController(SpielService spielService) {
        this.spielService = spielService;
    }

    @GetMapping("/matches/{id}")
    public SpielDto findOne(@PathVariable Long id) {
        log.info("SpielDto:findOne::" + id);
        SpielDto dto = spielService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("SpielDto found with {}", dto);
        return dto;
    }


    @PostMapping("/matches")
    @ResponseStatus(HttpStatus.CREATED)
    public SpielDto post(@RequestBody @Valid SpielDto dto) {
        log.info("New match day {}", dto);

        SpielDto createdModel = this.spielService.save(dto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        log.info("Created match day {}", createdModel);
        return createdModel;
    }

    @PutMapping(value = "/matches/{id}")
    public SpielDto update(@PathVariable Long id, @RequestBody SpielDto dto) {
        log.info("Update match day {}", dto);

        SpielDto createdModel = this.spielService.updateSpiel(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated match day {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/matches/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            spielService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

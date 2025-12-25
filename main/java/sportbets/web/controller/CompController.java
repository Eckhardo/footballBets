package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.competition.CompService;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;

@RestController
public class CompController {

    private static final Logger log = LoggerFactory.getLogger(CompController.class);
    private final CompService compService;


    public CompController(CompService compService) {
        this.compService = compService;

    }
    @GetMapping("/competitions")
    public List<CompetitionDto> findAll() {

        return compService.getAll();
    }

    @GetMapping("/competitions/{id}")
    public CompetitionDto findOne(@PathVariable Long id) {
        log.info("CompController:findOne::" + id);
        CompetitionDto compDto = compService.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("CompetitionDto found with {}", compDto);
        return compDto;
    }
    @GetMapping("/competitions/{id}/teams")
    public List<TeamDto> findAllTeams(@PathVariable Long id) {

        List<TeamDto> teamDtos = compService.findTeamsForComp(id);
        log.info("TeamDtos found with {}", teamDtos);
        return teamDtos;
    }

    @GetMapping("/competitions/{id}/rounds")
    public List<CompetitionRoundDto> findAllRounds(@PathVariable Long id) {
        log.info(" CompetitionRoundDto:findAll for comp::");
        List< CompetitionRoundDto> compRounds = compService.getAllFormComp(id);
        log.info("CompetitionRoundDto found with {}", compRounds);
        return compRounds;
    }
    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionDto post(@RequestBody @Valid CompetitionDto newComp) {
        log.info("New competition {}", newComp);

        CompetitionDto createdModel = this.compService.save(newComp).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));;
        log.info("Created competition {}", createdModel);
        return createdModel;
    }

    @PutMapping(value = "/competitions/{id}")
    public CompetitionDto update(@PathVariable Long id, @RequestBody CompetitionDto compDto) {

        CompetitionDto createdModel = this.compService.updateComp(id, compDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated competition {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/competitions/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("CompController.delete::" + id);
        try {
            compService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

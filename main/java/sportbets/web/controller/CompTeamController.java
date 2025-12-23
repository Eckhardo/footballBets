package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.CompTeamService;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.List;

@RestController
class CompTeamController {

    private static final Logger log = LoggerFactory.getLogger(CompTeamController.class);
    private final CompTeamService compTeamService;

    public CompTeamController(CompTeamService compTeamService) {
        this.compTeamService = compTeamService;
    }

    @GetMapping("/compTeam/{id}")
    public CompetitionTeamDto findOne(@PathVariable Long id) {

        CompetitionTeamDto dto = compTeamService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("CompetitionTeamDto found with {}", dto);
        return dto;
    }

    @GetMapping("/compTeams/{compId}")
    public List<CompetitionTeamDto> findAllFormComp(@PathVariable Long compId) {

        List<CompetitionTeamDto> dtos = compTeamService.getAllFormComp(compId);

        log.info("# of CompetitionTeamDtos found with {}", dtos.size());
        return dtos;
    }

    @PostMapping("/compTeam")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionTeamDto post(@RequestBody @Valid CompetitionTeamDto dto) {
        log.info("New compTeam  day {}", dto);

        CompetitionTeamDto createdModel = compTeamService.save(dto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ;
        log.info("Created comp team {}", createdModel);
        return createdModel;
    }

    @PostMapping("/compTeams")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CompetitionTeamDto> post(@RequestBody @Valid List<CompetitionTeamDto> dtos) {
        log.info("New dtos{}", dtos.size());

        List<CompetitionTeamDto> createdDtos = compTeamService.saveAll(dtos);
        ;
        log.info("Created compTeam dtos size: {}", createdDtos.size());
        return createdDtos;
    }

    @PutMapping(value = "/compTeam/{id}")
    public CompetitionTeamDto update(@PathVariable Long id, @RequestBody CompetitionTeamDto dto) {
        log.info("UpdatecompTeam {}", dto);

        CompetitionTeamDto createdModel = compTeamService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated compTeam {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/compTeam/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            compTeamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/compTeam/{ids}")
    public ResponseEntity<HttpStatus> delete(@PathVariable List<Long> ids) {
        try {
            for (Long id : ids) {
                compTeamService.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

package sportbets.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.competition.TeamService;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;

@RestController
public class TeamController {
    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;


    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams/{id}")
    public TeamDto findOne(@PathVariable Long id) {
        log.info("TeamDto:findOne::" + id);
        TeamDto teamDto = teamService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("TeamDto found with {}", teamDto);
        return teamDto;
    }

    @GetMapping("/teams")
    public List<TeamDto> findAllTeams() {

        List<TeamDto> teamDtos = teamService.getAll();
        log.info("TeamDtos found with {}", teamDtos);
        return teamDtos;
    }

    @PostMapping("/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto post(@RequestBody TeamDto teamDto) {
        log.info("New team {}", teamDto);

        TeamDto createdModel = this.teamService.save(teamDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        log.info("Created team {}", createdModel);
        return createdModel;
    }

    @PutMapping(value = "/teams/{id}")
    public TeamDto update(@PathVariable Long id, @RequestBody TeamDto teamDto) {
        log.info("Update team  {}", teamDto);

        TeamDto createdModel = this.teamService.updateTeam(id, teamDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated team  {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/teams/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            teamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Team;
import sportbets.service.competition.TeamService;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
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
        log.debug("TeamDto:findOne::{}", id);
        TeamDto teamDto = teamService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return teamDto;
    }

    @GetMapping("/teams")
    public List<TeamDto> findAllTeams() {

     return teamService.getAll();

    }

    @GetMapping("/teams/clubs")
    public List<TeamDto> findAllClubTeams() {

       return teamService.getAllClubTeams();

    }
    @GetMapping("/teams/nations")
    public List<TeamDto> findAllNationTeams() {

       return teamService.getAllNationTeams();

    }
    @PostMapping("/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto post(@RequestBody TeamDto teamDto) {
        log.info("New team {}", teamDto);

      return teamService.save(teamDto);

    }

    @PutMapping(value = "/teams/{id}")
    public TeamDto update(@PathVariable Long id, @RequestBody  @Valid TeamDto teamDto) {
        log.info("Update teamDTO  {}", teamDto);

       return teamService.updateTeam(id, teamDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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

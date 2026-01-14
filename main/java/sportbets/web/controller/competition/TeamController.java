package sportbets.web.controller.competition;

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
    private final ModelMapper modelMapper;


    public TeamController(TeamService teamService, ModelMapper modelMapper) {
        this.teamService = teamService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/teams/{id}")
    public TeamDto findOne(@PathVariable Long id) {
        log.debug("TeamDto:findOne::{}", id);
        Team model = teamService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return modelMapper.map(model, TeamDto.class);
    }

    @GetMapping("/teams")
    public List<TeamDto> findAllTeams() {

        List<Team> teams = teamService.getAll();
        List<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(fam -> {
            teamDtos.add(modelMapper.map(fam, TeamDto.class));
        });
        return teamDtos;
    }

    @GetMapping("/teams/clubs")
    public List<TeamDto> findAllClubTeams() {

        List<Team> teams = teamService.getAllClubTeams();
        List<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(fam -> {
            teamDtos.add(modelMapper.map(fam, TeamDto.class));
        });
        return teamDtos;
    }
    @GetMapping("/teams/nations")
    public List<TeamDto> findAllNationTeams() {

        List<Team> teams = teamService.getAllNationTeams();
        List<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(fam -> {
            teamDtos.add(modelMapper.map(fam, TeamDto.class));
        });
        return teamDtos;
    }
    @PostMapping("/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto post(@RequestBody TeamDto teamDto) {
        log.info("New team {}", teamDto);
        Team model =new Team(teamDto.getName(),teamDto.getAcronym(),teamDto.isClub());
        Team createdModel = teamService.save(model);
        TeamDto saved = modelMapper.map(createdModel, TeamDto.class);
        log.debug("return save::{}", saved);
        return saved;
    }

    @PutMapping(value = "/teams/{id}")
    public TeamDto update(@PathVariable Long id, @RequestBody TeamDto teamDto) {
        log.debug("Update team  {}", teamDto);

        Team model = modelMapper.map(teamDto, Team.class);
        Team updatedModel = teamService.updateTeam(id, model).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        TeamDto updated = modelMapper.map(updatedModel, TeamDto.class);
        log.debug("return save::{}", updated);
        return updated;
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

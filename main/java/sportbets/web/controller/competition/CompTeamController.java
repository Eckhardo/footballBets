package sportbets.web.controller.competition;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.service.competition.CompService;
import sportbets.service.competition.CompTeamService;
import sportbets.service.competition.TeamService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
class CompTeamController {

    private static final Logger log = LoggerFactory.getLogger(CompTeamController.class);
    private final CompService compService;
    private final CompTeamService compTeamService;
    private final TeamService teamService;

    public CompTeamController(CompService compService, CompTeamService compTeamService, TeamService teamService) {
        this.compService = compService;
        this.compTeamService = compTeamService;
        this.teamService = teamService;
    }

    @GetMapping("/compTeam/{id}")
    public CompetitionTeamDto findOne(@PathVariable Long id) {

        CompetitionTeam model = compTeamService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompTeam();
        log.debug("CompetitionRound found with {}", model);
        return modelMapper.map(model, CompetitionTeamDto.class);

    }

    @GetMapping("/compTeams/{compId}")
    public List<CompetitionTeamDto> findAllFormComp(@PathVariable Long compId) {

        List<CompetitionTeam> models = compTeamService.getAllForComp(compId);


        List<CompetitionTeamDto> competitionTeamDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        models.forEach(comp -> {
            competitionTeamDtos.add(myMapper.map(comp, CompetitionTeamDto.class));
        });
        return competitionTeamDtos;
    }

    @GetMapping("/compTeams/{compId}/teams")

    public List<List<CompetitionTeamDto>> findAllRegisteredAndNonRegistered(@PathVariable Long compId) {
        log.info("findAllRegisteredAndNonRegistered for competition id {}", compId);
        List<List<CompetitionTeamDto>> results = new ArrayList<>();
        Competition comp = compService.findById(compId).orElseThrow(() -> new EntityNotFoundException("Competition with id " + compId + " not found"));
        List<CompetitionTeam> models = compTeamService.getAllForComp(compId);
        List<CompetitionTeamDto> registered = new ArrayList<>();
        if (!models.isEmpty()) {
            ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
            models.forEach(ct -> {
                registered.add(myMapper.map(ct, CompetitionTeamDto.class));
            });
        }
        boolean hasClubs = comp.getCompetitionFamily().isHasClubs();
        final List<Team> unregisteredTeams = compTeamService.findUnregisteredTeams(hasClubs, models);
        final List<CompetitionTeamDto> unregistered = new ArrayList<>();
        for (Team team : unregisteredTeams) {
            CompetitionTeamDto competitionTeamDto = new CompetitionTeamDto(null, comp.getId(), comp.getName(), team.getId(), team.getAcronym(), team.isHasClub());
            unregistered.add(competitionTeamDto);
        }

        results.add(unregistered);
        results.add(registered);
        log.info("unregistered size {}", unregistered.size());
        log.info("registered size {}", registered.size());
        return results;
    }


    @PostMapping("/compTeam")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionTeamDto post(@RequestBody @Valid CompetitionTeamDto dto) {
        log.debug("post compTeam {}", dto);


        CompetitionTeam createdModel = compTeamService.save(dto);
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        CompetitionTeamDto createdDto = myMapper.map(createdModel, CompetitionTeamDto.class);
        log.debug("Created comp team {}", createdDto);
        return createdDto;
    }

    @PostMapping("/compTeams")
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody @Valid List<List<CompetitionTeamDto>> dtos) {
        log.info("save or delete compTeams {}", dtos.size());
        if (dtos.isEmpty()) {
            return;
        }
        List<CompetitionTeamDto> unregisteredCompTeams = dtos.get(0);
        List<CompetitionTeamDto> registeredCompTeams = dtos.get(1);

        log.info(" add compTeams {}", registeredCompTeams.size());
        List<Long> addedTeamIds = new ArrayList<>();
        for (CompetitionTeamDto toBeAdded : registeredCompTeams) {
            Optional<CompetitionTeam> candidate = compTeamService.findByTeamIdAndCompId(toBeAdded.getTeamId(), toBeAdded.getCompId());
            if (candidate.isPresent()){
                continue;
            }
            compTeamService.save(toBeAdded);
            addedTeamIds.add(toBeAdded.getTeamId());
        }
        log.info(" delete compTeams {}", unregisteredCompTeams.size());
        for (CompetitionTeamDto tobeDeleted : unregisteredCompTeams) {
            Optional<CompetitionTeam> candidate = compTeamService.findByTeamIdAndCompId(tobeDeleted.getTeamId(), tobeDeleted.getCompId());
            if (candidate.isEmpty() || addedTeamIds.contains(tobeDeleted.getTeamId())) {
                continue;
            }
            compTeamService.deleteById(tobeDeleted.getId());
        }

    }

    @PutMapping(value = "/compTeam/{id}")
    public CompetitionTeamDto update(@PathVariable Long id, @RequestBody CompetitionTeamDto dto) {
        log.debug("UpdatecompTeam {}", dto);
        CompetitionTeam updatedModel = compTeamService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug("Updated compTeam {}", updatedModel);
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        return myMapper.map(updatedModel, CompetitionTeamDto.class);

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

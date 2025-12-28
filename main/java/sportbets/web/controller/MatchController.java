package sportbets.web.controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.Team;
import sportbets.service.competition.SpielService;
import sportbets.service.competition.SpieltagService;
import sportbets.service.competition.TeamService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.SpielDto;

@RestController
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final SpielService spielService;
    private final SpieltagService spieltagService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public MatchController(SpielService spielService, SpieltagService spieltagService, TeamService teamService, ModelMapper modelMapper) {
        this.spielService = spielService;
        this.spieltagService = spieltagService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/matches/{id}")
    public SpielDto findOne(@PathVariable Long id) {
        log.info("SpielDto:findOne::{}", id);

        Spiel model = spielService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ModelMapper modelMapper = MapperUtil.getModelMapperForSpiel();
        log.info("Spiel found with {}", model);
        return modelMapper.map(model, SpielDto.class);

    }


    @PostMapping("/matches")
    @ResponseStatus(HttpStatus.CREATED)
    public SpielDto post(@RequestBody @Valid SpielDto spielDto) {
        log.info("New match day {}", spielDto);
        Spieltag spieltag = spieltagService.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamService.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamService.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        Spiel createdModel = spielService.save(model);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto updatedDto = myModelMapper.map(createdModel, SpielDto.class);
        log.info("Spiel RETURN do {}", updatedDto);
        return updatedDto;
    }

    @PutMapping(value = "/matches/{id}")
    public SpielDto update(@PathVariable Long id, @RequestBody SpielDto spielDto) {
        log.info("Update match day {}", spielDto);
        Spieltag spieltag = spieltagService.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamService.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamService.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        Spiel updatedModel = spielService.updateSpiel(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto updatedDto = myModelMapper.map(updatedModel, SpielDto.class);
        log.info("Spiel RETURN do {}", updatedDto);
        return updatedDto;
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

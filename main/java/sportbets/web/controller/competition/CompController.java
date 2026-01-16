package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.service.competition.CompService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompController {

    private static final Logger log = LoggerFactory.getLogger(CompController.class);
    private final CompService compService;
    private final SpieltagService spieltagService;
    public CompController(CompService compService, SpieltagService spieltagService) {
        this.compService = compService;
        this.spieltagService = spieltagService;
    }

    @GetMapping("/competitions")
    public List<CompetitionDto> findAll() {

        List<Competition> competitions = compService.getAll();
        List<CompetitionDto> competitionDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        competitions.forEach(comp -> {
            competitionDtos.add(myMapper.map(comp, CompetitionDto.class));
        });
        return competitionDtos;
    }

    @GetMapping("/competitions/{id}")
    public CompetitionDto findOne(@PathVariable Long id) {
        log.debug("CompController:findOne::{}", id);
        Competition model = compService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForFamily();
        log.debug("Competition found with {}", model);
        return modelMapper.map(model, CompetitionDto.class);

    }

    @GetMapping("/competitions/{id}/teams")
    public List<TeamDto> findAllTeams(@PathVariable Long id) {

        List<TeamDto> teamDtos = compService.findTeamsForComp(id);
        log.debug("TeamDtos found with {}", teamDtos);
        return teamDtos;
    }

    @GetMapping("/competitions/{compId}/matchdays")
    public List<SpieltagDto> findAllForCompetition(@PathVariable Long compId) {
        log.info("SpieltagDto:findAll::{}", compId);
        List<Spieltag> spieltags = spieltagService.getAllForCompetition(compId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltags.forEach(comp -> {
            spieltagDtos.add(myMapper.map(comp, SpieltagDto.class));
        });
        return spieltagDtos;
    }

    @GetMapping("/competitions/{familyId}/competitions")
    public List<CompetitionDto> findAllCompsByFamId(@PathVariable Long familyId) {
        log.info("CompetitionDto:findAllCompsByFamId::{}", familyId);
        List<Competition> comps = compService.findByFamilyId(familyId);
        List<CompetitionDto> compDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        comps.forEach(comp -> {
            compDtos.add(myMapper.map(comp, CompetitionDto.class));
        });
        return compDtos;
    }


    @GetMapping("/competitions/{id}/rounds")
    public List<CompetitionRoundDto> findAllRounds(@PathVariable Long id) {
        log.debug(" CompetitionRoundDto:findAll for comp::");
        List<CompetitionRound> compRounds = compService.getAllFormComp(id);
        List<CompetitionRoundDto> roundDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetition();
        compRounds.forEach(comp -> {
            roundDtos.add(myMapper.map(comp, CompetitionRoundDto.class));
        });
        return roundDtos;
    }

    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionDto post(@RequestBody @Valid CompetitionDto newComp) {
        log.debug("New competition {}", newComp);

        Competition createdModel = compService.save(newComp);

        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();
        CompetitionDto createdDto = myModelMapper.map(createdModel, CompetitionDto.class);
        log.debug("Competition RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/competitions/{id}")
    public CompetitionDto update(@PathVariable Long id, @RequestBody CompetitionDto compDto) {

        Competition updatedComp = this.compService.updateComp(id, compDto).orElseThrow();

        log.debug("Updated competition {}", updatedComp);

        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();
        CompetitionDto updatedDto = myModelMapper.map(updatedComp, CompetitionDto.class);
        log.debug("Competition RETURN do {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/competitions/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.debug("CompController.delete::{}", id);
        try {
            compService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

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
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
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
        log.info("CompController:findOne::{}", id);
        Competition model = compService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForFamily();
        log.info("Competition found with {}", model);
        return modelMapper.map(model, CompetitionDto.class);

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
        log.info("New competition {}", newComp);

        Competition createdModel = compService.save(newComp);

        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();
        CompetitionDto createdDto = myModelMapper.map(createdModel, CompetitionDto.class);
        log.info("Competition RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/competitions/{id}")
    public CompetitionDto update(@PathVariable Long id, @RequestBody CompetitionDto compDto) {

        Competition updatedComp = this.compService.updateComp(id, compDto);

        log.info("Updated competition {}", updatedComp);

        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();
        CompetitionDto updatedDto = myModelMapper.map(updatedComp, CompetitionDto.class);
        log.info("Competition RETURN do {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/competitions/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("CompController.delete::{}", id);
        try {
            compService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

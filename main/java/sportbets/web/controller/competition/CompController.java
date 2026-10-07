package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.service.competition.CompService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/competitions")
public class CompController {

    private static final Logger log = LoggerFactory.getLogger(CompController.class);
    private final CompService compService;
    private final SpieltagService spieltagService;
    private final ModelMapper myMapper;

    public CompController(CompService compService, SpieltagService spieltagService) {
        this.compService = compService;
        this.spieltagService = spieltagService;
        this.myMapper = MapperUtil.getModelMapperForFamily();

    }

    @GetMapping
    public List<CompetitionDto> findAll() {
        List<Competition> competitions = compService.getAll();
        return convertToDtos(competitions);
    }

    @NonNull
    private List<CompetitionDto> convertToDtos(List<Competition> competitions) {
        List<CompetitionDto> competitionDtos = new ArrayList<>();
        competitions.forEach(comp -> competitionDtos.add(myMapper.map(comp, CompetitionDto.class)));
        return competitionDtos;
    }

    @GetMapping("/{id}")
    public CompetitionDto findOne(@PathVariable Long id) {
        Competition model = compService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
          return myMapper.map(model, CompetitionDto.class);

    }

    @GetMapping("/{id}/teams")
    public List<TeamDto> findAllTeams(@PathVariable Long id) {
        return compService.findTeamsForComp(id);
    }

    @GetMapping("/{compId}/matchdays")
    public List<SpieltagDto> findAllForCompetition(@PathVariable Long compId) {
        log.info("SpieltagDto:findAll::{}", compId);
        List<Spieltag> matchdays = spieltagService.getAllForCompetition(compId);
        return getSpieltagDtos(matchdays);
    }


    @GetMapping("/{familyId}/competitions")
    public List<CompetitionDto> findAllCompsByFamId(@PathVariable Long familyId) {
        log.info("CompetitionDto:findAllCompsByFamId::{}", familyId);
        List<Competition> comps = compService.findByFamilyId(familyId);
        return convertToDtos(comps);
    }


    @GetMapping("/{id}/rounds")
    public List<CompetitionRoundDto> findAllRounds(@PathVariable Long id) {
        log.debug(" CompetitionRoundDto:findAll for comp:{}:",id);
        List<CompetitionRound> compRounds = compService.getAllFormComp(id);
        List<CompetitionRoundDto> roundDtos = new ArrayList<>();
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        compRounds.forEach(comp -> roundDtos.add(modelMapper.map(comp, CompetitionRoundDto.class)));
        for(CompetitionRoundDto roundDto : roundDtos) {
            log.debug("Round found with {}", roundDto);
        }
        return roundDtos;
    }

    @PostMapping(produces = "application/vnd.kirschning.new-comp+json", consumes = "application/vnd.kirschning.new-comp+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OldCompetitionDto post(@RequestBody @Valid OldCompetitionDto oldCompetitionDto) {
        log.error("Example for API Structrual Change without using versioning: content-negotiation mechanism  {}", oldCompetitionDto);
        return oldCompetitionDto;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionDto postNewStructure(@RequestBody @Valid CompetitionDto newComp) {
        log.debug("New competition with new media type {}", newComp);
        Competition createdModel = compService.save(newComp);
        return myMapper.map(createdModel, CompetitionDto.class);
    }

    @PutMapping(value = "/{id}")
    public CompetitionDto update(@PathVariable Long id, @RequestBody CompetitionDto compDto) {
        Competition updatedComp = this.compService.updateComp(id, compDto).orElseThrow();
        return myMapper.map(updatedComp, CompetitionDto.class);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        compService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static List<SpieltagDto> getSpieltagDtos(List<Spieltag> matchdays) {
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetitionRound();
        matchdays.forEach(matchday -> spieltagDtos.add(modelMapper.map(matchday, SpieltagDto.class)));
        return spieltagDtos;
    }
}

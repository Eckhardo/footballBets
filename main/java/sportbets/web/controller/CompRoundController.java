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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.service.competition.CompRoundService;
import sportbets.service.competition.CompService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompRoundController {


    private static final Logger log = LoggerFactory.getLogger(CompRoundController.class);
    private final CompService compService;
    private final CompRoundService roundService;
    private final SpieltagService spieltagService;
    private final ModelMapper modelMapper;

    public CompRoundController(CompService compService, CompRoundService roundService, SpieltagService spieltagService, ModelMapper modelMapper) {
        this.compService = compService;
        this.roundService = roundService;
        this.spieltagService = spieltagService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/rounds/{id}")
    public CompetitionRoundDto findOne(@PathVariable Long id) {
        CompetitionRound model = roundService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Competition comp = compService.findById(model.getCompetition().getId()).orElseThrow(() -> new EntityNotFoundException("comp not found "));
        model.setCompetition(comp);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        log.info("CompetitionRound found with {}", model);
        return modelMapper.map(model, CompetitionRoundDto.class);

    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionRoundDto post(@RequestBody @Valid CompetitionRoundDto roundDto) {
        log.info("post: {}", roundDto);
        Competition comp = compService.findByName(roundDto.getCompName()).orElseThrow(() -> new EntityNotFoundException("comp not found "));

        CompetitionRound model = modelMapper.map(roundDto, CompetitionRound.class);
        model.setCompetition(comp);

        CompetitionRound createdModel = roundService.save(model);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto createdDto = myModelMapper.map(createdModel, CompetitionRoundDto.class);
        log.info("CompetitionRound RETURN do {}", createdDto);
        return createdDto;
    }

    @GetMapping("/rounds/{roundId}/matchdays")
    public List<SpieltagDto> findAll(@PathVariable Long roundId) {
        log.info("SpieltagDto:findAll::" + roundId);
        List<Spieltag> spieltags = spieltagService.getAllForRound(roundId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltags.forEach(comp -> {
            spieltagDtos.add(myMapper.map(comp, SpieltagDto.class));
        });
        return spieltagDtos;
    }

    @GetMapping("/rounds")
    public List<CompetitionRoundDto> findAll() {
        log.info(" CompetitionRoundDto:findAll::");
        List<CompetitionRound> compRounds = roundService.getAll();
        List<CompetitionRoundDto> roundDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetition();
        compRounds.forEach(comp -> {
            roundDtos.add(myMapper.map(comp, CompetitionRoundDto.class));
        });
        return roundDtos;
    }

    @PutMapping(value = "/rounds/{id}")
    public CompetitionRoundDto update(@PathVariable Long id, @RequestBody CompetitionRoundDto roundDto) {
        log.info("Update round {}", roundDto);
        Competition comp = compService.findById(roundDto.getCompId()).orElseThrow(() -> new EntityNotFoundException("comp not found "));

        CompetitionRound model = modelMapper.map(roundDto, CompetitionRound.class);
        model.setCompetition(comp);

        CompetitionRound updatedModel = this.roundService.updateRound(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto updatedDto = myModelMapper.map(updatedModel, CompetitionRoundDto.class);
        log.info("Competition RETURN do {}", updatedDto);
        return updatedDto;
    }

    @DeleteMapping(value = "/rounds/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            roundService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

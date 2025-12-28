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
    private final CompRoundService roundService;
    private final SpieltagService spieltagService;

    public CompRoundController( CompRoundService roundService, SpieltagService spieltagService) {
        this.roundService = roundService;
        this.spieltagService = spieltagService;
       }

    @GetMapping("/rounds/{id}")
    public CompetitionRoundDto findOne(@PathVariable Long id) {
        CompetitionRound model = roundService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        log.info("CompetitionRound found with {}", model);
        return modelMapper.map(model, CompetitionRoundDto.class);

    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionRoundDto post(@RequestBody @Valid CompetitionRoundDto roundDto) {
        log.info("post: {}", roundDto);

        CompetitionRound createdModel = roundService.save(roundDto);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto createdDto = myModelMapper.map(createdModel, CompetitionRoundDto.class);
        log.info("CompetitionRound RETURN do {}", createdDto);
        return createdDto;
    }

    @GetMapping("/rounds/{roundId}/matchdays")
    public List<SpieltagDto> findAllForComp(@PathVariable Long roundId) {
        log.info("SpieltagDto:findAll::{}", roundId);
        List<Spieltag> spieltags = spieltagService.getAllForRound(roundId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltags.forEach(comp -> {
            spieltagDtos.add(myMapper.map(comp, SpieltagDto.class));
        });
        return spieltagDtos;
    }


    @PutMapping(value = "/rounds/{id}")
    public CompetitionRoundDto update(@PathVariable Long id, @RequestBody CompetitionRoundDto roundDto) {
        log.info("Update round {}", roundDto);

        CompetitionRound updatedModel = this.roundService.updateRound(id, roundDto)
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

package sportbets.web.controller.competition;

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
import sportbets.service.competition.SpielService;
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.SpielDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpieltagController {

    private static final Logger log = LoggerFactory.getLogger(SpieltagController.class);
    private final SpieltagService spieltagService;
    private final SpielService spielService;


    public SpieltagController(SpieltagService spieltagService, SpielService spielService) {
        this.spieltagService = spieltagService;
        this.spielService = spielService;

    }

    @GetMapping("/matchdays")
    public List<SpieltagDto> findAll() {
        log.debug(" SpieltagDto:findAll::");

        List<Spieltag> matchdays = spieltagService.getAll();


        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        matchdays.forEach(comp -> {

            spieltagDtos.add(myMapper.map(comp, SpieltagDto.class));
        });
        return spieltagDtos;
    }

    @GetMapping("/matchdays/{matchdayId}/matches")
    public List<SpielDto> findAllForMatchday(@PathVariable Long matchdayId) {
        log.debug(" SpielDto:findAll::");
        List<Spiel> matches = spielService.getAllForMatchday(matchdayId);


        List<SpielDto> spielDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForSpiel();
        matches.forEach(match -> {

            spielDtos.add(myMapper.map(match, SpielDto.class));
        });
        return spielDtos;
    }

    @GetMapping("/matchdays/{id}")
    public SpieltagDto findOne(@PathVariable Long id) {
        log.debug("SpieltagDto:findOne::{}", id);
        Spieltag model = spieltagService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetitionRound();
        log.debug("CompetitionRound found with {}", model);
        return modelMapper.map(model, SpieltagDto.class);

    }


    @PostMapping("/matchdays")
    @ResponseStatus(HttpStatus.CREATED)
    public SpieltagDto post(@RequestBody @Valid SpieltagDto spieltagDto) {
        log.debug("New match day {}", spieltagDto);


        Spieltag createdModel = spieltagService.save(spieltagDto);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetitionRound();
        SpieltagDto createdDto = myModelMapper.map(createdModel, SpieltagDto.class);
        log.debug("SpieltagDto RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/matchdays/{id}")
    public SpieltagDto update(@PathVariable Long id, @RequestBody SpieltagDto spieltagDto) {
        log.debug("Update match day {}", spieltagDto);
        Spieltag updatedModel = spieltagService.updateMatchDay(id, spieltagDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetitionRound();
        SpieltagDto updatedDto = myModelMapper.map(updatedModel, SpieltagDto.class);
        log.debug("Competition RETURN do {}", updatedDto);
        return updatedDto;
    }

    @DeleteMapping(value = "/matchdays/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            spieltagService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

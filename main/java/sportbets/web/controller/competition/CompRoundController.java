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
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.service.competition.CompRoundService;
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
    private final ModelMapper myMapper;
    public CompRoundController(CompRoundService roundService, SpieltagService spieltagService) {
        this.roundService = roundService;
        this.spieltagService = spieltagService;
        this.myMapper = MapperUtil.getModelMapperForCompetition();
    }

    @GetMapping("/rounds/{id}")
    public CompetitionRoundDto findOne(@PathVariable Long id) {
        CompetitionRound model = roundService.findById(id).orElseThrow(() -> new EntityNotFoundException("competition round with id " + id + " not found"));
        log.debug("CompetitionRound found with {}", model);
        return myMapper.map(model, CompetitionRoundDto.class);

    }

    @GetMapping("rounds/search")
    public CompetitionRoundDto searchProducts(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "compId") Long compId) {
        log.info("CompetitionRound findByNameAndCompId:: {} ", compId);
        CompetitionRound model = roundService.findByNameAndCompId(name, compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("CompetitionRound foundByName:: {} ", model);
         return myMapper.map(model, CompetitionRoundDto.class);

    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionRoundDto post(@RequestBody @Valid CompetitionRoundDto roundDto) {
        log.info("post: {}", roundDto);

        CompetitionRound createdModel = roundService.save(roundDto);

        CompetitionRoundDto createdDto = myMapper.map(createdModel, CompetitionRoundDto.class);
        log.info("CompetitionRound RETURN do {}", createdDto);
        return createdDto;
    }


    @PutMapping(value = "/rounds/{id}")
    public CompetitionRoundDto update(@PathVariable Long id, @RequestBody CompetitionRoundDto roundDto) {
        log.info("Update round {}", roundDto);

        CompetitionRound updatedModel = this.roundService.updateRound(id, roundDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
         CompetitionRoundDto updatedDto = myMapper.map(updatedModel, CompetitionRoundDto.class);
        log.info("round updated RETURN do {}", updatedDto);
        return updatedDto;
    }

    @DeleteMapping(value = "/rounds/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

            roundService.deleteById(id);
            return ResponseEntity.noContent().build();

    }

    @GetMapping("/rounds/{roundId}/matchdays")
    public List<SpieltagDto> findAllForRound(@PathVariable Long roundId) {
        log.info("SpieltagDto:findAllForRound::{}", roundId);
        List<Spieltag> spieltags = spieltagService.getAllForRound(roundId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltags.forEach(comp -> {
            spieltagDtos.add(myMapper.map(comp, SpieltagDto.class));
        });
        return spieltagDtos;
    }

}

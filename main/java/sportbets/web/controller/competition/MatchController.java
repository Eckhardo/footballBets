package sportbets.web.controller.competition;


import jakarta.persistence.EntityExistsException;
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
import sportbets.web.dto.competition.batch.MatchBatchRecord;

import java.util.Optional;

@RestController
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final SpielService spielService;
    private final SpieltagService spieltagService;

    public MatchController(SpielService spielService, SpieltagService spieltagService) {
        this.spielService = spielService;
        this.spieltagService = spieltagService;
        ;
    }

    @GetMapping("/matches/{id}")
    public SpielDto findOne(@PathVariable Long id) {
        log.debug("SpielDto:findOne::{}", id);

        Spiel model = spielService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ModelMapper modelMapper = MapperUtil.getModelMapperForSpiel();
        log.debug("Spiel found with {}", model);
        return modelMapper.map(model, SpielDto.class);

    }

    @PostMapping("/matches/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public void postBatch(@RequestBody @Valid MatchBatchRecord matchBatchRecord) {
        log.info("New matches  {}", matchBatchRecord);



        spielService.saveAll(matchBatchRecord);

    }


    @PostMapping("/matches")
    @ResponseStatus(HttpStatus.CREATED)
    public SpielDto post(@RequestBody @Valid SpielDto spielDto) {
        log.debug("New match day {}", spielDto);

        Spiel createdModel = spielService.save(spielDto);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto updatedDto = myModelMapper.map(createdModel, SpielDto.class);
        log.debug("Spiel RETURN do {}", updatedDto);
        return updatedDto;
    }

    @PutMapping(value = "/matches/{id}")
    public SpielDto update(@PathVariable Long id, @RequestBody SpielDto spielDto) {
        log.debug("Update match day {}", spielDto);


        Spiel updatedModel = spielService.updateSpiel(id, spielDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto updatedDto = myModelMapper.map(updatedModel, SpielDto.class);
        log.debug("Spiel RETURN do {}", updatedDto);
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

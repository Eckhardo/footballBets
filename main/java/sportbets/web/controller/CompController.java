package sportbets.web.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.Competition;
import sportbets.service.CompService;
import sportbets.web.dto.CompDtoOLD;
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionFamilyDto;
import sportbets.web.dto.MapperUtil;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@RestController
public class CompController {

    private static final Logger log = LoggerFactory.getLogger(CompController.class);
    private CompService compService;


    public CompController(CompService compService) {
        this.compService = compService;

    }
    @GetMapping("/competitions")
    public List<CompetitionDto> findAll() {

        return compService.getAll();
    }

    @GetMapping("/competitions/{id}")
    public CompetitionDto findOne(@PathVariable Long id) {
        log.info("CompController:findOne::" + id);
        CompetitionDto compDto = compService.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("CompetitionDto found with {}", compDto);
        return compDto;
    }

    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionDto post(@RequestBody @Valid CompetitionDto newComp) {
        log.info("New competition {}", newComp);

        CompetitionDto createdModel = this.compService.save(newComp).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));;
        log.info("Created competition {}", createdModel);
        return createdModel;
    }

    @PutMapping(value = "/competitions/{id}")
    public CompetitionDto update(@PathVariable Long id, @RequestBody CompetitionDto compDto) {

        CompetitionDto createdModel = this.compService.updateComp(id, compDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated competition {}", createdModel);
        return createdModel;
    }

    @DeleteMapping(value = "/competitions/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("CompController.delete::" + id);
        try {
            compService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

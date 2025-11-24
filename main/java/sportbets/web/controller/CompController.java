package sportbets.web.controller;

import jakarta.validation.Valid;
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

@RestController
public class CompController {

    private static final Logger log = LoggerFactory.getLogger(CompController.class);
    private CompService compService;

    public CompController(CompService compService) {
        this.compService = compService;
    }

    @GetMapping("/competitions/{id}")
    public CompDtoOLD findOne(@PathVariable Long id) {

        Competition model = compService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        CompDtoOLD dto = CompDtoOLD.Mapper.toDto(model);
        log.info("Competition found with {}", model);
        return dto;
    }

    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompDtoOLD post(@RequestBody @Valid CompDtoOLD newComp) {
        log.info("New competition {}", newComp);
        Competition model = CompDtoOLD.Mapper.toModel(newComp);
        Competition createdModel = this.compService.save(model);
        log.info("Created competition {}", createdModel);
        return CompDtoOLD.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/competitions/{id}")
    public CompDtoOLD update(@PathVariable Long id, @RequestBody @Validated(CompDtoOLD.CompUpdateValidationData.class) CompDtoOLD compDto) {
        Competition model = CompDtoOLD.Mapper.toModel(compDto);
        Competition createdModel = this.compService.updateComp(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Updated competition {}", createdModel);
        return CompDtoOLD.Mapper.toDto(createdModel);
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

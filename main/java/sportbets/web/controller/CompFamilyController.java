package sportbets.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.service.CompFamilyService;
import sportbets.web.dto.CompFamilyDtoOLD;

@RestController
public class CompFamilyController {

    private static final Logger log = LoggerFactory.getLogger(CompFamilyController.class);
    private CompFamilyService compFamilyService;

    public CompFamilyController(CompFamilyService compFamilyService) {
        this.compFamilyService = compFamilyService;
    }

    @GetMapping("/families/{id}")
    public CompFamilyDtoOLD findOne(@PathVariable Long id) {

        CompetitionFamily model = compFamilyService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CompFamilyDtoOLD.Mapper.toDto(model);
    }

    @PostMapping("/families")
    @ResponseStatus(HttpStatus.CREATED)
    public CompFamilyDtoOLD post(@RequestBody @Valid CompFamilyDtoOLD newFam) {
      log.info("CompFamilyController.create::" +newFam.toString());
        CompetitionFamily model = CompFamilyDtoOLD.Mapper.toModel(newFam,null);
        log.info("CompFamilyController.post::" +model.toString());
        CompetitionFamily createdModel = this.compFamilyService.save(model);
        return CompFamilyDtoOLD.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/families/{id}")
    public CompFamilyDtoOLD update(@PathVariable Long id, @RequestBody @Validated(CompFamilyDtoOLD.CompFamilyUpdateValidationData.class) CompFamilyDtoOLD familyDto) {
       // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.info("CompFamilyController.update::" +familyDto.toString());
        CompetitionFamily model = CompFamilyDtoOLD.Mapper.toModel(familyDto,id);
        CompetitionFamily createdModel = this.compFamilyService.updateFamily(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CompFamilyDtoOLD.Mapper.toDto(createdModel);
    }

    @DeleteMapping(value = "/families/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.info("CompFamilyController.delete::" + id);
        try {
            compFamilyService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

package sportbets.web.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.CompFamilyService;
import sportbets.web.dto.CompetitionFamilyDto;


@RestController
public class CompFamilyController {

    private static final Logger log = LoggerFactory.getLogger(CompFamilyController.class);
    private final CompFamilyService compFamilyService;

    public CompFamilyController(CompFamilyService compFamilyService) {
        this.compFamilyService = compFamilyService;
    }

    @GetMapping("/families/{id}")
    public CompetitionFamilyDto findOne(@PathVariable Long id) {

        CompetitionFamilyDto famDto = compFamilyService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("return::" + famDto.toString());
        return famDto;
    }

    @PostMapping("/families")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionFamilyDto post(@RequestBody @Valid CompetitionFamilyDto newFam) {
        log.info("CompFamilyController.create::" + newFam.toString());

        CompetitionFamilyDto famDto = this.compFamilyService.save(newFam).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        log.info("return save::" + famDto);
        return famDto;
    }

    @PutMapping(value = "/families/{id}")
    public CompetitionFamilyDto update(@PathVariable Long id, @RequestBody @Valid CompetitionFamilyDto familyDto) {
        // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.info("CompFamilyController.update::" + familyDto.toString());

        CompetitionFamilyDto createdDto = this.compFamilyService.updateFamily(id, familyDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("return update::" + createdDto);
        return createdDto;
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

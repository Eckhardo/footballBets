package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.service.competition.CompFamilyService;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CompFamilyController {

    private static final Logger log = LoggerFactory.getLogger(CompFamilyController.class);
    private final CompFamilyService compFamilyService;
    private final ModelMapper modelMapper;

    public CompFamilyController(CompFamilyService compFamilyService, ModelMapper modelMapper) {
        this.compFamilyService = compFamilyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/families/{id}")
    public CompetitionFamilyDto findOne(@PathVariable Long id) {

        CompetitionFamily model = compFamilyService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return modelMapper.map(model, CompetitionFamilyDto.class);

    }

    @GetMapping("/families/search")
    public CompetitionFamilyDto findByName(@RequestParam String name) {

        CompetitionFamily model = compFamilyService.findByByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return modelMapper.map(model, CompetitionFamilyDto.class);

    }
    @PostMapping("/families")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionFamilyDto post(@RequestBody @Valid CompetitionFamilyDto newFam) {
        log.debug("CompFamilyController.create::{}", newFam);
        CompetitionFamily createdModel = compFamilyService.save(newFam).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        log.debug("return save::{}", famDto);
        return famDto;
    }

    @PutMapping(value = "/families/{id}")
    public CompetitionFamilyDto update(@PathVariable Long id, @RequestBody @Valid CompetitionFamilyDto familyDto) {
        // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.debug("CompFamilyController.update::{}", familyDto.toString());
        CompetitionFamily updatedModel = compFamilyService.updateFamily(id, familyDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CompetitionFamilyDto famDto = modelMapper.map(updatedModel, CompetitionFamilyDto.class);
        log.debug("return save::{}", famDto);
        return famDto;
    }

    @DeleteMapping(value = "/families/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.debug("CompFamilyController.delete::{}", id);
        try {
            compFamilyService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/families")
    public List<CompetitionFamilyDto> findAll() {
        List<CompetitionFamily> fams = compFamilyService.getAll();
        List<CompetitionFamilyDto> famDtos = new ArrayList<>();
        fams.forEach(fam -> {
            famDtos.add(modelMapper.map(fam, CompetitionFamilyDto.class));
        });
        return famDtos;
    }

    @GetMapping("/families/{id}/families")
    public CompetitionFamilyDto findByCompId(@PathVariable Long id) {

        CompetitionFamily model = compFamilyService.findByByCompId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return modelMapper.map(model, CompetitionFamilyDto.class);

    }
}

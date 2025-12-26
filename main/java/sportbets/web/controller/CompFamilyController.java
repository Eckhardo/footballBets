package sportbets.web.controller;

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
import java.util.Optional;


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

    @PostMapping("/families")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionFamilyDto post(@RequestBody @Valid CompetitionFamilyDto newFam) {
        log.info("CompFamilyController.create::" + newFam.toString());
        CompetitionFamily model = modelMapper.map(newFam, CompetitionFamily.class);
        CompetitionFamily createdModel = compFamilyService.save(model).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        log.info("return save::" + famDto);
        return famDto;
    }

    @PutMapping(value = "/families/{id}")
    public CompetitionFamilyDto update(@PathVariable Long id, @RequestBody @Valid CompetitionFamilyDto familyDto) {
        // CompetitionFamily model = compFamilyService.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
        log.info("CompFamilyController.update::" + familyDto.toString());
        CompetitionFamily model = modelMapper.map(familyDto, CompetitionFamily.class);
        CompetitionFamily updatedModel = compFamilyService.updateFamily(id,model).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CompetitionFamilyDto famDto = modelMapper.map(updatedModel, CompetitionFamilyDto.class);
        log.info("return save::" + famDto);
        return famDto;
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

    @GetMapping("/families")
    public List<CompetitionFamilyDto> findAll() {
        List<CompetitionFamily> fams = compFamilyService.getAll();
        List<CompetitionFamilyDto> famDtos = new ArrayList<>();
        fams.forEach(fam -> {
            famDtos.add(modelMapper.map(fam, CompetitionFamilyDto.class));
        });
        return famDtos;
    }

}

package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.service.competition.CompTeamService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.ArrayList;
import java.util.List;

@RestController
class CompTeamController {

    private static final Logger log = LoggerFactory.getLogger(CompTeamController.class);
    private final CompTeamService compTeamService;

    public CompTeamController(CompTeamService compTeamService) {
        this.compTeamService = compTeamService;

    }

    @GetMapping("/compTeam/{id}")
    public CompetitionTeamDto findOne(@PathVariable Long id) {

        CompetitionTeam model = compTeamService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompTeam();
        log.debug("CompetitionRound found with {}", model);
        return modelMapper.map(model, CompetitionTeamDto.class);

    }

    @GetMapping("/compTeams/{compId}")
    public List<CompetitionTeamDto> findAllFormComp(@PathVariable Long compId) {

        List<CompetitionTeam> models = compTeamService.getAllFormComp(compId);


        List<CompetitionTeamDto> competitionTeamDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        models.forEach(comp -> {
            competitionTeamDtos.add(myMapper.map(comp, CompetitionTeamDto.class));
        });
        return competitionTeamDtos;
    }

    @PostMapping("/compTeam")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionTeamDto post(@RequestBody @Valid CompetitionTeamDto dto) {
        log.debug("post compTeam {}", dto);


        CompetitionTeam createdModel = compTeamService.save(dto);
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        CompetitionTeamDto createdDto = myMapper.map(createdModel, CompetitionTeamDto.class);
        log.debug("Created comp team {}", createdDto);
        return createdDto;
    }

    @PostMapping("/compTeams")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CompetitionTeamDto> post(@RequestBody @Valid List<CompetitionTeamDto> dtos) {
        log.debug("New dtos{}", dtos.size());
        if (dtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition teams must not be empty");
        }

        List<CompetitionTeamDto> createdDtos = new ArrayList<>();
        for (CompetitionTeamDto dto : dtos) {

            CompetitionTeam createdModel = compTeamService.save(dto);
            ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
            CompetitionTeamDto createdDto = myMapper.map(createdModel, CompetitionTeamDto.class);

            createdDtos.add(createdDto);

        }
        log.debug("Created compTeam dtos size: {}", createdDtos.size());
        return createdDtos;
    }

    @PutMapping(value = "/compTeam/{id}")
    public CompetitionTeamDto update(@PathVariable Long id, @RequestBody CompetitionTeamDto dto) {
        log.debug("UpdatecompTeam {}", dto);


        CompetitionTeam updatedModel = compTeamService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug("Updated compTeam {}", updatedModel);
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        return myMapper.map(updatedModel, CompetitionTeamDto.class);

    }

    @DeleteMapping(value = "/compTeam/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            compTeamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/compTeam/{ids}")
    public ResponseEntity<HttpStatus> delete(@PathVariable List<Long> ids) {
        try {
            for (Long id : ids) {
                compTeamService.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

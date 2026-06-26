package sportbets.web.controller.competition;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.service.competition.CompetitionMembershipService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionMembershipDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompetitionMembershipController {

    private static final Logger log = LoggerFactory.getLogger(CompetitionMembershipController.class);


    private final CompetitionMembershipService compMembService;
    private final ModelMapper myMapper;
    public CompetitionMembershipController(CompetitionMembershipService compMembService) {
        this.compMembService = compMembService;
        this.myMapper = MapperUtil.getModelMapperForCompetitionMembership();
    }


    @GetMapping("/compMembs")
    public List<CompetitionMembershipDto> findAll() {
        log.debug(":findAll");
        List<CompetitionMembership> compMembs = compMembService.getAll();
        List<CompetitionMembershipDto> commMembDtos = new ArrayList<>();
        compMembs.forEach(comp -> {
            commMembDtos.add(myMapper.map(comp, CompetitionMembershipDto.class));
        });
        return commMembDtos;
    }

    @GetMapping("/compMembs/{id}")
    public CompetitionMembershipDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        CompetitionMembership model = compMembService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug("compMemb found with {}", model);
        return myMapper.map(model, CompetitionMembershipDto.class);

    }

    @PostMapping("/compMembs")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionMembershipDto post(@RequestBody @Valid CompetitionMembershipDto newCommMemb) {
        log.debug("New compMemb {}", newCommMemb);
        CompetitionMembership createdModel = compMembService.save(newCommMemb);
        CompetitionMembershipDto createdDto = myMapper.map(createdModel, CompetitionMembershipDto.class);
        log.debug("compMemb RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/compMembs/{id}")
    public CompetitionMembershipDto update(@PathVariable Long id, @RequestBody CompetitionMembershipDto membershipDto) {

        CompetitionMembership updatedComm = this.compMembService.update(id, membershipDto).orElseThrow();
        log.debug("Updated compMemb entity {}", updatedComm);
        CompetitionMembershipDto updatedDto = myMapper.map(updatedComm, CompetitionMembershipDto.class);
        log.debug("CompMembDto RETURN  {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/compMembs/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.debug("CompetitionMembershipController.delete::{}", id);
        try {
            compMembService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

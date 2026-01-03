package sportbets.web.controller.community;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.service.community.CommunityMembershipService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.community.CommunityMembershipDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommunityMembershipController {


    private static final Logger log = LoggerFactory.getLogger(CommunityMembershipController.class);
    private final CommunityMembershipService commMembService;


    public CommunityMembershipController(CommunityMembershipService commMembService) {
        this.commMembService = commMembService;

    }


    @GetMapping("/commMembs")
    public List<CommunityMembershipDto> findAll() {
        log.info(":findAll");
        List<CommunityMembership> commMembs = commMembService.getAll();
        List<CommunityMembershipDto> commMembDtos = new ArrayList<>();
        ModelMapper modelMapper = MapperUtil.getModelMapperForCommunityMembership();
        commMembs.forEach(comp -> {
            commMembDtos.add(modelMapper.map(comp, CommunityMembershipDto.class));
        });
        return commMembDtos;
    }

    @GetMapping("/commMembs/{id}")
    public CommunityMembershipDto findOne(@PathVariable Long id) {
        log.info(":findOne::{}", id);
        CommunityMembership model = commMembService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelMapper modelMapper = MapperUtil.getModelMapperForCommunityMembership();
        log.info("Community found with {}", model);
        return modelMapper.map(model, CommunityMembershipDto.class);

    }

    @PostMapping("/commMembs")
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityMembershipDto post(@RequestBody @Valid CommunityMembershipDto newCommMemb) {
        log.info("New commMemb {}", newCommMemb);
        CommunityMembership createdModel = commMembService.save(newCommMemb);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCommunityMembership();
        CommunityMembershipDto createdDto = modelMapper.map(createdModel, CommunityMembershipDto.class);
        log.info("commMemb RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/commMembs/{id}")
    public CommunityMembershipDto update(@PathVariable Long id, @RequestBody CommunityMembershipDto membershipDto) {

        CommunityMembership updatedComm = this.commMembService.update(id, membershipDto).orElseThrow();
        log.info("Updated commMemb entity {}", updatedComm);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCommunityMembership();
        CommunityMembershipDto updatedDto = modelMapper.map(updatedComm, CommunityMembershipDto.class);
        log.info("CommMembDto RETURN  {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/commMembs/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("CommunityMembershipController.delete::{}", id);
        try {
            commMembService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

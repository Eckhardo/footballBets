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
import sportbets.persistence.entity.community.Tipper;
import sportbets.service.community.CommunityMembershipService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.community.TipperDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commMembs")
public class CommunityMembershipController {


    private static final Logger log = LoggerFactory.getLogger(CommunityMembershipController.class);
    private final CommunityMembershipService commMembService;
    ModelMapper myModelMapper = MapperUtil.getModelMapperForCommunityMembership();


    public CommunityMembershipController(CommunityMembershipService commMembService) {
        this.commMembService = commMembService;
    }


    @GetMapping()
    public List<CommunityMembershipDto> findAll() {
        log.debug(":findAll");
        List<CommunityMembership> commMembs = commMembService.getAll();
        List<CommunityMembershipDto> commMembDtos = new ArrayList<>();
        commMembs.forEach(comp -> {
            commMembDtos.add(myModelMapper.map(comp, CommunityMembershipDto.class));
        });
        return commMembDtos;
    }

    @GetMapping("/{id}")
    public CommunityMembershipDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        CommunityMembership model = commMembService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug("Community found with {}", model);
        return myModelMapper.map(model, CommunityMembershipDto.class);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityMembershipDto post(@RequestBody @Valid CommunityMembershipDto newCommMemb) {
        log.debug("New commMemb {}", newCommMemb);
        CommunityMembership createdModel = commMembService.save(newCommMemb);
        CommunityMembershipDto createdDto = myModelMapper.map(createdModel, CommunityMembershipDto.class);
        log.debug("commMemb RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/{id}")
    public CommunityMembershipDto update(@PathVariable Long id, @RequestBody CommunityMembershipDto membershipDto) {

        CommunityMembership updatedComm = this.commMembService.update(id, membershipDto).orElseThrow();
        log.debug("Updated commMemb entity {}", updatedComm);
        CommunityMembershipDto updatedDto = myModelMapper.map(updatedComm, CommunityMembershipDto.class);
        log.debug("CommMembDto RETURN  {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("CommunityMembershipController.delete::{}", id);
        commMembService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{commId}/tipper")
    public List<TipperDto> findTippers(@PathVariable Long commId) {
        log.debug(":find tippers");
        List<Tipper> tippers = commMembService.findTippers(commId);
        List<TipperDto> tipperDtos = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        tippers.forEach(tipper -> {
            tipperDtos.add(modelMapper.map(tipper, TipperDto.class));
        });
        return tipperDtos;
    }

    @GetMapping("/{username}/communities")
    public List<CommunityDto> findTipperCommunities(@PathVariable String username) {
        log.debug(":find communities for tipper {}", username);
        return commMembService.findCommunities(username);

    }

}

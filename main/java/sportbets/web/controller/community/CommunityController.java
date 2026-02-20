package sportbets.web.controller.community;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.community.Community;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommunityController {

    private static final Logger log = LoggerFactory.getLogger(CommunityController.class);
    private final CommunityService communityService;
    private final ModelMapper modelMapper;

    public CommunityController(CommunityService communityService, ModelMapper modelMapper) {
        this.communityService = communityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/communities")
    public List<CommunityDto> findAll() {
        log.debug(":findAll");
        List<Community> communities = communityService.getAll();
        List<CommunityDto> communityDtos = new ArrayList<>();

        communities.forEach(comp -> {
            communityDtos.add(modelMapper.map(comp, CommunityDto.class));
        });
        return communityDtos;
    }

    @GetMapping("/communities/{id}")
    public CommunityDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        Community model = communityService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.debug("Community found with {}", model);
        return modelMapper.map(model, CommunityDto.class);

    }

    @PostMapping("/communities")
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityDto post(@RequestBody @Valid CommunityDto newComm) {
        log.debug("New community {}", newComm);
        Community createdModel = communityService.save(newComm);
        CommunityDto createdDto = modelMapper.map(createdModel, CommunityDto.class);
        log.debug("Community RETURN do {}", createdDto);
        return createdDto;
    }

    @PutMapping(value = "/communities/{id}")
    public CommunityDto update(@PathVariable Long id, @RequestBody CommunityDto commDto) {

        Community updatedComm = this.communityService.update(id, commDto).orElseThrow();
        log.debug("Updated community {}", updatedComm);
        CommunityDto updatedDto = modelMapper.map(updatedComm, CommunityDto.class);
        log.debug("Community RETURN do {}", updatedDto);
        return updatedDto;


    }

    @DeleteMapping(value = "/communities/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.debug("CommunityController.delete::{}", id);
        try {
            communityService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

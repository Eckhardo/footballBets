package sportbets.web.controller.community;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.service.community.TipperService;
import sportbets.service.competition.CompService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.ArrayList;
import java.util.List;

@RestController
class TipperController {

    private static final Logger log = LoggerFactory.getLogger(TipperController.class);
    private final TipperService tipperService;
    private final ModelMapper modelMapper;
    TipperController(TipperService tipperService, ModelMapper modelMapper) {
        this.tipperService = tipperService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/tipper/{id}")
    public TipperDto findOne(@PathVariable Long id) {
        log.info("TipperController:findOne::{}", id);
        Tipper model = tipperService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("Tipper found with {}", model.getUsername());
        return modelMapper.map(model, TipperDto.class);

    }
    @PostMapping("/tipper")
    @ResponseStatus(HttpStatus.CREATED)
    public TipperDto post(@RequestBody @Valid TipperDto newTipper) {
        log.info("TipperController.create::{}", newTipper);
        Tipper createdModel = tipperService.save(newTipper).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        TipperDto tipperDto = modelMapper.map(createdModel, TipperDto.class);
        log.info("return save::{}", tipperDto);
        return tipperDto;
    }


    @PutMapping(value = "/tipper/{id}")
    public TipperDto update(@PathVariable Long id, @RequestBody @Valid TipperDto tipperDto) {
        log.info("CompFamilyController.update::{}", tipperDto.toString());
        Tipper updatedModel = tipperService.update(id, tipperDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        TipperDto dto = modelMapper.map(updatedModel, TipperDto.class);
        log.info("return save::{}", dto);
        return dto;
    }


    @DeleteMapping(value = "/tipper/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("CompFamilyController.delete::{}", id);
        try {
            tipperService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/tippers")
    public List<TipperDto> findAll() {
        List<Tipper> tippers = tipperService.getAll();
        List<TipperDto> tipperDtos = new ArrayList<>();
        tippers.forEach(fam -> {
            tipperDtos.add(modelMapper.map(fam, TipperDto.class));
        });
        return tipperDtos;
    }
}

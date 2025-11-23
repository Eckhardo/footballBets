package sportbets.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.Competition;
import sportbets.service.CompService;
import sportbets.web.dto.CompDto;

@RestController
public class CompController {

    private CompService compService;

    public CompController(CompService compService) {
        this.compService = compService;
    }

    @GetMapping("/competitions/{id}")
    public CompDto findOne(@PathVariable Long id) {

        Competition model = compService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CompDto.Mapper.toDto(model);
    }

    @PostMapping("/competitions/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompDto post(@RequestBody @Valid CompDto newComp) {
        Competition model = CompDto.Mapper.toModel(newComp);
        Competition createdModel = this.compService.save(model);
        return CompDto.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/competitions/{id}")
    public CompDto update(@PathVariable Long id, @RequestBody @Validated(CompDto.CompUpdateValidationData.class) CompDto compDto) {
        Competition model = CompDto.Mapper.toModel(compDto);
        Competition createdModel = this.compService.updateComp(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CompDto.Mapper.toDto(createdModel);
    }
}

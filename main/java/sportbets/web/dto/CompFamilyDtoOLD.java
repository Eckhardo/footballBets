package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record CompFamilyDtoOLD(

        Long id,

        @NotBlank(groups = {CompFamilyUpdateValidationData.class, Default.class},
                message = "name can't be blank")
        String name,

        @Size(groups = {CompFamilyUpdateValidationData.class, Default.class},
                min = 10, max = 50,
                message = "description must be between 10 and 50 characters long")
        String description,
        boolean hasLigaModus,
        boolean hasClubs,
        Set<CompDtoOLD> competitions
) {
    private static final Logger log = LoggerFactory.getLogger(CompFamilyDtoOLD.class); // @formatter:on

    public static class Mapper {
        public static CompetitionFamily toModel(CompFamilyDtoOLD dto, Long id) {

            if (dto == null)
                return null;
            log.info("Mapper.toModel::" + dto);
            CompetitionFamily model = new CompetitionFamily(dto.name(), dto.description(), dto.hasLigaModus(), dto.hasClubs());
            if (dto.competitions() != null) {
                log.info("Mapper.toModel.competitions::" + dto.competitions());
                dto.competitions.forEach(comp ->{
                   Competition compEntity= new Competition(comp.name(), comp.description(), comp.winMultiplicator(), comp.remisMultiplicator(), model);
                   model.addCompetition(compEntity);
                }) ;
            }
                if (!Objects.isNull(dto.id())) {
                model.setId(id);
            }
            log.info("Mapper.toModel return::" + model);
            return model;
        }

        public static CompFamilyDtoOLD toDto(CompetitionFamily model) {
            if (model == null)
                return null;
            Set<CompDtoOLD> competitions = model.getCompetitions()
                    .stream()
                    .map(CompDtoOLD.Mapper::toDto)
                    .collect(Collectors.toSet());

            CompFamilyDtoOLD dto = new CompFamilyDtoOLD(model.getId(), model.getName(), model.getDescription(), model.isHasLigaModus(), model.isHasClubs(), competitions);
           log.info("RETURN:: " +dto);
            return dto;
        }
    }

    public interface CompFamilyUpdateValidationData {
    }
}


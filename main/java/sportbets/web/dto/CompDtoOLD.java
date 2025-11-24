package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;

import java.util.Objects;

public record CompDtoOLD(

        @NotNull(groups = {CompDtoOLD.CompUpdateValidationData.class},
                message = "Competition id can't be null")
        Long id,

        @NotBlank(groups = {CompDtoOLD.CompUpdateValidationData.class, Default.class},
                message = "name can't be blank")
        String name,
        @NotNull(groups = { CompUpdateValidationData.class, Default.class },
                message = "familyId can't be null")
        Long compFamilyId,

        @Size(groups = {CompDtoOLD.CompUpdateValidationData.class, Default.class},
                min = 10, max = 50,
                message = "description must be between 10 and 50 characters long")
        String description,
        int winMultiplicator,
        int remisMultiplicator
        ) {
    private static final Logger log = LoggerFactory.getLogger(CompDtoOLD.class); // @formatter:on

    public static class Mapper {
        public static Competition toModel(CompDtoOLD dto) {
            if (dto == null)
                return null;
            log.info("Mapping competitionDTO to competition");
            CompetitionFamily family = new CompetitionFamily();
            family.setId(dto.compFamilyId());

            Competition model = new Competition(dto.name(), dto.description(), dto.winMultiplicator(), dto.remisMultiplicator(), family);
            if (!Objects.isNull(dto.id())) {
                model.setId(dto.id());
            }

            return model;
        }

        public static CompDtoOLD toDto(Competition model) {
            if (model == null)
                return null;
            CompDtoOLD dto = new CompDtoOLD(model.getId(), model.getName(), model.getCompetitionFamily().getId(), model.getDescription(), model.getWinMultiplicator(), model.getRemisMultiplicator());
            return dto;
        }
    }

    public interface CompUpdateValidationData {
    }


}

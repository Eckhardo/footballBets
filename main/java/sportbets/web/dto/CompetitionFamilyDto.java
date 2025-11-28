package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.CompetitionFamily;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link CompetitionFamily}
 */
public class CompetitionFamilyDto implements Serializable {
    private Long id;

    @NotBlank(message = "name can't be blank")
    private String name;

    @Size(min = 10, max = 50,
            message = "description must be between 10 and 50 characters long")
    private String description;
    private boolean hasLigaModus;
    private boolean hasClubs;
    private Set<CompetitionDto> competitions = new HashSet<>();

    public CompetitionFamilyDto() {
    }

    public CompetitionFamilyDto(Long id, String name, String description, boolean hasLigaModus, boolean hasClubs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hasLigaModus = hasLigaModus;
        this.hasClubs = hasClubs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean getHasLigaModus() {
        return hasLigaModus;
    }

    public void setHasLigaModus(boolean hasLigaModus) {
        this.hasLigaModus = hasLigaModus;
    }

    public boolean getHasClubs() {
        return hasClubs;
    }

    public void setHasClubs(boolean hasClubs) {
        this.hasClubs = hasClubs;
    }

    public boolean isHasLigaModus() {
        return hasLigaModus;
    }

    public boolean isHasClubs() {
        return hasClubs;
    }

    public Set<CompetitionDto> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(Set<CompetitionDto> comps) {
        this.competitions = comps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionFamilyDto entity = (CompetitionFamilyDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.hasLigaModus, entity.hasLigaModus) &&
                Objects.equals(this.hasClubs, entity.hasClubs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, hasLigaModus, hasClubs);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "competition names = " + competitions + ", " +
                "hasLigaModus = " + hasLigaModus + ", " +
                "hasClubs = " + hasClubs + ")";
    }

    public static class Mapper {

        private static final Logger log = LoggerFactory.getLogger(Mapper.class);

        public static CompetitionFamily toModel(CompetitionFamilyDto dto) {

            if (dto == null)
                return null;
            log.info("Mapper.toModel::" + dto);
            CompetitionFamily model = new CompetitionFamily(dto.getName(), dto.getDescription(), dto.getHasLigaModus(), dto.getHasClubs());


            log.info("Mapper.toModel return::" + model);
            return model;
        }

        public static CompetitionFamilyDto toDto(CompetitionFamily model) {
            if (model == null)
                return null;

            CompetitionFamilyDto dto = new CompetitionFamilyDto(model.getId(), model.getName(), model.getDescription(), model.isHasLigaModus(), model.isHasClubs());
            log.info("RETURN:: " + dto);
            return dto;
        }
    }
}
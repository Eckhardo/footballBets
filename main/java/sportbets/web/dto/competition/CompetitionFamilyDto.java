package sportbets.web.dto.competition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sportbets.common.Country;
import sportbets.persistence.entity.competition.CompetitionFamily;

import java.io.Serializable;
import java.util.Objects;

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
    private Country country;

    public CompetitionFamilyDto() {
    }

    public CompetitionFamilyDto(Long id, String name, String description, boolean hasLigaModus, boolean hasClubs, Country country) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hasLigaModus = hasLigaModus;
        this.hasClubs = hasClubs;
        this.country = country;
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

    public boolean getHasClubs() {
        return hasClubs;
    }

    public boolean isHasLigaModus() {
        return hasLigaModus;
    }

    public void setHasLigaModus(boolean hasLigaModus) {
        this.hasLigaModus = hasLigaModus;
    }

    public boolean isHasClubs() {
        return hasClubs;
    }

    public void setHasClubs(boolean hasClubs) {
        this.hasClubs = hasClubs;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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
                "hasLigaModus = " + hasLigaModus + ", " +
                "country = " + country + ", " +
                "hasClubs = " + hasClubs + ")";
    }

}
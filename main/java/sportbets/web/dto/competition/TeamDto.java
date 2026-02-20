package sportbets.web.dto.competition;

import jakarta.validation.constraints.NotBlank;
import sportbets.persistence.entity.competition.Team;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Team}
 */
public class TeamDto implements Serializable {
    private Long id;
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "acronym cannot be blank")
    private String acronym;
    private boolean hasClub;


    public TeamDto() {
    }

    public TeamDto(Long id, String name, String acronym,  boolean isClub) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.hasClub = isClub;
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public boolean isHasClub() {
        return hasClub;
    }

    public void setHasClub(boolean hasClub) {
        this.hasClub = hasClub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamDto entity = (TeamDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.acronym, entity.acronym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "isClub = " + hasClub + ", " +
                "acronym = " + acronym + ")";
    }


}
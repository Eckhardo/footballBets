package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.Team}
 */
public class TeamDto implements Serializable {
    private Long id;
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "acronym cannot be blank")
    private String acronym;


    public TeamDto() {
    }

    public TeamDto(Long id, String name, String acronym) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
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
                "acronym = " + acronym + ")";
    }
}
package sportbets.web.dto.authorization;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.authorization.CompetitionRole;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link CompetitionRole}
 */
public class CompetitionRoleDto implements Serializable {
    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Long competitionId;

    @NotNull
    private String competitionName;

    public CompetitionRoleDto() {
    }

    public CompetitionRoleDto(Long id, String name, String description, Long competitionId, String competitionName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.competitionId = competitionId;
        this.competitionName = competitionName;
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

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionRoleDto that = (CompetitionRoleDto) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(competitionId, that.competitionId) && Objects.equals(competitionName, that.competitionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, competitionId, competitionName);
    }

    @Override
    public String toString() {
        return "CompetitionRoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", competitionId=" + competitionId +
                ", competitionName='" + competitionName + '\'' +
                '}';
    }
}
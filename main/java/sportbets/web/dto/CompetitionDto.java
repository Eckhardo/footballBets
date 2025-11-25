package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.Competition}
 */
public class CompetitionDto implements Serializable {
    private Long id;

    @NotBlank(groups = {CompDtoOLD.CompUpdateValidationData.class, Default.class},
            message = "name can't be blank")
    private  String name;

    @Size(groups = {CompDtoOLD.CompUpdateValidationData.class, Default.class},
            min = 10, max = 50,
            message = "description must be between 10 and 50 characters long")
    private  String description;
    private  int winMultiplicator;
    private  int remisMultiplicator;
    private  Date createdOn;
    private CompetitionFamilyDto competitionFamily;


    public CompetitionDto() {
    }

    public CompetitionDto(Long id, String name, String description, int winMultiplicator, int remisMultiplicator, Date createdOn,CompetitionFamilyDto competitionFamily) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.winMultiplicator = winMultiplicator;
        this.remisMultiplicator = remisMultiplicator;
        this.createdOn = createdOn;
        this.competitionFamily = competitionFamily;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWinMultiplicator() {
        return winMultiplicator;
    }

    public int getRemisMultiplicator() {
        return remisMultiplicator;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWinMultiplicator(int winMultiplicator) {
        this.winMultiplicator = winMultiplicator;
    }

    public void setRemisMultiplicator(int remisMultiplicator) {
        this.remisMultiplicator = remisMultiplicator;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public CompetitionFamilyDto getCompetitionFamily() {
        return competitionFamily;
    }

    public void setCompetitionFamily(CompetitionFamilyDto competitionFamily) {
        this.competitionFamily = competitionFamily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionDto entity = (CompetitionDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.winMultiplicator, entity.winMultiplicator) &&
                Objects.equals(this.remisMultiplicator, entity.remisMultiplicator) &&
                Objects.equals(this.createdOn, entity.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, winMultiplicator, remisMultiplicator, createdOn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "family = " + competitionFamily + ", " +
                "winMultiplicator = " + winMultiplicator + ", " +
                "remisMultiplicator = " + remisMultiplicator + ", " +
                "createdOn = " + createdOn + ")";
    }
}
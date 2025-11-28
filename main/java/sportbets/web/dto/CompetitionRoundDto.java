package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.CompetitionRound}
 */
public class CompetitionRoundDto implements Serializable {
    private Long id;
    @NotNull(message = " round number cannot be null")
    private int roundNumber;
    @NotBlank(message = "name must not be empty")
    private String name;
    private boolean hasGroups = false;
    @NotNull(message = " competition id cannot be null")
    private Long compId;

    public CompetitionRoundDto() {
    }

    public CompetitionRoundDto(Long id, int roundNumber, String name, boolean hasGroups) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.name = name;
        this.hasGroups = hasGroups;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getHasGroups() {
        return hasGroups;
    }

    public void setHasGroups(boolean hasGroups) {
        this.hasGroups = hasGroups;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionRoundDto dto = (CompetitionRoundDto) o;
        return Objects.equals(this.id, dto.id) &&
                Objects.equals(this.roundNumber, dto.roundNumber) &&
                Objects.equals(this.name, dto.name) &&
                Objects.equals(this.hasGroups, dto.hasGroups) &&
                Objects.equals(this.compId, dto.compId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roundNumber, name, hasGroups);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "roundNumber = " + roundNumber + ", " +
                "name = " + name + ", " +
                "hasGroups = " + hasGroups + ", " +
                "compId = " + compId +")" ;
    }
}
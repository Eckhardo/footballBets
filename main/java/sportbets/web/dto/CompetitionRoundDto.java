package sportbets.web.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.CompetitionRound}
 */
public class CompetitionRoundDto implements Serializable {
    private Long id;
    private int roundNumber;
    private String name;
    private boolean hasGroups = false;
    @NotNull
    private CompetitionDto competition;
    private Date createdOn = new Date();

    public CompetitionRoundDto() {
    }

    public CompetitionRoundDto(Long id, int roundNumber, String name, boolean hasGroups, CompetitionDto competition, Date createdOn) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.name = name;
        this.hasGroups = hasGroups;
        this.competition = competition;
        this.createdOn = createdOn;
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

    public CompetitionDto getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionDto competition) {
        this.competition = competition;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
                Objects.equals(this.competition, dto.competition) &&
                Objects.equals(this.createdOn, dto.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roundNumber, name, hasGroups, competition, createdOn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "roundNumber = " + roundNumber + ", " +
                "name = " + name + ", " +
                "hasGroups = " + hasGroups + ", " +
                "competition = " + competition + ", " +
                "createdOn = " + createdOn + ")";
    }
}
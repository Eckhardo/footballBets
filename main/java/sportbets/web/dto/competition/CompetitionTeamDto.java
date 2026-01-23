package sportbets.web.dto.competition;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.CompetitionTeam;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link CompetitionTeam}
 */
public class CompetitionTeamDto implements Serializable {
    private Long id;
    @NotNull(message = " competition id cannot be null")
    private Long compId;
    private String compName;
    @NotNull(message = " team id cannot be null")
    private Long teamId;
    private String teamAcronym;
    private Boolean hasClub = true;

    public CompetitionTeamDto() {
    }

    public CompetitionTeamDto(Long id, Long compId, String compName, Long teamId, String teamAcronym, Boolean isClub) {
        this.id = id;
        this.compId = compId;
        this.compName = compName;
        this.teamId = teamId;
        this.teamAcronym = teamAcronym;
        this.hasClub = isClub;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamAcronym() {
        return teamAcronym;
    }

    public void setTeamAcronym(String teamAcronym) {
        this.teamAcronym = teamAcronym;
    }

    public Boolean getHasClub() {
        return hasClub;
    }

    public void setHasClub(Boolean hasClub) {
        this.hasClub = hasClub;
    }

    @Override
    public String toString() {
        return "CompetitionTeamDto{" +
                "id=" + id +
                ", compId=" + compId +
                ", compName='" + compName + '\'' +
                ", teamId=" + teamId +
                ", teamAcronym='" + teamAcronym + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionTeamDto that = (CompetitionTeamDto) o;
        return Objects.equals(id, that.id) && Objects.equals(compId, that.compId) && Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, compId, teamId);
    }
}
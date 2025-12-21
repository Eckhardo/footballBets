package sportbets.web.dto;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.CompetitionTeam;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link CompetitionTeam}
 */
public class CompetitionTeamDto implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(CompetitionTeamDto.class);
    private Long id;
    @NotNull(message = " competition id cannot be null")
    private Long compId;
    private String compName;
    @NotNull(message = " team id cannot be null")
    private Long teamId;
    private String teamAcronym;

    public CompetitionTeamDto() {
    }

    public CompetitionTeamDto(Long id, Long compId, String compName, Long teamId, String teamAcronym) {
        this.id = id;

        this.compId = compId;
        this.compName = compName;
        this.teamId = teamId;
        this.teamAcronym = teamAcronym;
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
package sportbets.web.dto.competition;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.CompetitionMembership;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link CompetitionMembership}
 */
public class CompetitionMembershipDto implements Serializable {
    private Long id;
    @NotNull(message = " competition id cannot be null")
    private Long compId;
    private String compName;
    @NotNull(message = " community id cannot be null")
    private Long commId;
    private String commName;

    public CompetitionMembershipDto() {
    }


    public Long getId() {
        return id;
    }

    public CompetitionMembershipDto(Long compId, String compName, Long commId, String commName) {
        this.compId = compId;
        this.compName = compName;
        this.commId = commId;
        this.commName = commName;
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

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionMembershipDto entity = (CompetitionMembershipDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.commId, entity.commId)
                && Objects.equals(this.compId, entity.compId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commId, compId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "compId = " + compId + ", " +
                "commId = " + commId + ")";
    }
}
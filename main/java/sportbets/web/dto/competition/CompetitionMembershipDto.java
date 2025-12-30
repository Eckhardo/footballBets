package sportbets.web.dto.competition;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.CompetitionMembership;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link CompetitionMembership}
 */
public class CompetitionMembershipDto implements Serializable {
    private Long id;
    private LocalDateTime createdOn;
    @NotNull(message = " competition id cannot be null")
    private Long compId;
    private String compName;
    @NotNull(message = " community id cannot be null")
    private Long commId;
    private String commName;

    public CompetitionMembershipDto() {
    }

    public CompetitionMembershipDto(Long id, LocalDateTime createdOn) {
        this.id = id;
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
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
                Objects.equals(this.createdOn, entity.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdOn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "createdOn = " + createdOn + ")";
    }
}
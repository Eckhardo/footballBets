package sportbets.web.dto.community;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.CommunityMembership;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link CommunityMembership}
 */
public class CommunityMembershipDto implements Serializable {
    private Long id;
    private LocalDateTime createdOn = LocalDateTime.now();
    @NotNull(message = " tipper id cannot be null")
    private Long tipperId;
    private String tipperName;
    @NotNull(message = " community id cannot be null")
    private Long commId;
    private String commName;
    public CommunityMembershipDto() {
    }

    public CommunityMembershipDto(Long id, LocalDateTime createdOn) {
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

    public Long getTipperId() {
        return tipperId;
    }

    public void setTipperId(Long tipperId) {
        this.tipperId = tipperId;
    }

    public String getTipperName() {
        return tipperName;
    }

    public void setTipperName(String tipperName) {
        this.tipperName = tipperName;
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
        CommunityMembershipDto entity = (CommunityMembershipDto) o;
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
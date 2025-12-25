package sportbets.web.dto.authorization;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.authorization.CommunityRole;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link CommunityRole}
 */
public class CommunityRoleDto implements Serializable {
    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Long communityId;

    @NotNull
    private String communityName;
    public CommunityRoleDto() {
    }

    public CommunityRoleDto(Long id, String name, String description, Long communityId, String communityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.communityId = communityId;
        this.communityName = communityName;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommunityRoleDto that = (CommunityRoleDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(communityId, that.communityId) && Objects.equals(communityName, that.communityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, communityId, communityName);
    }

    @Override
    public String toString() {
        return "CommunityRoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", communityId=" + communityId +
                ", communityName='" + communityName + '\'' +
                '}';
    }
}
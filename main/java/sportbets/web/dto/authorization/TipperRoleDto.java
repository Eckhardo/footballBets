package sportbets.web.dto.authorization;

import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.authorization.TipperRole;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link TipperRole}
 */
public class TipperRoleDto implements Serializable {
    @NotNull
    private Long id;

    @NotNull
    private Long tipperId;

    @NotNull
    private String tipperUserName;

    @NotNull
    private Long roleId;

    @NotNull
    private String roleName;

    public TipperRoleDto() {
    }

    public TipperRoleDto(Long id, Long tipperId, String tipperUserName, Long roleId, String roleName) {
        this.id = id;
        this.tipperId = tipperId;
        this.tipperUserName = tipperUserName;
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipperId() {
        return tipperId;
    }

    public void setTipperId(Long tipperId) {
        this.tipperId = tipperId;
    }

    public String getTipperUserName() {
        return tipperUserName;
    }

    public void setTipperUserName(String tipperUserName) {
        this.tipperUserName = tipperUserName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TipperRoleDto that = (TipperRoleDto) o;
        return Objects.equals(id, that.id) && Objects.equals(tipperId, that.tipperId) && Objects.equals(tipperUserName, that.tipperUserName) && Objects.equals(roleId, that.roleId) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipperId, tipperUserName, roleId, roleName);
    }

    @Override
    public String toString() {
        return "TipperRoleDto{" +
                "id=" + id +
                ", tipperId=" + tipperId +
                ", tipperUserName='" + tipperUserName + '\'' +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
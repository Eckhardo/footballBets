package sportbets.persistence.entity.authorization;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.Tipper;

import java.util.Objects;

@Entity

public class TipperRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "rolle_id")
    @NotNull
    private Role role;

    @ManyToOne
    @JoinColumn(name = "tipper_id")
    @NotNull
    private Tipper tipper;

    public TipperRole() {
    }

    public TipperRole(Role role, Tipper tipper) {
        this.role = role;
        this.tipper = tipper;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRolle() {
        return role;
    }

    public void setRolle(Role role) {
        this.role = role;
    }

    public Tipper getTipper() {
        return tipper;
    }

    public void setTipper(Tipper tipper) {
        this.tipper = tipper;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TipperRole that = (TipperRole) o;
        return Objects.equals(id, that.id) && Objects.equals(role, that.role) && Objects.equals(tipper, that.tipper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, tipper);
    }

    @Override
    public String toString() {
        return "TipperRole{" +
                "id=" + id +
                ", role=" + role.getName() +
                ", tipper=" + tipper.getUsername() +
                '}';
    }
}
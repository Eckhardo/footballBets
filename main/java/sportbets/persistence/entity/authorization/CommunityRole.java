package sportbets.persistence.entity.authorization;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.Community;

import java.util.Objects;

@Entity
public class CommunityRole extends Role {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comm_id",foreignKey = @ForeignKey(name = "FK_COMM_ROLE_TO_COMM"))
    @NotNull
    public Community community;

    public CommunityRole() {

    }

    public CommunityRole(String name, String description, Community community) {
        super(name, description);
        this.community = community;
    }


    public Community getCommunity() {
        return this.community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public String toString() {
        return super.toString() + "CommunityRole{" +
                "community=" + community +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommunityRole that = (CommunityRole) o;
        return Objects.equals(community, that.community);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(),getDescription());
    }
}
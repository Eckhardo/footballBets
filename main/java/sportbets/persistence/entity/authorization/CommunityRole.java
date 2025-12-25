package sportbets.persistence.entity.authorization;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Competition;

import java.util.Objects;
import java.util.Optional;

@Entity
public class CommunityRole extends Role {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comm_id")
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
        return Objects.hash(super.hashCode(), community.getName());
    }
}
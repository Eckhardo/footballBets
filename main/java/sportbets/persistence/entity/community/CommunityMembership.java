package sportbets.persistence.entity.community;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CommunityMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comm_id",foreignKey = @ForeignKey(name = "FK_COMM_MEMB_TO_COMMUNITY"))
    @NotNull
    private Community community;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_tipper_id",foreignKey = @ForeignKey(name = "FK_COMM_MEMB_TO_ZIPPRT"))
    @NotNull
    private Tipper tipper;


    public CommunityMembership() {
    }

    public CommunityMembership(Community community, Tipper tipper) {
        this.community = community;
        this.tipper = tipper;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Tipper getTipper() {
        return tipper;
    }

    public void setTipper(Tipper tipper) {
        this.tipper = tipper;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommunityMembership that = (CommunityMembership) o;
        return Objects.equals(id, that.id) && Objects.equals(createdOn, that.createdOn) && Objects.equals(community.getName(), that.community.getName()) && Objects.equals(tipper.getUsername(), that.tipper.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdOn, community.getName(), tipper.getUsername());
    }

    @Override
    public String toString() {
        return "CommunityMembership{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", community=" + community.getName() +
                ", tipper=" + tipper.getUsername() +
                '}';
    }
}

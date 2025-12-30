package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.Community;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CompetitionMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comm_id",foreignKey = @ForeignKey(name = "FK_COMP_MEMB_TO_COMMUNITY"))
    @NotNull
    private Community community;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comp_id",foreignKey = @ForeignKey(name = "FK_COMP_MEMB_TO_COMP"))
    @NotNull
    private Competition competition;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitionMembership() {
    }

    public CompetitionMembership(Community community, Competition competition) {
        this.community = community;
        this.competition = competition;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionMembership that = (CompetitionMembership) o;
        return Objects.equals(id, that.id) && Objects.equals(createdOn, that.createdOn) && Objects.equals(community.getName(), that.community.getName()) && Objects.equals(competition.getName(), that.competition.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdOn, community, competition);
    }

    @Override
    public String toString() {
        return "CompetitionMembership{" +
                "id=" + id +
                ", community=" + community.getName() +
                ", competition=" + competition.getName() +
                '}';
    }
}
package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CompetitionTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime createdOn=LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_team_id")
    @NotNull
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_id")
    @NotNull
    private Competition competition;
     public CompetitionTeam(Team team, Competition competition) {
        this.team = team;
     //   this.team.addCompetitionTeam(this);
        this.competition = competition;
    //    this.competition.addCompetitionTeam(this);
    }

    public CompetitionTeam() {

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
        CompetitionTeam that = (CompetitionTeam) o;
        return id == that.id && Objects.equals(createdOn, that.createdOn) && Objects.equals(team, that.team) && Objects.equals(competition, that.competition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdOn, team, competition);
    }

    @Override
    public String toString() {
        return "CompetitionTeam{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", team=" + team +
                ", competition=" + competition +
                '}';
    }
}

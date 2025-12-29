package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class CompetitionRound {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roundNumber;
    @Column(nullable = false)
    private String name;
    private boolean hasGroups = false;
    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comp_id", foreignKey = @ForeignKey(name = "FK_ROUND_TO_COMP"))
    @NotNull
    private Competition competition;
    @OneToMany(mappedBy = "competitionRound", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<CompetitionGroup> competitionGroups = new HashSet<>();
    @OneToMany(mappedBy = "competitionRound", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Spieltag> spieltage = new HashSet<>();

    public CompetitionRound() {

    }

//-------------- Constructor----------------------

    /**
     * Simple properties constructor.
     */

    public CompetitionRound(int roundNumber, String name, Competition competition, boolean hasGroups) {
        this.roundNumber = roundNumber;
        this.name = name;
        this.competition = competition;
        this.hasGroups = hasGroups;

        // guarantee ref integrity
        //    ;
    }
//-------- Getter & Setter-------------------

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

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Set<CompetitionGroup> getCompetitionGroups() {
        return competitionGroups;
    }

    public void addCompetitionGroup(CompetitionGroup group) {
        if (group == null)
            throw new IllegalArgumentException("Can't add a null group.");
        this.getCompetitionGroups().add(group);
    }

    public Set<Spieltag> getSpieltage() {
        return spieltage;
    }

    public void addSpieltag(Spieltag spieltag) {
        this.getSpieltage().add(spieltag);
    }


    public boolean isHasGroups() {
        return hasGroups;
    }

    public void setHasGroups(boolean hasGroups) {
        this.hasGroups = hasGroups;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionRound that = (CompetitionRound) o;
        return Objects.equals(id, that.id) && hasGroups == that.hasGroups && roundNumber == that.roundNumber && Objects.equals(name, that.name) && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hasGroups, createdOn, roundNumber);
    }

    @Override
    public String toString() {
        return "CompetitionRound{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hasGroups=" + hasGroups +
                ", competition=" + competition.getId() +
                ", createdOn=" + createdOn +
                ", roundNumber=" + roundNumber +
                '}';
    }
}

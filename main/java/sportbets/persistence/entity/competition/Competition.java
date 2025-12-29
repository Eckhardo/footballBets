package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.authorization.CompetitionRole;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class Competition {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    private int winMultiplicator = 3;
    private int remisMultiplicator = 1;
    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    final Set<CompetitionRole> competitionRoles = new HashSet<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<CompetitionRound> competitionRounds = new HashSet<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<CompetitionTeam> competitionTeams = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comp_family_id")
    @NotNull
    private CompetitionFamily competitionFamily;


    /**
     * No-arg constructor for JavaBean tools.
     */
    public Competition() {
    }

    /**
     * Full constructor
     */
    public Competition(String name, String description, int winMultiplicator, int remisMultiplicator, @NotNull CompetitionFamily competitionFamily) {
        this.name = name;
        this.description = description;
        this.competitionFamily = competitionFamily;
        //guarantee ref integrity
        //   competitionFamily.addCompetition(this);
        this.winMultiplicator = winMultiplicator;
        this.remisMultiplicator = remisMultiplicator;


    }

    public Competition(Long source) {
        this.id = source;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CompetitionRound> getCompetitionRounds() {
        return competitionRounds;
    }

    public void addCompetitionRound(CompetitionRound round) {
        if (round == null)
            throw new IllegalArgumentException("Can't add a null round.");
        this.getCompetitionRounds().add(round);
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

    public CompetitionFamily getCompetitionFamily() {
        return competitionFamily;
    }

    public void setCompetitionFamily(CompetitionFamily competitionFamily) {
        this.competitionFamily = competitionFamily;
    }

    public Set<CompetitionTeam> getCompetitionTeams() {
        return competitionTeams;
    }

    public void addCompetitionTeam(CompetitionTeam competitionTeam) {
        if (competitionTeam == null)
            throw new IllegalArgumentException("Can't add a null competitionTeam.");
        this.getCompetitionTeams().add(competitionTeam);

    }


    public Set<CompetitionRole> getCompetitionRoles() {
        return competitionRoles;
    }

    public void addCompetitionRole(CompetitionRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Can't add a null competiiton role.");
        }
        this.getCompetitionRoles().add(role);
    }

    public int getWinMultiplicator() {
        return winMultiplicator;
    }

    public void setWinMultiplicator(int winMultiplicator) {
        this.winMultiplicator = winMultiplicator;
    }

    public int getRemisMultiplicator() {
        return remisMultiplicator;
    }

    public void setRemisMultiplicator(int remisMultiplicator) {
        this.remisMultiplicator = remisMultiplicator;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Competition that = (Competition) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdOn);
    }

    @Override
    public String toString() {
        return "Competition{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", winMultiplicator=" + winMultiplicator +
                ", remisMultiplicator=" + remisMultiplicator +
                ", createdOn=" + createdOn +
                ", family [=" + (competitionFamily == null ? null : competitionFamily) +
                "]}";
    }
}

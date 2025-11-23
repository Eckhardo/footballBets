package sportbets.persistence.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String acronym;

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<CompetitionTeam> competitionTeams= new HashSet();

    public Team() {

    }

    public Team(String name, String acronym) {
        this.name = name;
        this.acronym = acronym;
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Set<CompetitionTeam> getCompetitionTeams() {
        return competitionTeams;
    }
    public void addCompetitionTeam(CompetitionTeam competitionTeam) {
        if (competitionTeam == null)
            throw new IllegalArgumentException("Can't add a null competitionTeam.");
        this.getCompetitionTeams().add(competitionTeam);

    }
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && Objects.equals(name, team.name) && Objects.equals(acronym, team.acronym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym);
    }
}

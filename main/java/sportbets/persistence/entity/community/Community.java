package sportbets.persistence.entity.community;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.competition.CompetitionMembership;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "community")
public class Community {
    private static final Logger log = LoggerFactory.getLogger(Community.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private final LocalDateTime createdOn = LocalDateTime.now();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<CommunityRole> communityRoles = new HashSet<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CompetitionMembership> competitionMemberships = new HashSet<>();

    public Community() {

    }

    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Community(String name, String description, Set<CommunityRole> communityRoles, Set<CompetitionMembership> competitionMemberships) {
        this(name, description);
        this.description = description;
        this.communityRoles = communityRoles;
        this.competitionMemberships = competitionMemberships;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CommunityRole> getCommunityRoles() {
        return communityRoles;
    }

    public void addCommunityRole(CommunityRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Can't add a null Community role.");
        }
        this.getCommunityRoles().add(role);
    }

    public Set<CompetitionMembership> getCompetitionMemberships() {
        return competitionMemberships;
    }

    public void addCompetitionMembership(CompetitionMembership compMemb) {
        if (compMemb == null) {
            throw new IllegalArgumentException("Can't add a null CompetitionMembership role.");
        }
        this.getCompetitionMemberships().add(compMemb);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return Objects.equals(name, community.name) && Objects.equals(description, community.description) && Objects.equals(createdOn, community.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, createdOn);
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
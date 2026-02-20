package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import sportbets.common.Country;
import sportbets.common.CountryAttributeConverter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class CompetitionFamily {
    @OneToMany(mappedBy = "competitionFamily", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Competition> competitions = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @Convert(converter = CountryAttributeConverter.class)
    private Country country;

    @Column(nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    private boolean hasLigaModus;
    /**
     * has clubs or countries
     */
    private boolean hasClubs;

    //	********************** Constructors ********************** //

    /**
     * No-arg constructor for JavaBean tools.
     */

    public CompetitionFamily() {

        // TODO Auto-generated constructor stub
    }

    /**
     * Basic constructor.
     */
    public CompetitionFamily(String name, String description, Boolean ligaModus, Boolean hasClubs, Country country) {

        this.name = name;
        this.description = description;
        this.hasLigaModus = ligaModus;
        this.hasClubs = hasClubs;
        this.createdOn = LocalDateTime.now();
        this.country = country;

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

    public boolean isHasClubs() {
        return hasClubs;
    }

    public void setHasClubs(boolean hasClubs) {
        this.hasClubs = hasClubs;
    }

    public boolean isHasLigaModus() {
        return hasLigaModus;
    }

    public void setHasLigaModus(boolean hasLigaModus) {
        this.hasLigaModus = hasLigaModus;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Competition> getCompetitions() {
        return competitions;
    }

    public void addCompetition(Competition competition) {
        if (competition == null)
            throw new IllegalArgumentException("Can't add a null competition.");
        this.getCompetitions().add(competition);

    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionFamily that = (CompetitionFamily) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdOn);
    }

    @Override
    public String toString() {
        return "CompetitionFamily{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hasLigaModus=" + hasLigaModus +
                ", country=" + country +
                ", hasClubs=" + hasClubs +
                '}';
    }

}

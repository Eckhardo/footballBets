package sportbets.persistence.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class CompetitionFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "competitionFamily",cascade ={ CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private final Set<Competition> competitions = new HashSet<>();

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdOn=new Date();

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
    public CompetitionFamily(String name, String description, Boolean ligaModus, Boolean hasClubs) {

        this.name = name;
        this.description = description;
        this.hasLigaModus = ligaModus;
        this.hasClubs = hasClubs;

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

    public Date getCreatedOn() {
        return createdOn;
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
                ", hasClubs=" + hasClubs +
                '}';
    }

}

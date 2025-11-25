package sportbets.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class CompetitionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private int groupNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdOn=new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_round_id")
    @NotNull
    private CompetitionRound competitionRound;

    @OneToMany(mappedBy = "competitionGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Spiel> spiele=new HashSet<>();



    //	********************** Constructors ********************** //
    /**
     * No-arg constructor for JavaBean tools.
     */
   public CompetitionGroup() {}

    /**
     * Full constructor.
     */
    public CompetitionGroup(String name, Integer groupNumber,CompetitionRound competitionRound) {
        this.name=name;
        this.groupNumber=groupNumber;
        this.competitionRound=competitionRound;

        //guarantee ref integrity
       // competitionRound.addCompetitionGroup(this);
    }
    //	********************** Getter/Setter Methods ********************** //


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

    public int getGroupNumber() {
        return groupNumber;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public CompetitionRound getCompetitionRound() {
        return competitionRound;
    }
    public Set<Spiel> getSpiele() {
        return spiele;
    }
    public void addSpiel(Spiel spiel) {
        spiele.add(spiel);
    }
    public void setCompetitionRound(CompetitionRound competitionRound) {
        this.competitionRound = competitionRound;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionGroup that = (CompetitionGroup) o;
        return id == that.id && groupNumber == that.groupNumber && Objects.equals(name, that.name) && Objects.equals(createdOn, that.createdOn) && Objects.equals(competitionRound, that.competitionRound);
    }

    @Override
    public String toString() {
        return "CompetitionGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupNumber=" + groupNumber +
                ", createdOn=" + createdOn +
                ", competitionRound=" + competitionRound +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, groupNumber, createdOn, competitionRound);
    }
}

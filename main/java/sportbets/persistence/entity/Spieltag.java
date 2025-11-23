package sportbets.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.common.DateUtil;

import java.util.Date;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Spieltag {
    private static final Logger log = LoggerFactory.getLogger(Spieltag.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int spieltagNumber;

    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_round_id")
    @NotNull
    private CompetitionRound competitionRound;

    @OneToMany(mappedBy = "spieltag", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SortedSet<Spiel> spiele=new TreeSet<>();

    public Spieltag() {

    }

    public Spieltag(int spieltagNumber, Date startDate, CompetitionRound competitionRound) {
        this.spieltagNumber = spieltagNumber;
        this.startDate = startDate;
        this.competitionRound = competitionRound;
        // guarantee ref integrity
        competitionRound.addSpieltag(this);
    }


    public CompetitionRound getCompetitionRound() {
        return competitionRound;
    }

    public void setCompetitionRound(CompetitionRound competitionRound) {
        this.competitionRound = competitionRound;
    }

    public SortedSet<Spiel> getSpiele() {
        return spiele;
    }
    public void addSpiel(Spiel spiel) {
        spiele.add(spiel);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(int spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getFormattedStartDate() {
        return DateUtil.formatDate(this.startDate);
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {

        log.debug(" call equals::");
        if (o == null || getClass() != o.getClass()) return false;
        Spieltag spieltag = (Spieltag) o;
        return spieltagNumber == spieltag.spieltagNumber && Objects.equals(id, spieltag.id) && Objects.equals(startDate, spieltag.startDate) && Objects.equals(competitionRound, spieltag.competitionRound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spieltagNumber, startDate);
    }

    @Override
    public String toString() {
        return "Spieltag{" +
                "id=" + id +
                ", spieltagNumber=" + spieltagNumber +
                ", createdOn=" + startDate +
                ", competitionRound=" + competitionRound +
                '}';
    }
}

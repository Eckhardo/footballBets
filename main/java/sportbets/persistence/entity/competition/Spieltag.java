package sportbets.persistence.entity.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import sportbets.common.DateUtil;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Spieltag {
    private static final Logger log = LoggerFactory.getLogger(Spieltag.class);
    @OneToMany(mappedBy = "spieltag", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final SortedSet<Spiel> spiele = new TreeSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int spieltagNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comp_round_id")
    @NotNull
    private CompetitionRound competitionRound;

    public Spieltag() {

    }

    public Spieltag(int spieltagNumber, LocalDateTime startDate, CompetitionRound competitionRound) {
        this.spieltagNumber = spieltagNumber;
        this.startDate = startDate;
        this.competitionRound = competitionRound;
        // guarantee ref integrity
        // competitionRound.addSpieltag(this);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(int spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getFormattedStartDate() {
        return DateUtil.formatDate(this.startDate);
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
                ", startDate=" + startDate +
                ", competitionRound.name=" + competitionRound.getName() +
                ", competitionRound.id=" + competitionRound.getId() +
                '}';
    }
}

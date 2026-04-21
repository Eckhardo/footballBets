package sportbets.persistence.entity.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import sportbets.common.DateUtil;
import sportbets.persistence.entity.tipps.TippConfig;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class Spieltag {
    private static final Logger log = LoggerFactory.getLogger(Spieltag.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column(nullable = false)
    private int spieltagNumber;
    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_round_id",foreignKey = @ForeignKey(name = "FK_SPIELTAG_TO_ROUND"))
    @NotNull
    private CompetitionRound competitionRound;

    @OneToMany(mappedBy = "spieltag", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final Set<Spiel> spiele = new HashSet<>();


    // Owning Side:
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tipp_config_id")
    private TippConfig tippConfig;

    public Spieltag() {

    }

    public Spieltag(int spieltagNumber, LocalDateTime startDate, CompetitionRound competitionRound) {
        this.spieltagNumber = spieltagNumber;
        this.startDate = startDate;
        this.competitionRound = competitionRound;
     }


    public CompetitionRound getCompetitionRound() {
        return competitionRound;
    }

    public void setCompetitionRound(CompetitionRound competitionRound) {
        this.competitionRound = competitionRound;
    }

    public Set<Spiel> getSpiele() {
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

    public TippConfig getTippConfig() {
        return tippConfig;
    }

    public void setTippConfig(TippConfig tippConfig) {
        this.tippConfig = tippConfig;
    }

    public String getFormattedStartDate() {
        return DateUtil.formatDate(this.startDate);
    }

    @Override
    public boolean equals(Object o) {

        log.debug(" call equals::");
        if (o == null || getClass() != o.getClass()) return false;
        Spieltag spieltag = (Spieltag) o;
        return spieltagNumber == spieltag.spieltagNumber && Objects.equals(id, spieltag.id) && Objects.equals(createdOn, spieltag.createdOn) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spieltagNumber, createdOn);
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

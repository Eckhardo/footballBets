package sportbets.persistence.entity.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import sportbets.common.DateUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
public class Spiel implements Comparable<Spiel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int spielNumber;

    private int heimTore;

    private int gastTore;


    private boolean stattgefunden;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime anpfiffdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_heim_team_id",foreignKey = @ForeignKey(name = "FK_SPIEL_TO_HEIM_TEAM"))
    private Team heimTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_gast_team_id",foreignKey = @ForeignKey(name = "FK_SPIEL_TO_GAST_TEAM"))
    private Team gastTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_spieltag_id",foreignKey = @ForeignKey(name = "FK_SPIEL_TO_SPIELTAG"))
    private Spieltag spieltag;

    @ManyToOne(fetch = FetchType.EAGER)
    private CompetitionGroup competitionGroup;




    @OneToMany(mappedBy = "spiel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final Set<SpielFormula> spielFormulas = new HashSet<>();

    public Spiel() {

    }

    /**
     * Simple properties only constructor for not-group spiele.
     */
    public Spiel(Spieltag spieltag, int spielNumber, LocalDateTime startDate,
                 Team heimTeam, Team gastTeam) {
        this.spieltag = spieltag;
        this.spielNumber = spielNumber;
        this.anpfiffdate = startDate;
        this.stattgefunden = false;
        this.heimTeam = heimTeam;
        this.gastTeam = gastTeam;

        // Guarantee referential integrity
        //  spieltag.addSpiel(this);

    }

    /**
     * Simple properties only constructor for group spiele.
     */
    public Spiel(Spieltag spieltag, int spielNumber, LocalDateTime startDate,
                 Team heimTeam, Team gastTeam, CompetitionGroup group) {
        this(spieltag, spielNumber, startDate, heimTeam, gastTeam);
        this.competitionGroup = group;
        // Guarantee referential integrity
        //   group.addSpiel(this);
    }

    /**
     * Full constructor without group spiele
     */
    public Spiel(Spieltag spieltag, int spielNumber, LocalDateTime startDate,
                 Team heimTeam, Team gastTeam, Integer heimTore, Integer gastTore,
                 Boolean stattgefunden) {
        this(spieltag, spielNumber, startDate, heimTeam, gastTeam);
        this.stattgefunden = stattgefunden;
        this.heimTore = heimTore;
        this.gastTore = gastTore;

    }

    /**
     * Full constructor group spiele
     */
    public Spiel(Spieltag spieltag, int spielNumber, LocalDateTime startDate,
                 Team heimTeam, Team gastTeam, CompetitionGroup group, Integer heimTore, Integer gastTore,
                 Boolean stattgefunden) {
        this(spieltag, spielNumber, startDate, heimTeam, gastTeam, group);
        this.stattgefunden = stattgefunden;
        this.heimTore = heimTore;
        this.gastTore = gastTore;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSpielNumber() {
        return spielNumber;
    }

    public void setSpielNumber(int spielNumber) {
        this.spielNumber = spielNumber;
    }

    public int getHeimTore() {
        return heimTore;
    }

    public void setHeimTore(int heimTore) {
        this.heimTore = heimTore;
    }

    public int getGastTore() {
        return gastTore;
    }

    public void setGastTore(int gastTore) {
        this.gastTore = gastTore;
    }

    public boolean isStattgefunden() {
        return stattgefunden;
    }

    public void setStattgefunden(boolean stattgefunden) {
        this.stattgefunden = stattgefunden;
    }

    public LocalDateTime getAnpfiffdate() {
        return anpfiffdate;
    }

    public void setAnpfiffdate(LocalDateTime anpfiffdate) {
        this.anpfiffdate = anpfiffdate;
    }

    public String getFormattedAnpfiffDate() {
        return DateUtil.formatDate(this.anpfiffdate);
    }

    public Team getHeimTeam() {
        return heimTeam;
    }

    public void setHeimTeam(Team heimTeam) {
        this.heimTeam = heimTeam;
    }

    public Team getGastTeam() {
        return gastTeam;
    }

    public void setGastTeam(Team gastTeam) {
        this.gastTeam = gastTeam;
    }

    public Spieltag getSpieltag() {
        return spieltag;
    }

    public void setSpieltag(Spieltag spieltag) {
        this.spieltag = spieltag;
    }

    public CompetitionGroup getCompetitionGroup() {
        return competitionGroup;
    }

    public void setCompetitionGroup(CompetitionGroup competitionGroup) {
        this.competitionGroup = competitionGroup;
    }
    public Set<SpielFormula> getSpielFormulas() {
        return spielFormulas;
    }
    public Optional<SpielFormula> getSpielFormulaForHeim() {
        for (SpielFormula spielFormula : spielFormulas) {
            if (spielFormula.isHeimTeam()) {
                return Optional.of(spielFormula);
            }
        }
        return Optional.empty();
    }
    public Optional<SpielFormula> getSpielFormulaForGast() {
        for (SpielFormula spielFormula : spielFormulas) {
            if (! spielFormula.isHeimTeam()) {
                return Optional.of(spielFormula);
            }
        }
        return Optional.empty();
    }

    public void addSpielFormula(SpielFormula spielFormula) {
        if (spielFormula == null)
            throw new IllegalArgumentException("Can't add a null spielFormula.");
        this.getSpielFormulas().add(spielFormula);

    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Spiel spiel = (Spiel) o;
        return Objects.equals(id, spiel.id) && spielNumber == spiel.spielNumber && stattgefunden == spiel.stattgefunden && Objects.equals(anpfiffdate, spiel.anpfiffdate) && Objects.equals(heimTeam, spiel.heimTeam) && Objects.equals(gastTeam, spiel.gastTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spielNumber, stattgefunden, anpfiffdate, heimTeam, gastTeam);
    }

    @Override
    public String toString() {
        return "Spiel{" +
                "id=" + id +
                ", spielNumber=" + spielNumber +
                ", heimTore=" + heimTore +
                ", gastTore=" + gastTore +
                ", stattgefunden=" + stattgefunden +
                ", anpfiffdate=" + anpfiffdate +
                ", heimTeam id=" + heimTeam.getId() +
                ", gastTeam id=" + gastTeam.getId() +
                ", heimTeam=" + heimTeam.getAcronym() +
                ", gastTeam=" + gastTeam.getAcronym() +
                ", spieltag ID=" + spieltag.getId() +
                ", spieltagNr=" + spieltag.getSpieltagNumber() +
                '}';
    }

    @Override
    public int compareTo(Spiel o) {
        return o.getSpielNumber() - this.getSpielNumber();
    }
}
package sportbets.persistence.entity;

import jakarta.persistence.*;
import sportbets.common.DateUtil;

import java.util.Date;
import java.util.Objects;

@Entity
public class Spiel implements Comparable<Spiel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int spielNumber;

    private int heimTore;

    private int gastTore;


    private boolean stattgefunden;


    private Date anpfiffdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_heim_team_id")
    private Team heimTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_gast_team_id")
    private Team gastTeam;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_spieltag_id")
    private Spieltag spieltag;

    @ManyToOne(fetch = FetchType.LAZY)
    private CompetitionGroup competitionGroup;

    public Spiel() {

    }

    /**
     * Simple properties only constructor for not-group spiele.
     */
    public Spiel(Spieltag spieltag, int spielNumber, Date startDate,
                 Team heimTeam, Team gastTeam) {
        this.spieltag = spieltag;
        this.spielNumber = spielNumber;
        this.anpfiffdate = startDate;
        this.stattgefunden = false;
        this.heimTeam = heimTeam;
        this.gastTeam = gastTeam;

        // Guarantee referential integrity
        spieltag.addSpiel(this);

    }
    /**
     * Simple properties only constructor for group spiele.
     */
    public Spiel(Spieltag spieltag, int spielNumber, Date startDate,
                 Team heimTeam, Team gastTeam,CompetitionGroup group) {
        this(spieltag, spielNumber, startDate,heimTeam,gastTeam);
        this.competitionGroup=group;
        // Guarantee referential integrity
        group.addSpiel(this);
    }

    /**
     * Full constructor without group spiele
     */
    public Spiel(Spieltag spieltag, int spielNumber, Date startDate,
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
    public Spiel(Spieltag spieltag, int spielNumber, Date startDate,
                 Team heimTeam, Team gastTeam,CompetitionGroup group, Integer heimTore, Integer gastTore,
                 Boolean stattgefunden) {
        this(spieltag, spielNumber, startDate, heimTeam, gastTeam,group);
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

    public Date getAnpfiffdate() {
        return anpfiffdate;
    }

    public String getFormattedAnpfiffDate() {
        return DateUtil.formatDate(this.anpfiffdate);
    }

    public void setAnpfiffdate(Date anpfiffdate) {
        this.anpfiffdate = anpfiffdate;
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

    public CompetitionGroup getCompetitionGroup() {
        return competitionGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Spiel spiel = (Spiel) o;
        return id == spiel.id && spielNumber == spiel.spielNumber && stattgefunden == spiel.stattgefunden && Objects.equals(anpfiffdate, spiel.anpfiffdate) && Objects.equals(heimTeam, spiel.heimTeam) && Objects.equals(gastTeam, spiel.gastTeam);
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
                ", heimTeam=" + heimTeam +
                ", gastTeam=" + gastTeam +
                ", spieltag=" + spieltag +
                ", competitionGroup=" + competitionGroup +
                '}';
    }

    @Override
    public int compareTo(Spiel o) {
        return o.getSpielNumber()-this.getSpielNumber();
    }
}
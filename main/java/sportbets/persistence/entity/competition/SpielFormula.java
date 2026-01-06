package sportbets.persistence.entity.competition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class SpielFormula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int won;

    private int remis;

    private int lost;

    private boolean isHeimTeam;

    private int points;

    private int heimTore;

    private int gastTore;

    private int diffTore;

    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    private String teamName;
    private String teamNameAcronym;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_spiel_id", foreignKey = @ForeignKey(name = "FK_FORMULA_TO_SPIEL"))
    @NotNull
    private Spiel spiel;



    public SpielFormula() {

    }


    /**
     * Simple properties constructor.
     */
    public SpielFormula(Spiel spiel, String teamName, String teamNameAcronym, boolean isHeimTeam) {
        this.spiel = spiel;
        this.teamName = teamName;
        this.teamNameAcronym = teamNameAcronym;
        this.isHeimTeam = isHeimTeam;


    }

    /**
     * Full constructor.
     */
    public SpielFormula(Spiel spiel, String teamName, String teamNameAcronym, boolean isHeimTeam,
                        int heimTore, int gastTore, int points) {
        this(spiel, teamName, teamNameAcronym, isHeimTeam);
        this.heimTore = heimTore;
        this.gastTore = gastTore;
        calculateTrend(heimTore, gastTore, spiel.isStattgefunden());
        this.points = points;


    }

    public int getDiffTore() {
        return diffTore;
    }

    public void setDiffTore(int diffTore) {
        this.diffTore = diffTore;
    }

    public int getGastTore() {
        return gastTore;
    }

    public void setGastTore(int gastTore) {
        this.gastTore = gastTore;
    }

    public int getHeimTore() {
        return heimTore;
    }

    public void setHeimTore(int heimTore) {
        this.heimTore = heimTore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHeimTeam() {
        return isHeimTeam;
    }

    public void setHeimTeam(boolean heimTeam) {
        isHeimTeam = heimTeam;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRemis() {
        return remis;
    }

    public void setRemis(int remis) {
        this.remis = remis;
    }

    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNameAcronym() {
        return teamNameAcronym;
    }

    public void setTeamNameAcronym(String teamNameAcronym) {
        this.teamNameAcronym = teamNameAcronym;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public void calculateTrend(int tore1, int tore2,
                               boolean stattgefunden) {
        if (stattgefunden) {

            if (tore1 == tore2) {
                remis = 1;
            } else if (tore1 > tore2) {
                won = 1;
            } else {
                lost = 1;

            }
        } else {
            remis = won = lost = 0;
        }
    }


    public static int calculatePoints(Competition competition,
                                          int heimTor, int gastTor, boolean stattgefunden) {

        if (stattgefunden) {
            if (heimTor == gastTor) {
               return competition.getRemisMultiplicator();
            } else if (heimTor < gastTor) {
                return 0;
            } else {
                return competition.getWinMultiplicator();
            }
        }
        return 0;
    }

}

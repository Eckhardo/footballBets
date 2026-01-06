package sportbets.persistence.rowObject;

import java.io.Serializable;
import java.util.Comparator;

public class TeamPositionSummaryRow implements Serializable, Comparable<TeamPositionSummaryRow> {

    private String teamName;

    private String teamNameAcronym;

    private int spieltage;

    private int points;

    private int heimtore;

    private int gasttore;

    private int difftore;

    private int gamesWon;

    private int gamesRemis;

    private int gamesLost;

    public TeamPositionSummaryRow() {

    }

    public TeamPositionSummaryRow(String name, int spieltage, int points, int heimtore, int gasttore, int difftore, int gamesWon, int gamesRemis, int gamesLost) {
        this.teamName = name;
        this.spieltage = spieltage;
        this.points = points;
        this.heimtore = heimtore;
        this.gasttore = gasttore;
        this.difftore = difftore;
        this.gamesWon = gamesWon;
        this.gamesRemis = gamesRemis;
        this.gamesLost = gamesLost;

    }

    public TeamPositionSummaryRow(String name, String acro, int spieltage, int points, int heimtore, int gasttore, int difftore, int gamesWon, int gamesRemis, int gamesLost) {
        this.teamName = name;
        this.teamNameAcronym = acro;
        this.spieltage = spieltage;
        this.points = points;
        this.heimtore = heimtore;
        this.gasttore = gasttore;
        this.difftore = difftore;
        this.gamesWon = gamesWon;
        this.gamesRemis = gamesRemis;
        this.gamesLost = gamesLost;

    }
    /**
     * @param position The position to set.
     */
    /**
     * @return Returns the difftore.
     */
    public int getDifftore() {
        return difftore;
    }

    /**
     * @return Returns the gamesLost.
     */
    public int getGamesLost() {
        return gamesLost;
    }

    /**
     * @return Returns the gamesRemis.
     */
    public int getGamesRemis() {
        return gamesRemis;
    }

    /**
     * @return Returns the gamesWon.
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * @return Returns the gasttore.
     */
    public int getGasttore() {
        return gasttore;
    }

    /**
     * @return Returns the heimtore.
     */
    public int getHeimtore() {
        return heimtore;
    }

    /**
     * @return Returns the points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return Returns the spieltage.
     */
    public int getSpieltage() {
        return spieltage;
    }

    /**
     * @return Returns the teamName.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @return Returns the teamNameAcronym.
     */
    public String getTeamNameAcronym() {
        return teamNameAcronym;
    }

    @Override
    public int compareTo(TeamPositionSummaryRow tr) {
        return Comparator.comparingInt(TeamPositionSummaryRow::getPoints)
                .thenComparingInt(TeamPositionSummaryRow::getDifftore)
                .thenComparingInt(TeamPositionSummaryRow::getHeimtore)
                .compare(this, tr);
    }
}

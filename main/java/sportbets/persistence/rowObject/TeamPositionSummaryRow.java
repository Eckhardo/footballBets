package sportbets.persistence.rowObject;

import java.io.Serializable;
import java.util.Comparator;

public class TeamPositionSummaryRow implements Serializable, Comparable<TeamPositionSummaryRow> {

    private String teamName;

    private String teamNameAcronym;

    private long spieltage;

    private long points;

    private long heimtore;

    private long gasttore;

    private long difftore;

    private long gamesWon;

    private long gamesRemis;

    private long gamesLost;

    public TeamPositionSummaryRow() {

    }

    public TeamPositionSummaryRow(String name, long spieltage, long points, long heimtore, long gasttore, long difftore, long gamesWon, long gamesRemis, long gamesLost) {
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

    public TeamPositionSummaryRow(String name, String acro, long spieltage, long polongs, long heimtore, long gasttore, long difftore, long gamesWon, long gamesRemis, long gamesLost) {
        this.teamName = name;
        this.teamNameAcronym = acro;
        this.spieltage = spieltage;
        this.points = polongs;
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
    public long getDifftore() {
        return difftore;
    }

    /**
     * @return Returns the gamesLost.
     */
    public long getGamesLost() {
        return gamesLost;
    }

    /**
     * @return Returns the gamesRemis.
     */
    public long getGamesRemis() {
        return gamesRemis;
    }

    /**
     * @return Returns the gamesWon.
     */
    public long getGamesWon() {
        return gamesWon;
    }

    /**
     * @return Returns the gasttore.
     */
    public long getGasttore() {
        return gasttore;
    }

    /**
     * @return Returns the heimtore.
     */
    public long getHeimtore() {
        return heimtore;
    }

    /**
     * @return Returns the polongs.
     */
    public long getPoints() {
        return points;
    }

    /**
     * @return Returns the spieltage.
     */
    public long getSpieltage() {
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
        return Comparator.comparingLong(TeamPositionSummaryRow::getPoints)
                .thenComparingLong(TeamPositionSummaryRow::getDifftore)
                .thenComparingLong(TeamPositionSummaryRow::getHeimtore)
                .compare(this, tr);
    }

    @Override
    public String toString() {
        return "TeamPositionSummaryRow{" +
                "difftore=" + difftore +
                ", teamName='" + teamName + '\'' +
                ", teamNameAcronym='" + teamNameAcronym + '\'' +
                ", spieltage=" + spieltage +
                ", points=" + points +
                ", heimtore=" + heimtore +
                ", gasttore=" + gasttore +
                ", gamesWon=" + gamesWon +
                ", gamesRemis=" + gamesRemis +
                ", gamesLost=" + gamesLost +
                '}';
    }
}

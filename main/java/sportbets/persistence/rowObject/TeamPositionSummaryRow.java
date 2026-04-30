package sportbets.persistence.rowObject;

import java.io.Serializable;
import java.util.Comparator;

public class TeamPositionSummaryRow implements Serializable, Comparable<TeamPositionSummaryRow> {

    private final String teamName;

    private final String teamNameAcronym;

    private final long spieltage;

    private final long points;

    private final long heimtore;

    private final long gasttore;

    private final long difftore;

    private final long gamesWon;

    private final long gamesRemis;

    private final long gamesLost;



    public TeamPositionSummaryRow(String teamName, String teamNameAcronym, long spieltage, long points, long heimtore, long gasttore, long difftore, long gamesWon, long gamesRemis, long gamesLost) {
      super();
        this.teamName = teamName;
        this.teamNameAcronym = teamNameAcronym;
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

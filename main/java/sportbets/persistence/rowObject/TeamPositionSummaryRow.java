package sportbets.persistence.rowObject;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Comparator;

public record TeamPositionSummaryRow(String teamName, String teamNameAcronym, long spieltage, long points,
                                     long heimtore, long gasttore, long difftore, long gamesWon, long gamesRemis,
                                     long gamesLost) implements Serializable, Comparable<TeamPositionSummaryRow> {

    /**
     * @param position The position to set.
     */
    /**
     * @return Returns the difftore.
     */
    @Override
    public long difftore() {
        return difftore;
    }

    /**
     * @return Returns the gamesLost.
     */
    @Override
    public long gamesLost() {
        return gamesLost;
    }

    /**
     * @return Returns the gamesRemis.
     */
    @Override
    public long gamesRemis() {
        return gamesRemis;
    }

    /**
     * @return Returns the gamesWon.
     */
    @Override
    public long gamesWon() {
        return gamesWon;
    }

    /**
     * @return Returns the gasttore.
     */
    @Override
    public long gasttore() {
        return gasttore;
    }

    /**
     * @return Returns the heimtore.
     */
    @Override
    public long heimtore() {
        return heimtore;
    }

    /**
     * @return Returns the polongs.
     */
    @Override
    public long points() {
        return points;
    }

    /**
     * @return Returns the spieltage.
     */
    @Override
    public long spieltage() {
        return spieltage;
    }

    /**
     * @return Returns the teamName.
     */
    @Override
    public String teamName() {
        return teamName;
    }

    /**
     * @return Returns the teamNameAcronym.
     */
    @Override
    public String teamNameAcronym() {
        return teamNameAcronym;
    }

    @Override
    public int compareTo(@NonNull TeamPositionSummaryRow tr) {
        return Comparator.comparingLong(TeamPositionSummaryRow::points)
                .thenComparingLong(TeamPositionSummaryRow::difftore)
                .thenComparingLong(TeamPositionSummaryRow::heimtore)
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

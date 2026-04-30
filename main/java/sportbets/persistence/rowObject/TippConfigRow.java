package sportbets.persistence.rowObject;

import java.io.Serializable;

public class TippConfigRow implements Serializable {


    private final String competitionName;

    private final Long roundId;

    private final String roundName;

    private final String tippModusName;

    private final Long compMembId;
    private final int spieltagNumber;


    public TippConfigRow(String competitionName, Long roundId, String roundName, String tippModusName, Long compMembId, int spieltagNumber) {
        this.competitionName = competitionName;
        this.roundId = roundId;
        this.roundName = roundName;
        this.tippModusName = tippModusName;
        this.compMembId = compMembId;
        this.spieltagNumber = spieltagNumber;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public Long getRoundId() {
        return roundId;
    }

    public String getRoundName() {
        return roundName;
    }

    public String getTippModusName() {
        return tippModusName;
    }

    public Long getCompMembId() {
        return compMembId;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    @Override
    public String toString() {
        return "TippConfigRow{" +
                "competitionName='" + competitionName + '\'' +
                ", spieltag=" + spieltagNumber +
                ", roundId=" + roundId +
                ", roundName='" + roundName + '\'' +
                ", tippModusName='" + tippModusName + '\'' +
                ", compMembId=" + compMembId +
                '}';
    }
}

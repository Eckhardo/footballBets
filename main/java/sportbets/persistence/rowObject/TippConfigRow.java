package sportbets.persistence.rowObject;

import java.io.Serializable;

public class TippConfigRow implements Serializable {

    private final Long id;

    private final String competitionName;
    private final Long compMembId;
    private final String roundName;
    private final Long tippModusId;
    private final String tippModusName;
    private final Long spieltagId;
    private final int spieltagNumber;

    public TippConfigRow(Long id, String competitionName, Long compMembId, String roundName, Long tippModusId, String tippModusName, Long spieltagId, int spieltagNumber) {
        this.id = id;
        this.competitionName = competitionName;
        this.compMembId = compMembId;
        this.roundName = roundName;
        this.tippModusId = tippModusId;
        this.tippModusName = tippModusName;
        this.spieltagId = spieltagId;
        this.spieltagNumber = spieltagNumber;
    }

    public String getCompetitionName() {
        return competitionName;
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

    public Long getId() {
        return id;
    }

    public Long getTippModusId() {
        return tippModusId;
    }

    public Long getSpieltagId() {
        return spieltagId;
    }

    @Override
    public String toString() {
        return "TippConfigRow{" +
                "id=" + id +
                ", competitionName='" + competitionName + '\'' +
                ", compMembId=" + compMembId +
                ", roundName='" + roundName + '\'' +
                ", tippModusId=" + tippModusId +
                ", tippModusName='" + tippModusName + '\'' +
                ", spieltagId=" + spieltagId +
                ", spieltagNumber=" + spieltagNumber +
                '}';
    }
}

package sportbets.persistence.rowObject;

import java.io.Serializable;

public record TippConfigRow(Long id, String competitionName, Long compMembId, String roundName, Long tippModusId,
                            String tippModusName, Long spieltagId, int spieltagNumber) implements Serializable {

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

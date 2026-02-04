package sportbets.web.dto.competition.batch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MatchdayBatchRecord(int firstMatchdayNumber, int lastMatchdayNumber,
                                  @NotNull(message = " round id cannot be null") Long compRoundId,
                                  @NotBlank(message = " round name cannot be empty") String compRoundName) {

    public MatchdayBatchRecord(int firstMatchdayNumber, int lastMatchdayNumber, Long compRoundId, String compRoundName) {
        this.firstMatchdayNumber = firstMatchdayNumber;
        this.lastMatchdayNumber = lastMatchdayNumber;
        this.compRoundId = compRoundId;
        this.compRoundName = compRoundName;
    }

    @Override
    public Long compRoundId() {
        return compRoundId;
    }

    @Override
    public String compRoundName() {
        return compRoundName;
    }

    @Override
    public String toString() {
        return "MatchdayBatchRecord{" +
                "firstMatchdayNumber=" + firstMatchdayNumber +
                ", lastMatchdayNumber=" + lastMatchdayNumber +
                ", compRoundId=" + compRoundId +
                ", compRoundName='" + compRoundName + '\'' +
                '}';
    }
}

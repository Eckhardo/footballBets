package sportbets.web.dto.competition.batch;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MatchBatchRecord(@Positive(message = "firstMatchdayNumber must be positive") int firstMatchdayNumber,
                               @Positive(message = "lastMatchdayNumber must be positive") int lastMatchdayNumber,
                               @Positive(message = "numberOfMatches must be positive") int numberOfMatches,
                               @NotNull(message = " round id cannot be null") Long compRoundId,
                               @NotNull(message = " heim team id cannot be null") Long heimTeamId,
                               @NotNull(message = " gast team id cannot be null") Long gastTeamId ){


    public MatchBatchRecord(int firstMatchdayNumber, int lastMatchdayNumber, int numberOfMatches, Long compRoundId, Long heimTeamId,  Long gastTeamId) {
        this.firstMatchdayNumber = firstMatchdayNumber;
        this.lastMatchdayNumber = lastMatchdayNumber;
        this.numberOfMatches = numberOfMatches;
        this.compRoundId = compRoundId;
        this.heimTeamId = heimTeamId;
        this.gastTeamId = gastTeamId;

    }
}


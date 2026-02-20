package sportbets.web.dto.competition.batch;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MatchBatchRecord(
                               @NotNull(message = " round id cannot be null") Long compRoundId,
                               @NotNull(message = " heim team id cannot be null") Long heimTeamId,
                               @NotNull(message = " gast team id cannot be null") Long gastTeamId ){


    public MatchBatchRecord( Long compRoundId, Long heimTeamId,  Long gastTeamId) {
        this.compRoundId = compRoundId;
        this.heimTeamId = heimTeamId;
        this.gastTeamId = gastTeamId;

    }
}


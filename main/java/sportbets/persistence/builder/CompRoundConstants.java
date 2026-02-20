package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;

public class CompRoundConstants {


    public static CompetitionRound getHinrunde(Competition comp) {
        return new CompetitionRound(1, "Hinrunde", comp, false, 18, 17, 1);
    }

    public static CompetitionRound getRueckrunde(Competition comp) {
        return new CompetitionRound(2, "Rueckrunde", comp, false, 18, 17, 18);
    }
}

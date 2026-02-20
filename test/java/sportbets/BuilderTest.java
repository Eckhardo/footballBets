package sportbets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.builder.GenericBuilder;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;

public class BuilderTest {
    Logger logger = LoggerFactory.getLogger(BuilderTest.class);


    @Test
    public void buildCompFamily() {
        CompetitionFamily competitionFamily = GenericBuilder.of(CompetitionFamily::new)
                .with(CompetitionFamily::setName, "1. Bundesliga")
                .with(CompetitionFamily::setDescription, "1. Deutsche Fussball Bundesliga")
                .with(CompetitionFamily::setHasClubs, true).with(CompetitionFamily::setHasLigaModus, true)
                .build();

        System.out.println("competitionFamily: " + competitionFamily.toString());
    }


    @Test
    public void buildCompetition() {
        CompetitionFamily competitionFamily = GenericBuilder.of(CompetitionFamily::new)
                .with(CompetitionFamily::setName, "1. Bundesliga")
                .with(CompetitionFamily::setDescription, "1. Deutsche Fussball Bundesliga")
                .with(CompetitionFamily::setHasClubs, true).with(CompetitionFamily::setHasLigaModus, true)

                .build();

        Competition competition = GenericBuilder.of(Competition::new)
                .with(Competition::setName, "Saison 205/26")
                .with(Competition::setDescription, "1. Deutsche Fussball Bundesliga Saison 2025/26")
                .with(Competition::setRemisMultiplicator, 1).with(Competition::setWinMultiplicator, 3)
                .with(Competition::setCompetitionFamily, competitionFamily)
                .build();

        System.out.println("competition: " + competition.toString());
    }
}

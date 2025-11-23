package sportbets.testData;

import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;

public class CompetitionConstants {

    public static final String BUNDESLIGA2_NAME_2025 ="Saison 2005/26" ;
    public static String BUNDESLIGA_NAME_2025="Saison 2005/26";
    public static Competition getBundesliga2025(CompetitionFamily fam) {
        return new Competition(BUNDESLIGA_NAME_2025, "1. Deutsche Fussball Bundesliga Saison 2025/26",3,1, fam);
    }
}

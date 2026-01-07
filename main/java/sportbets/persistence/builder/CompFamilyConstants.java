package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.CompetitionFamily;

public class CompFamilyConstants {

    public static CompetitionFamily BUNDESLIGA = new CompetitionFamily("1. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
    public static CompetitionFamily FIFA_WM = new CompetitionFamily("FIFA WM", "Fussball Weltmeisterschaft", false, false);
    public static CompetitionFamily ZWEITE_BUNDESLIGA = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);

}

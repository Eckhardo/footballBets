package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.Team;

public class NewTeamConstans {

    static final Team[] teams = {

            new Team("FC Bayern München", "Bayern", true ),
            new Team("Hamburger SV", "HSV", true ),
            new Team("FC St. Pauli 1910", "St. Pauli", true ),
            new Team("SV Werder Bremen", "Bremen", true ),
            new Team("Borussia Dortmund", "Dortmund", true ),
            new Team("Bayer 04 Leverkusen", "Leverkusen", true ),
            new Team("VfL Wolfsburg", "Wolfsburg", true ),
            new Team("1. FC Köln", "Koeln", true ),
            new Team("SC Freiburg", "Freiburg", true ),
            new Team("TSG 1899 Hoffenheim", "Hoffenheim", true ),
            new Team("1. FC Union Berlin", "Union", true ),
            new Team("Borussia Mönchengladbach", "Gladbach", true ),
            new Team("Eintracht Frankfurt", "Frankfurt", true ),
            new Team("VfB Stuttgart", "Stuttgart", true ),
            new Team("1. FSV Mainz 05", "Mainz", true ),
            new Team("RB Leipzig", "Leipzig", true ),
            new Team("1. FC Heidenheim 1846", "Heidenheim", true ),
            new Team("FC Augsburg", "Augsburg", true)
    };


    public static Team[] getTeams() {
        return teams;
    }
}

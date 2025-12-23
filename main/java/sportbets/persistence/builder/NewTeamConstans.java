package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.Team;

public class NewTeamConstans {

    static Team[] teams = {

            new Team("FC Bayern München", "Bayern"),
            new Team("Hamburger SV", "HSV"),
            new Team("FC St. Pauli 1910", "St. Pauli"),
            new Team("SV Werder Bremen", "Bremen"),
            new Team("Borussia Dortmund", "Dortmund"),
            new Team("Bayer 04 Leverkusen", "Leverkusen"),
            new Team("VfL Wolfsburg", "Wolfsburg"),
            new Team("1. FC Köln", "Koeln"),
            new Team("SC Freiburg", "Freiburg"),
            new Team("TSG 1899 Hoffenheim", "Hoffenheim"),
            new Team("1. FC Union Berlin", "Union"),
            new Team("Borussia Mönchengladbach", "Gladbach"),
            new Team("Eintracht Frankfurt", "Frankfurt"),
            new Team("VfB Stuttgart", "Stuttgart"),
            new Team("1. FSV Mainz 05", "Mainz"),
            new Team("RB Leipzig", "Leipzig"),
            new Team("1. FC Heidenheim 1846", "Heidenheim"),
            new Team("FC Augsburg", "Augsburg")
    };


    public static Team[] getTeams() {
        return teams;
    }
}

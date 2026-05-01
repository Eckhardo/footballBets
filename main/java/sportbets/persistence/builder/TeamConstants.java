package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamConstants {


    public static List<Team> getBuliTeams(){

      List<Team> teams=new ArrayList<>();
        teams.add(BAY);
        teams.add(HSV);
        teams.add(PAULI);
        teams.add(FRANKFURT);
        teams.add(BVB);
        teams.add(WERDER);
        teams.add(WOLFSBURG);
        teams.add(KOELN);
        teams.add(LEVERKUSEN);
        teams.add(FREIBURG);
        teams.add(HOFFENHEIM);
        teams.add(HEIDENHEIM);
        teams.add(UNION);
        teams.add(GLADBACH);
        teams.add(MAINZ);
        teams.add(LEIPZIG);
        teams.add(AUGSBURG);
        teams.add(STUTTGART);
        return teams;


    }


    public static Team BAY = new Team("FC Bayern München", "Bayern", true );
    public static Team HSV = new Team("Hamburger SV", "HSV", true );
    public static Team PAULI = new Team("FC St. Pauli 1910", "St. Pauli", true );
    public static Team WERDER = new Team("SV Werder Bremen", "Bremen", true );
    public static Team BVB = new Team("Borussia Dortmund", "Dortmund", true );
    public static Team LEVERKUSEN = new Team("Bayer 04 Leverkusen", "Leverkusen", true );
    public static Team WOLFSBURG = new Team("VfL Wolfsburg", "Wolfsburg", true );
    public static Team KOELN = new Team("1. FC Köln", "Koeln", true );
    public static Team FREIBURG = new Team("SC Freiburg", "Freiburg", true );
    public static Team HOFFENHEIM = new Team("TSG 1899 Hoffenheim", "Hoffenheim", true );
    public static Team UNION = new Team("1. FC Union Berlin", "Union", true );
    public static Team GLADBACH = new Team("Borussia Mönchengladbach", "Gladbach", true );
    public static Team FRANKFURT = new Team("Eintracht Frankfurt", "Frankfurt", true );
    public static Team STUTTGART = new Team("VfB Stuttgart", "Stuttgart", true );
    public static Team MAINZ = new Team("1. FSV Mainz 05", "Mainz", true );
    public static Team LEIPZIG = new Team("RB Leipzig", "Leipzig", true );
    public static Team HEIDENHEIM = new Team("1. FC Heidenheim 1846", "Heidenheim", true );
    public static Team AUGSBURG = new Team("FC Augsburg", "Augsburg", true );
}

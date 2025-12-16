package sportbets.persistence.testData;

import sportbets.persistence.entity.*;

import java.time.LocalDateTime;

public class TestDataUtil {


    public static CompetitionFamily testFam = new CompetitionFamily("TestLiga", "2. Deutsche Fussball Bundesliga", true, true);

    public static Competition testComp = new Competition("TestLiga: Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFam);

    public static CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);

    public static Team team1 = new Team("Test1", "1");
    public static Team team2 = new Team("Test2", "2");
    public static Team team3 = new Team("Test3", "3");
    public static Team team4 = new Team("Test4", "4");


    public static Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
    public static Spiel testSpiel1 = new Spiel(testSpieltag, 1, LocalDateTime.now(), team1, team2, 3, 1, false);
    public static Spiel testSpiel2 = new Spiel(testSpieltag, 2, LocalDateTime.now(), team3, team4, 2, 2, false);


    public static CompetitionFamily getCompetitionFamily() {
        // Initialize test data before test methods

        testFam.addCompetition(getTestComp());
        return testFam;
    }

    public static Competition getTestComp() {

        testComp.addCompetitionRound(getCompRound());
        return testComp;
    }

    public static CompetitionRound getCompRound() {


        CompetitionTeam ct1 = new CompetitionTeam(team1, testComp);
        team1.addCompetitionTeam(ct1);
        CompetitionTeam ct2 = new CompetitionTeam(team2, testComp);
        team2.addCompetitionTeam(ct2);
        CompetitionTeam ct3 = new CompetitionTeam(team3, testComp);
        team3.addCompetitionTeam(ct3);
        CompetitionTeam ct4 = new CompetitionTeam(team4, testComp);
        team4.addCompetitionTeam(ct4);
        testComp.addCompetitionTeam(ct1);
        testComp.addCompetitionTeam(ct2);
        testComp.addCompetitionTeam(ct3);
        testComp.addCompetitionTeam(ct4);

        testRound.addSpieltag(getSpieltag());

        return testRound;
    }

    public static Spieltag getSpieltag() {


        testSpieltag.addSpiel(testSpiel1);
        testSpieltag.addSpiel(testSpiel2);
        return testSpieltag;
    }
}

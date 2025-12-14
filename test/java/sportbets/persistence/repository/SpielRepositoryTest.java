package sportbets.persistence.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpielRepositoryTest {
    private CompetitionFamily testFamily;
    private Competition testComp;


    private CompetitionRound testRound;

    private CompetitionGroup testGroup;
    private Spieltag testSpieltag;

    private Spiel testSpiel1;
    private Spiel testSpiel2;
    @Autowired
    private CompetitionFamilyRepository familyRepo;

    @Autowired
    private SpielRepository spielRepo;
    @Autowired
    private CompetitionRepository compRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Before
    public void setUp() {
      CompetitionFamily testFamily=  getCompFamily();
        familyRepo.save(testFamily);
        //  competitionDAO.save(testComp);
    }

    private CompetitionFamily getCompFamily() {
        // Initialize test data before test methods
        testFamily = new CompetitionFamily("TestLiga", "2. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("TestLiga: Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        testGroup = new CompetitionGroup("Gruppe A", 1, testRound);
        testRound.addCompetitionGroup(testGroup);
        testSpieltag = new Spieltag(1,LocalDateTime.now(), testRound);
        testRound.addSpieltag(testSpieltag);
        Team team1 = new Team("Test1", "1");
        Team team2 = new Team("Test2", "2");
        Team team3 = new Team("Test3", "3");
        Team team4 = new Team("Test4", "4");
      
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


        testSpiel1 = new Spiel(testSpieltag, 1, LocalDateTime.now(), team1, team2, 3, 1, false);
        testSpiel2 = new Spiel(testSpieltag, 2, LocalDateTime.now(), team3, team4, 2, 2, false);
        testSpieltag.addSpiel(testSpiel1);
        testSpieltag.addSpiel(testSpiel2);
       return  testFamily;
    }

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenGroupsAreFound() {
        Competition foundComp = compRepo.findByName(testComp.getName()).orElse(null);


        assertNotNull(foundComp);


        for (CompetitionRound round : foundComp.getCompetitionRounds()) {
            Set<Spieltag> spieltage = round.getSpieltage();

            assertEquals(1, spieltage.size());


        }
    }

    @Test
    public void whenFindByNameCalled_thenGroupsAreFound() {
        // given
        Predicate<Spiel> p1 = g -> g.getSpielNumber() == testSpiel1.getSpielNumber();
        Predicate<Spiel> p2 = g -> g.getSpielNumber() == testSpiel2.getSpielNumber();
        Predicate<Spiel> p3 = g -> g.getSpielNumber() == 100;

        // when
        Competition foundComp = compRepo.findByName(testComp.getName()).orElse(null);
        assertNotNull(foundComp);
        for (CompetitionRound round : foundComp.getCompetitionRounds()) {
            Set<Spieltag> spieltage = round.getSpieltage();
            for(Spieltag sp : spieltage) {

                Set<Spiel> found= sp.getSpiele();
                   assertNotNull(found);
                   assertTrue(found.stream().anyMatch(p1));
                   assertTrue(found.stream().anyMatch(p2));
                   assertTrue(found.stream().noneMatch(p3));
               }


        }

    }
}

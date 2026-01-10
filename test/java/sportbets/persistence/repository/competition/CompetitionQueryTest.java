package sportbets.persistence.repository.competition;

import jakarta.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.rowObject.CompRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionQueryTest {

    public static final String TEST_LIGA = "TestLiga";
    public static final String TEST_LIGA_SAISON_2025_26 = "TestLiga: Saison 2025/26";
    private static final Logger log = LoggerFactory.getLogger(CompetitionQueryTest.class);

    private CompetitionFamily testFamily;
    private Competition testComp;

    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    private CompetitionRoundRepository roundRepo;

    @Autowired
    private CompetitionFamilyRepository familyRepo;

    @Before
    public void setUp() {
        // Initialize test data before test methods
        testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition(TEST_LIGA_SAISON_2025_26, "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);

    }

    @Test
    public void findByName() {

        CompRecord comp = compRepo.findCompByNameAndFamily(TEST_LIGA_SAISON_2025_26, testFamily.getId());
        assertNotNull(comp);


    }

    @Test
    public void findByIdJoinFetchRounds() {
        Competition foundComp = compRepo.findByName(testComp.getName()).orElseThrow();

        Competition comp = compRepo.findByIdJoinFetchRounds(foundComp.getId());
        assertNotNull(comp);
        System.out.println("Found competition name: " + comp.getName());
        comp.getCompetitionRounds().forEach(compRound -> {

            log.debug("Found competitionRound name: {}", compRound.getName());
        });
    }


    @Test
    public void findByNameJoinFetchRounds() {

        Competition comp = compRepo.findByNameJoinFetchRounds(TEST_LIGA_SAISON_2025_26).orElseThrow(EntityNotFoundException::new);
        assertNotNull(comp);

        comp.getCompetitionRounds().forEach(compRound -> {

            log.debug("Found competitionRound name: {}", compRound.getName());
            compRound.getSpieltage().forEach(spieltag -> System.out.println("Found spieltag name: " + spieltag.getSpieltagNumber()));
        });
    }


    @Test
    public void findTeamsForCompetition() {
        System.out.println("\n");
        System.out.println("findTeamsForCompetition");
        List<Team> teams = compRepo.findTeamsForComp(testComp.getId());
        assertNotNull(teams);
        teams.forEach(team -> {
            log.debug("Found team name: {}", team);
        });
    }
}

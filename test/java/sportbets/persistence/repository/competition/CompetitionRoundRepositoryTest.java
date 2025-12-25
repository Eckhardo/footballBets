package sportbets.persistence.repository.competition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionRoundRepositoryTest {

    private CompetitionFamily testFamily;
    private Competition testComp;


    private CompetitionRound testRound;
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
        testComp = new Competition("TestLiga: Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);
        //  competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenCompsAndRoundsAreFound() {
       Competition foundComp = compRepo.findByName(testComp.getName()).orElse(null);

        assertNotNull(foundComp);
        assertEquals(testComp.getName(), foundComp.getName());
        assertFalse(foundComp.getCompetitionRounds().isEmpty());
        assertEquals(testComp.getCompetitionRounds().size(), foundComp.getCompetitionRounds().size());

    }

    @Test
    public void whenFindAll_thenCheckWithAllAnyMatchers() {
        // given
        Predicate<CompetitionRound> p1 = g -> g.getName().equals(testRound.getName());
        Predicate<CompetitionRound> p2 = g -> g.getName().equals("Finale");

        // when
        List<CompetitionRound> found = roundRepo.findAll();

        // then
        assertNotNull(found);
        assertTrue(found.stream().anyMatch(p1));
        assertTrue(found.stream().noneMatch(p2));
    }

    @Test
    public void whenFindAllFormComp_thenCheckWithAllAnyMatchers() {
        // given
        Predicate<CompetitionRound> p1 = g -> g.getName().equals(testRound.getName());
        Predicate<CompetitionRound> p2 = g -> g.getName().equals("Finale");

        // when
        List<CompetitionRound> found = compRepo.findAllForComp(testComp.getId());

        // then
        assertNotNull(found);
        assertTrue(found.stream().anyMatch(p1));
        assertTrue(found.stream().noneMatch(p2));
    }
}

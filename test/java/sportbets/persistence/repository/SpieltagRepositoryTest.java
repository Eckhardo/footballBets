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

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpieltagRepositoryTest {

    private CompetitionFamily testFamily;
    private Competition testComp;


    private CompetitionRound testRound;

    private CompetitionGroup testGroup;
    private Spieltag testSpieltag;
    private Spieltag testSpieltag2;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRoundRepository compRoundRepo;
    @Autowired
    private SpieltagRepository spieltagRepo;


    @Before
    public void setUp() {
        // Initialize test data before test methods
        testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        testRound = new CompetitionRound(1, "Vorrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        testSpieltag = new Spieltag(1, new Date(), testRound);
        testSpieltag2 = new Spieltag(2, new Date(), testRound);
        testRound.addSpieltag(testSpieltag);
        testRound.addSpieltag(testSpieltag2);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);
        //  competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }

    @Test
    public void givenRound_whenFindByNameCalled_thenSpieltagIsFound() {
        CompetitionRound foundRound = compRoundRepo.findByName(testRound.getName()).orElse(null);

        assertNotNull(foundRound);

        Spieltag sp = spieltagRepo.findByNumberWithRoundId(1, foundRound.getId()).orElseThrow(() -> {
            return new RuntimeException("Spieltag not found");
        });
        assertNotNull(sp);
    }


    @Test
    public void whenFindAllForRoundIsCalled_thenMatchDaysAreFound() {
        // given
        Predicate<Spieltag> p1 = g -> g.getSpieltagNumber() == 1;
        Predicate<Spieltag> p2 = g -> g.getSpieltagNumber() == 100;

        CompetitionRound foundRound = compRoundRepo.findByName(testRound.getName()).orElse(null);

        assertNotNull(foundRound);

        List<Spieltag> sp = spieltagRepo.findAllByRoundId(foundRound.getId());
        assertNotNull(sp);
        assertEquals(2,sp.size());


        // then

    }
}

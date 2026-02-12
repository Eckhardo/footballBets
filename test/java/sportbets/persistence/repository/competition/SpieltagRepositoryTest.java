package sportbets.persistence.repository.competition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.common.Country;
import sportbets.persistence.entity.competition.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpieltagRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SpieltagRepositoryTest.class);
    private Competition testComp;
    private CompetitionRound testRound;
    private CompetitionGroup testGroup;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;

    @Autowired
    private CompetitionRoundRepository compRoundRepo;
    @Autowired
    private SpieltagRepository spieltagRepo;


    @Before
    public void setUp() {
        // Initialize test data before test methods
        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true,   Country.GERMANY);
        testComp = new Competition("Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        testRound = new CompetitionRound(1, "Vorrunde", testComp, false, 18, 17);
        testComp.addCompetitionRound(testRound);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        Spieltag testSpieltag2 = new Spieltag(2, LocalDateTime.now(), testRound);
        testRound.addSpieltag(testSpieltag);
        testRound.addSpieltag(testSpieltag2);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);
        //  competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {


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

        Competition foundRound = compRepo.findByName(testComp.getName()).orElse(null);

        assertNotNull(foundRound);

        List<Spieltag> sp = spieltagRepo.findAllByCompId(foundRound.getId());
        assertNotNull(sp);
        assertEquals(2, sp.size());

    }

    @Test
    public void whenFindMaxMatchdayForRoundIsCalled_thenMaxMatchDayIsFound() {
         Competition foundRound = compRepo.findById(1L).orElse(null);

        assertNotNull(foundRound);

      Optional<Integer> maxMatchdays = spieltagRepo.findLastMatchdayForRound(foundRound.getId());
      if (maxMatchdays.isPresent()){
          log.info(maxMatchdays.get().toString());
          assertNotNull(maxMatchdays.get());
          assertEquals(17, maxMatchdays.get());
      }
      else{
          throw new RuntimeException("Spieltag not found");
      }



        // then

    }
}

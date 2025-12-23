package sportbets.persistence.repository.community;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest()
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TipperRepositoryTest {

    private CompetitionFamily testFamily;
    private Competition testComp;

    private Tipper testTipper;

    @Autowired
    private TipperRepository tipperRepo;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;
    @Before
   public void setUp() {
        testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("TestLiga: Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        familyRepo.save(testFamily);
        Competition savedComp= compRepo.findByName(testComp.getName()).orElseThrow();

        testTipper = new Tipper("Eckhard", "Kirschning", "Eckhardo", "root", "hint", "eki@gmx.de",savedComp.getId() );
        testTipper = tipperRepo.save(testTipper);
    }

    @After
   public void tearDown() {
    }

    @Test
   public void findByUsername() {
        Tipper tipper= tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);

    }

    @Test
 public   void deleteByUsername() {
    }
}
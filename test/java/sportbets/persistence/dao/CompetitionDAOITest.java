package sportbets.persistence.dao;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sportbets.persistence.builder.GenericBuilder;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;

import java.util.List;

@SpringBootTest
public class CompetitionDAOITest {

    private static final Logger log = LoggerFactory.getLogger(CompetitionDAOITest.class);
    @Autowired
    private CompetitionDAO competitionDAO;

    @Autowired
    private CompetitionFamilyDAO familyDAO;


    private CompetitionFamily testFamily;
    private Competition testComp;


  //  @Before
    public void setUp() {
        // Initialize test data before test methods
        testFamily = GenericBuilder.of(CompetitionFamily::new)
                .with(CompetitionFamily::setName, "3. Bundesliga")
                .with(CompetitionFamily::setDescription, "3. Deutsche Fussball Bundesliga")
                .with(CompetitionFamily::setHasClubs, true).with(CompetitionFamily::setHasLigaModus, true)
                .build();

        testComp = GenericBuilder.of(Competition::new)
                .with(Competition::setName, "Saison 2025")
                .with(Competition::setDescription, "3. Deutsche Fussball Bundesliga Saison 2025")
                .with(Competition::setRemisMultiplicator, 1).with(Competition::setWinMultiplicator, 3)
                .with(Competition::setCompetitionFamily, testFamily)
                .build();
        testFamily.addCompetition(testComp);
        log.info("Save all cascade");
        this.familyDAO.save(testFamily);
        this.competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {

        //    competitionDAO.deleteAll();
        //    familyDAO.deleteAll();
    }


    @Test
    public void findById() {
    }

    @Test
    public void findAll() {
        System.out.println("findAll::");
        List<Competition> competitions = competitionDAO.findAll();
        competitions.forEach(System.out::println);
    }

    @Test
    public void findByName() {
        System.out.println("findByName::");
        List<Competition> comps = competitionDAO.findByName(testComp.getName());
        comps.forEach(System.out::println);
    }


    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void deleteAll() {
    }
}
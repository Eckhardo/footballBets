package sportbets.persistence.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sportbets.common.Country;
import sportbets.persistence.dao.impl.CompetitionDAOImpl;
import sportbets.persistence.dao.impl.CompetitionFamilyDAOImpl;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;

import java.util.List;

@DataJpaTest()
@Import({CompetitionFamilyDAOImpl.class, CompetitionDAOImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionDAOITest {

    private static final Logger log = LoggerFactory.getLogger(CompetitionDAOITest.class);
    @Autowired
    private CompetitionDAO competitionDAO;

    @Autowired
    private CompetitionFamilyDAO familyDAO;


    private CompetitionFamily testFamily;
    private Competition testComp;


    @BeforeEach
    public void setUp() {
        // Initialize test data before test methods
        testFamily = new CompetitionFamily("3. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true,   Country.GERMANY);
        testComp = new Competition("Saison 2025/26", "3. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily, 18, 17);

        testFamily.addCompetition(testComp);
        log.debug("Save all cascade");
        this.familyDAO.save(testFamily);
        this.competitionDAO.save(testComp);
    }

    @AfterEach
    public void tearDown() {

    }


    @Test
    public void findById() {
        System.out.println("findAll family::");
        List<CompetitionFamily> fams = this.familyDAO.findAll();
        fams.forEach(System.out::println);
    }

    @Test
    public void findAll() {
        System.out.println("findAll::");
        List<Competition> competitions = this.competitionDAO.findAll();
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
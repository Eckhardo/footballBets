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
import sportbets.common.Country;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionGroup;
import sportbets.persistence.entity.competition.CompetitionRound;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionGroupRepositoryTest {


    private CompetitionGroup testGroup;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionGroupRepository groupRepo;

    @Before
    public void setUp() {
        // Initialize test data before test methods
        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true,  Country.GERMANY);
        Competition testComp = new Competition("Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        CompetitionRound testRound = new CompetitionRound(1, "Vorrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        testGroup = new CompetitionGroup("Gruppe A", 1, testRound);
        testRound.addCompetitionGroup(testGroup);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);
        //  competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenGroupsAreFound() {
        CompetitionGroup foundGroup = groupRepo.findByName(testGroup.getName()).orElse(null);

        assertNotNull(foundGroup);
        assertEquals(testGroup.getName(), foundGroup.getName());

    }

    @Test
    public void whenFindAll_thenCheckWithAllAnyMatchers() {
        // given
        Predicate<CompetitionGroup> p1 = g -> g.getName().equals(testGroup.getName());
        Predicate<CompetitionGroup> p2 = g -> g.getName().equals("Gruppe B");

        // when
        List<CompetitionGroup> found = groupRepo.findAll();

        // then
        assertNotNull(found);
        assertTrue(found.stream().anyMatch(p1));
        assertTrue(found.stream().noneMatch(p2));
    }
}

package sportbets.persistence.repository;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.CompetitionGroup;
import sportbets.persistence.entity.CompetitionRound;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionGroupRepositoryTest {
    private CompetitionFamily testFamily;
    private Competition testComp;


    private CompetitionRound testRound;

    private CompetitionGroup testGroup;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionGroupRepository groupRepo;

    @Before
    public void setUp() {
        // Initialize test data before test methods
        testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("Saison 2005/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        testRound = new CompetitionRound(1, "Vorrunde", testComp, false);
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
        CompetitionFamily foundFamily = familyRepo.findByName("1. Bundesliga").orElse(null);

        assertNotNull(foundFamily);
        assertEquals("1. Bundesliga", foundFamily.getName());
        assertNotNull(foundFamily.getCompetitions());
        Set<Competition> comps = foundFamily.getCompetitions();
        for (Competition comp : comps) {
            for (CompetitionRound round : comp.getCompetitionRounds()) {
                Set<CompetitionGroup> groups = round.getCompetitionGroups();
                groups.forEach(System.out::println);
            }
        }
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

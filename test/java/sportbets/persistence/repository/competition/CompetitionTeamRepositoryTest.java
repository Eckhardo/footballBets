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
import sportbets.persistence.entity.competition.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionTeamRepositoryTest {

    private Competition testComp;


    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    private CompetitionTeamRepository compTeamRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Before
    public void setUp() {
        // Initialize test data before test methods

        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        CompetitionRound testRound = new CompetitionRound(1, "Vorrunde", testComp, false);
        testComp.addCompetitionRound(testRound);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        Spieltag testSpieltag2 = new Spieltag(2, LocalDateTime.now(), testRound);
        testRound.addSpieltag(testSpieltag);
        testRound.addSpieltag(testSpieltag2);
        System.out.println("Save all cascade");
        familyRepo.save(testFamily);
        Team team1 = new Team("Test1", "1");
        Team team2 = new Team("Test2", "2");
        Team team3 = new Team("Test3", "3");
        Team team4 = new Team("Test4", "4");
        teamRepo.save(team1);
        teamRepo.save(team2);
        teamRepo.save(team3);
        teamRepo.save(team4);

        //  testGroup = new CompetitionGroup("Gruppe A", 1, testRound);

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

        compTeamRepo.saveAll(List.of(ct1, ct2, ct3, ct4));

        //  competitionDAO.save(testComp);
    }

    @After
    public void tearDown() {


        //     familyRepo.deleteAll();
    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenGroupsAreFound() {
        Competition foundComp = compRepo.findByName(testComp.getName()).orElse(null);

        assertNotNull(foundComp);
        assertEquals(testComp.getName(), foundComp.getName());

        Set<CompetitionTeam> compTeams = foundComp.getCompetitionTeams();
        assertEquals(testComp.getCompetitionTeams().size(), compTeams.size());
    }

    @Test
    public void whenFindByNameCalled_thenGroupsAreFound() {

        // given
        Predicate<CompetitionTeam> p1 = g -> g.getTeam().getAcronym().equals("1");
        Predicate<CompetitionTeam> p2 = g -> g.getTeam().getAcronym().equals("2");
        Predicate<CompetitionTeam> p3 = g -> g.getTeam().getAcronym().equals("5");

        // when
        List<CompetitionTeam> found = compTeamRepo.findAll();

        // then
        assertNotNull(found);
        assertTrue(found.stream().anyMatch(p1));
        assertTrue(found.stream().anyMatch(p2));
        assertTrue(found.stream().noneMatch(p3));

    }
}

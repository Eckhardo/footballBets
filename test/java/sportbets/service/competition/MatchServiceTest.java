package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MatchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    Team savedTeam1 = null;
    Team savedTeam2 = null;
    Spieltag savedMatchday = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;
    @Autowired
    private TeamService teamService;

    @Autowired
    private SpieltagService spieltagService;

    @Autowired
    private SpielService matchService;

    @BeforeEach
    public void setup() {
        CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "2. Deutsche Fussball TestLiga", true, true);

        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        Competition competition = new Competition(TEST_COMP, "Description of Competition", 3, 1, savedFam);

        Competition savedComp = compService.save(competition);
        CompetitionRound compRound = new CompetitionRound(1, TEST_COMP_ROUND, savedComp, false);
        CompetitionRound savedCompRound = compRoundService.save(compRound);
        Spieltag matchDayDto = new Spieltag(1, LocalDateTime.now(), savedCompRound);
        savedMatchday = spieltagService.save(matchDayDto);
        Team team = new Team(TEAM_NAME, "Braunschweig");
        Team team1 = new Team(TEAM_NAME_2, "Kiel");
        savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);

    }

    @AfterEach
    public void tearDown() {
        log.info("\n");
        log.info("Delete All Test data");

        familyService.deleteByName(TEST_COMP_FAM);
        compService.deleteByName(TEST_COMP);
        compRoundService.deleteByName(TEST_COMP_ROUND);
        spieltagService.deleteById(savedMatchday.getId());
        teamService.deleteByName(TEAM_NAME);
        teamService.deleteByName(TEAM_NAME_2);

    }

    @Test
    void whenValidMatch_thenMatchShouldBeSaved() {
        Spiel testSpiel1 = new Spiel(savedMatchday, 1, LocalDateTime.now(), savedTeam1, savedTeam2, 3, 1, false);

        Spiel savedMatch = matchService.save(testSpiel1);
        assertThat(savedMatch.getId()).isNotNull();
        assertThat(savedMatch.getAnpfiffdate()).isEqualTo(testSpiel1.getAnpfiffdate());
        assertThat(savedMatch.getSpielNumber()).isEqualTo(testSpiel1.getSpielNumber());
        assertThat(savedMatch.getHeimTore()).isEqualTo(testSpiel1.getHeimTore());
        assertThat(savedMatch.getGastTore()).isEqualTo(testSpiel1.getGastTore());

        assertThat(savedMatch.getSpieltag().getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
        assertThat(savedMatch.getSpieltag().getId()).isEqualTo(savedMatchday.getId());
        assertThat(savedMatch.getHeimTeam().getId()).isEqualTo(savedTeam1.getId());
        assertThat(savedMatch.getHeimTeam().getAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(savedMatch.getGastTeam().getId()).isEqualTo(savedTeam2.getId());
        assertThat(savedMatch.getGastTeam().getAcronym()).isEqualTo(savedTeam2.getAcronym());

        matchService.deleteById(savedMatch.getId());
    }

    @Test
    void whenValidMatch_thenMatchShouldBeUpdated() {
        Spiel testSpiel1 = new Spiel(savedMatchday, 1, LocalDateTime.now(), savedTeam1, savedTeam2, 3, 1, false);
        Spiel testSpiel2 = new Spiel(savedMatchday, 2, LocalDateTime.now(), savedTeam2, savedTeam1, 3, 1, false);
        Spiel savedMatch = matchService.save(testSpiel1);
        Spiel savedMatch2 = matchService.save(testSpiel2);
        // set gast team to same value as heimteam
        savedMatch.setGastTeam(savedMatch2.getGastTeam());
        savedMatch.setGastTore(4);
        Spiel updatedMatch = matchService.updateSpiel(savedMatch.getId(), savedMatch).orElseThrow();

        assertThat(updatedMatch.getId()).isNotNull();
        assertThat(updatedMatch.getAnpfiffdate()).isEqualTo(testSpiel1.getAnpfiffdate());
        assertThat(updatedMatch.getSpielNumber()).isEqualTo(testSpiel1.getSpielNumber());
        assertThat(updatedMatch.getHeimTore()).isEqualTo(testSpiel1.getHeimTore());
        assertThat(updatedMatch.getGastTore()).isEqualTo(4);

        assertThat(updatedMatch.getHeimTeam().getId()).isEqualTo(savedTeam1.getId());
        assertThat(updatedMatch.getHeimTeam().getAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(updatedMatch.getGastTeam().getId()).isEqualTo(savedTeam1.getId());
        assertThat(updatedMatch.getGastTeam().getAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(updatedMatch.getSpieltag().getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
        assertThat(updatedMatch.getSpieltag().getId()).isEqualTo(savedMatchday.getId());

        matchService.deleteById(savedMatch.getId());
        matchService.deleteById(savedMatch2.getId());
    }

    @Test
    void whenValidMatchDay_thenMatchesShouldBeRetrieved() {

        Spiel testSpiel1 = new Spiel(savedMatchday, 1, LocalDateTime.now(), savedTeam1, savedTeam2, 3, 1, false);
        Spiel testSpiel2 = new Spiel(savedMatchday, 2, LocalDateTime.now(), savedTeam2, savedTeam1, 3, 1, false);
        Spiel savedMatch = matchService.save(testSpiel1);
        Spiel savedMatch2 = matchService.save(testSpiel2);
        List<Spiel> spiels = matchService.getAllForMatchday(savedMatchday.getId());
        assertThat(spiels.size()).isEqualTo(2);
        for (Spiel entity : spiels) {
            assertThat(entity.getId()).isNotNull();
            assertThat(entity.getAnpfiffdate()).isNotNull();
            assertThat(entity.getSpielNumber()).isIn(savedMatch.getSpielNumber(), savedMatch2.getSpielNumber());
            assertThat(entity.getHeimTore()).isIn(savedMatch.getHeimTore(), savedMatch2.getHeimTore());
            assertThat(entity.getGastTore()).isIn(savedMatch.getGastTore(), savedMatch2.getGastTore());

            assertThat(entity.getSpieltag().getSpieltagNumber()).isIn(savedMatchday.getSpieltagNumber());
            assertThat(entity.getSpieltag().getId()).isIn(savedMatchday.getId());
            assertThat(entity.getHeimTeam().getId()).isIn(savedTeam1.getId(), savedTeam2.getId());
            assertThat(entity.getHeimTeam().getAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
            assertThat(entity.getGastTeam().getId()).isIn(savedTeam2.getId(), savedTeam1.getId());
            assertThat(entity.getGastTeam().getAcronym()).isIn(savedTeam2.getAcronym(), savedTeam1.getAcronym());
            assertThat(savedMatch.getHeimTeam().getId()).isEqualTo(savedTeam1.getId());


            matchService.deleteById(entity.getId());
        }
    }

}

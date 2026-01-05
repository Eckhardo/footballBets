package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.*;
import sportbets.web.dto.competition.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
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
       CompetitionFamilyDto competitionFamily = new CompetitionFamilyDto(null, TEST_COMP_FAM, "description of testliga", true, true);

        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        Competition savedComp = compService.save(compDto);
        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false, savedComp.getId(), savedComp.getName());

        compRoundDto.setCompId(savedComp.getId());
        CompetitionRound savedCompRound = compRoundService.save(compRoundDto);
        SpieltagDto matchDayDto = new SpieltagDto(null, 5, LocalDateTime.now(),savedCompRound.getId(),savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto);
        Team team = new Team(TEAM_NAME, "Braunschweig");
        Team team1 = new Team(TEAM_NAME_2, "Kiel");
        savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);

    }

    @AfterEach
    public void tearDown() {
        log.debug("\n");
        log.debug("Delete All Test data");


    }

    @Test
    void whenValidMatch_thenMatchShouldBeSaved() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());

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


    }

    @Test
    void whenValidMatch_thenMatchShouldBeUpdated() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
        Spiel savedMatch = matchService.save(testSpiel1);
        Spiel savedMatch2 = matchService.save(testSpiel2);
        // set gast team to same value as heimteam
         testSpiel1.setId(savedMatch.getId());
         testSpiel1.setGastTore(4);
        Spiel updatedMatch = matchService.updateSpiel(testSpiel1.getId(), testSpiel1).orElseThrow();

        assertThat(updatedMatch.getId()).isNotNull();
        assertThat(updatedMatch.getAnpfiffdate()).isEqualTo(testSpiel1.getAnpfiffdate());
        assertThat(updatedMatch.getSpielNumber()).isEqualTo(testSpiel1.getSpielNumber());
        assertThat(updatedMatch.getHeimTore()).isEqualTo(testSpiel1.getHeimTore());
        assertThat(updatedMatch.getGastTore()).isEqualTo(4);

        assertThat(updatedMatch.getHeimTeam().getId()).isEqualTo(savedTeam1.getId());
        assertThat(updatedMatch.getHeimTeam().getAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(updatedMatch.getGastTeam().getId()).isEqualTo(savedTeam2.getId());
        assertThat(updatedMatch.getGastTeam().getAcronym()).isEqualTo(savedTeam2.getAcronym());
        assertThat(updatedMatch.getSpieltag().getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
        assertThat(updatedMatch.getSpieltag().getId()).isEqualTo(savedMatchday.getId());

    }

    @Test
    void whenValidMatchDay_thenMatchesShouldBeRetrieved() {

        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
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

        }
    }

}

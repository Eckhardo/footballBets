package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.web.dto.competition.*;

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
    private static final int TEST_MATCH_DAY = 17;
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    TeamDto savedTeam1 = null;
    TeamDto savedTeam2 = null;
    SpieltagDto savedMatchday = null;
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
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        CompetitionDto savedComp = compService.save(compDto).orElseThrow();
        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false, savedComp.getId(), savedComp.getName());
        CompetitionRoundDto savedCompRound = compRoundService.save(compRoundDto).orElseThrow();
        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto).orElseThrow();
        TeamDto teamDto = new TeamDto(null, TEAM_NAME, "Braunschweig");
        TeamDto teamDto1 = new TeamDto(null, TEAM_NAME_2, "Kiel");
        savedTeam1 = teamService.save(teamDto).orElseThrow();
        savedTeam2 = teamService.save(teamDto1).orElseThrow();

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

        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto savedMatch = matchService.save(testSpiel1).orElseThrow();
        assertThat(savedMatch.getId()).isNotNull();
        assertThat(savedMatch.getAnpfiffdate()).isEqualTo(testSpiel1.getAnpfiffdate());
        assertThat(savedMatch.getSpielNumber()).isEqualTo(testSpiel1.getSpielNumber());
        assertThat(savedMatch.getHeimTore()).isEqualTo(testSpiel1.getHeimTore());
        assertThat(savedMatch.getGastTore()).isEqualTo(testSpiel1.getGastTore());

        assertThat(savedMatch.getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
        assertThat(savedMatch.getSpieltagId()).isEqualTo(savedMatchday.getId());
        assertThat(savedMatch.getHeimTeamId()).isEqualTo(savedTeam1.getId());
        assertThat(savedMatch.getHeimTeamAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(savedMatch.getGastTeamId()).isEqualTo(savedTeam2.getId());
        assertThat(savedMatch.getGastTeamAcronym()).isEqualTo(savedTeam2.getAcronym());

        matchService.deleteById(savedMatch.getId());
    }

    @Test
    void whenValidMatch_thenMatchShouldBeUpdated() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 3, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
        SpielDto savedMatch = matchService.save(testSpiel1).orElseThrow();
        SpielDto savedMatch2 = matchService.save(testSpiel2).orElseThrow();
        // set gast team to same value as heimteam
        savedMatch.setGastTeamId(savedMatch2.getGastTeamId());
        savedMatch.setGastTeamAcronym(savedMatch2.getGastTeamAcronym());
        savedMatch.setGastTore(4);
        SpielDto updatedMatch = matchService.updateSpiel(savedMatch.getId(), savedMatch).orElseThrow();

        assertThat(updatedMatch.getId()).isNotNull();
        assertThat(updatedMatch.getAnpfiffdate()).isEqualTo(testSpiel1.getAnpfiffdate());
        assertThat(updatedMatch.getSpielNumber()).isEqualTo(testSpiel1.getSpielNumber());
        assertThat(updatedMatch.getHeimTore()).isEqualTo(testSpiel1.getHeimTore());
        assertThat(updatedMatch.getGastTore()).isEqualTo(4);

        assertThat(updatedMatch.getHeimTeamId()).isEqualTo(savedTeam1.getId());
        assertThat(updatedMatch.getHeimTeamAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(updatedMatch.getGastTeamId()).isEqualTo(savedTeam1.getId());
        assertThat(updatedMatch.getGastTeamAcronym()).isEqualTo(savedTeam1.getAcronym());
        assertThat(updatedMatch.getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
        assertThat(updatedMatch.getSpieltagId()).isEqualTo(savedMatchday.getId());

        matchService.deleteById(savedMatch.getId());
        matchService.deleteById(savedMatch2.getId());
    }

    @Test
    void whenValidMatchDay_thenMatchesShouldBeRetrieved() {

        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 3, false, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
        SpielDto savedMatch = matchService.save(testSpiel1).orElseThrow();
        SpielDto savedMatch2 = matchService.save(testSpiel2).orElseThrow();
        List<SpielDto> spielDtos = matchService.getAllForMatchday(savedMatchday.getId());
        assertThat(spielDtos.size()).isEqualTo(2);
        for (SpielDto spielDto : spielDtos) {
            assertThat(spielDto.getId()).isNotNull();
            assertThat(spielDto.getAnpfiffdate()).isNotNull();
            assertThat(spielDto.getSpielNumber()).isIn(savedMatch.getSpielNumber(), savedMatch2.getSpielNumber());
            assertThat(spielDto.getHeimTore()).isIn(savedMatch.getHeimTore(), savedMatch2.getHeimTore());
            assertThat(spielDto.getGastTore()).isIn(savedMatch.getGastTore(), savedMatch2.getGastTore());

            assertThat(spielDto.getSpieltagNumber()).isIn(savedMatchday.getSpieltagNumber());
            assertThat(spielDto.getSpieltagId()).isIn(savedMatchday.getId());
            assertThat(spielDto.getHeimTeamId()).isIn(savedTeam1.getId(), savedTeam2.getId());
            assertThat(spielDto.getHeimTeamAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
            assertThat(spielDto.getGastTeamId()).isIn(savedTeam2.getId(), savedTeam1.getId());
            assertThat(spielDto.getGastTeamAcronym()).isIn(savedTeam2.getAcronym(), savedTeam1.getAcronym());

            matchService.deleteById(spielDto.getId());
        }
    }

}

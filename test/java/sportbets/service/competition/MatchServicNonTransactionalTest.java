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
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

public class MatchServicNonTransactionalTest {

    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    private static final Logger log = LoggerFactory.getLogger(MatchServicNonTransactionalTest.class);

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
    @Autowired
    private CompTableService compTableService;

    Competition savedComp = null;
    CompetitionRound savedCompRound = null;
    @BeforeEach
    public void setup() {
        CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
        CompetitionFamily savedFam = familyService.save(competitionFamily);
        CompetitionDto compDto = TestConstants.TEST_COMP;
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        CompetitionRoundDto compRoundDto = TestConstants.TEST_COMP_ROUND;
        compRoundDto.setCompId(savedComp.getId());
        log.info("save round " + compRoundDto.getCompId());
        savedCompRound = compRoundService.save(compRoundDto);
        log.info("round saved D: " + savedCompRound.getId());
        SpieltagDto matchDayDto = new SpieltagDto(null, 1, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        SpieltagDto matchDayDto2 = new SpieltagDto(null, 2, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto);
        Spieltag savedMatchday2 = spieltagService.save(matchDayDto2);
        Team team = new Team(TEAM_NAME, "Braunschweig", true);
        Team team1 = new Team(TEAM_NAME_2, "Kiel", true);
        savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);

    }

    @AfterEach
    public void tearDown() {
        log.debug("\n");
        log.debug("Delete All Test data");
        familyService.deleteByName(TestConstants.TEST_FAMILY.getName());
        teamService.deleteByName(TEAM_NAME);
        teamService.deleteByName(TEAM_NAME_2);


    }

    @Test
    void whenValidMatch_thenMatchShouldBeSaved() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());

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
        TableSearchCriteria searchCriteria = new TableSearchCriteria(savedComp.getId(), 1, 2, null);

        List<TeamPositionSummaryRow> rows = compTableService.findTableForLigaModus(searchCriteria);
        assertThat(rows.size()).isEqualTo(2);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));

    }

    @Test
    void whenValidMatch_thenMatchShouldBeUpdated() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
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
        TableSearchCriteria searchCriteria = new TableSearchCriteria(savedComp.getId(), 1, 2, null);

        List<TeamPositionSummaryRow> rows = compTableService.findTableForLigaModus(searchCriteria);
        assertThat(rows.size()).isEqualTo(2);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }

    @Test
    void whenValidMatchDay_thenMatchesShouldBeRetrieved() {

        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        SpielDto testSpiel2 = new SpielDto(null, 2, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam2.getId(), savedTeam2.getAcronym(), savedTeam1.getId(), savedTeam1.getAcronym());
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
        TableSearchCriteria searchCriteria = new TableSearchCriteria(savedComp.getId(), 1, 2, null);

        List<TeamPositionSummaryRow> rows = compTableService.findTableForLigaModus(searchCriteria);
        assertThat(rows.size()).isEqualTo(2);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }

    @Test
    void whenFamilyIsDeleted_thenMatchShouldBeDeleted() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());

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
        log.debug("\n");
        familyService.deleteByName(TestConstants.TEST_FAMILY.getName());
        Optional<Competition> deletedComp = compService.findByName(TestConstants.TEST_FAMILY.getName());
        assertThat(deletedComp.isEmpty());
        Optional<Spieltag> deletedMatchday = spieltagService.findById(savedMatchday.getId());
        assertThat(deletedMatchday.isEmpty());
        Optional<Spiel> deletedMatch = matchService.findById(savedMatch.getId());
        assertThat(deletedMatch.isEmpty());
        log.debug("\n");

    }

}

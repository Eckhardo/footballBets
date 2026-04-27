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
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.persistence.entity.competition.*;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sportbets.testdata.TestConstants.COMP_TEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

public class MatchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceTest.class);
    TeamDto savedTeam1 = null;
    TeamDto savedTeam2 = null;
    TeamDto team = TestConstants.TEAM_DTO_1;
    TeamDto team1 =TestConstants.TEAM_DTO_2;

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
        CompetitionDto compDto = new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, competitionFamily.getName());
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        CompetitionRoundDto compRoundDto =new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, compDto.getName(), 18, 17, 1);
        ;
        compRoundDto.setCompId(savedComp.getId());
        savedCompRound = compRoundService.save(compRoundDto);
        SpieltagDto matchDayDto = new SpieltagDto(null, 1, LocalDateTime.now(),savedCompRound.getId(),savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto);
         savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);

    }

    @AfterEach
    public void tearDown() {

        log.debug("Delete All Test data");
        familyService.deleteByName(TestConstants.TEST_FAMILY.getName());
        teamService.deleteByName(TestConstants.TEAM_1.getName() );
        teamService.deleteByName(TestConstants.TEAM_2.getName());


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

    }

}

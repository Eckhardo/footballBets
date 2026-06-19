package sportbets.service.competition;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.*;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.competition.batch.MatchBatchRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.testdata.TestConstants.createValidCompetitionDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceTest.class);
    TeamDto savedTeam1 = null;
    TeamDto savedTeam2 = null;
    TeamDto team = TestConstants.createValidTeamDto();
    TeamDto team1 = TestConstants.createValidTeamDto2();

    Spieltag savedMatchday = null;
    CompetitionFamilyDto competitionFamily = TestConstants.createValidFamilyDto();
    CompetitionDto compDto = createValidCompetitionDto();
    CompetitionRoundDto compRoundDto = TestConstants.createValidCompRoundDto();
    SpieltagDto spieltagDto = TestConstants.createValidSpieltagDto();
    Competition savedComp = null;
    CompetitionRound savedCompRound = null;
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

    @BeforeEach
    public void setup() {

        CompetitionFamily savedFam = familyService.save(competitionFamily);
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        compRoundDto.setCompId(savedComp.getId());
        savedCompRound = compRoundService.save(compRoundDto);
        spieltagDto.setCompRoundId(savedCompRound.getId());
        spieltagDto.setCompRoundName(savedCompRound.getName());
        savedMatchday = spieltagService.save(spieltagDto);
        savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);

    }

    @AfterEach
    public void tearDown() {

        log.debug("Delete All Test data");
        familyService.deleteByName(competitionFamily.getName());
        teamService.deleteByName(team.getName());
        teamService.deleteByName(team1.getName());


    }

    @Test
    @Order(1)
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
        SpielFormula heimFormula = savedMatch.getSpielFormulaForHeim().orElseThrow();
        assertNotNull(heimFormula);
        assertThat(1).isEqualTo(heimFormula.getWon());
        assertThat(0).isEqualTo(heimFormula.getRemis());
        assertThat(0).isEqualTo(heimFormula.getLost());
        assertThat(3).isEqualTo(heimFormula.getPoints());

        SpielFormula gastFormula = savedMatch.getSpielFormulaForGast().orElseThrow();
        assertThat(0).isEqualTo(gastFormula.getWon());
        assertThat(0).isEqualTo(gastFormula.getRemis());
        assertThat(1).isEqualTo(gastFormula.getLost());
        assertThat(0).isEqualTo(gastFormula.getPoints());

    }

    @Test
    @Order(2)
    void whenValidMatch_thenMatchShouldBeUpdated() {
        SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTeam1.getId(), savedTeam1.getAcronym(), savedTeam2.getId(), savedTeam2.getAcronym());
        Spiel savedMatch = matchService.save(testSpiel1);
        SpielFormula heimFormula = savedMatch.getSpielFormulaForHeim().orElseThrow();
        assertNotNull(heimFormula);
        assertThat(1).isEqualTo(heimFormula.getWon());
        assertThat(0).isEqualTo(heimFormula.getRemis());
        assertThat(0).isEqualTo(heimFormula.getLost());
        assertThat(3).isEqualTo(heimFormula.getPoints());

        SpielFormula gastFormula = savedMatch.getSpielFormulaForGast().orElseThrow();
        assertThat(0).isEqualTo(gastFormula.getWon());
        assertThat(0).isEqualTo(gastFormula.getRemis());
        assertThat(1).isEqualTo(gastFormula.getLost());
        assertThat(0).isEqualTo(gastFormula.getPoints());

        // set gast team to same value as heimteam
        testSpiel1.setId(savedMatch.getId());
        testSpiel1.setGastTore(4);
        Spiel updatedMatch = matchService.updateOne(testSpiel1.getId(), testSpiel1).orElseThrow();

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

        SpielFormula heimFormulaUpdate = updatedMatch.getSpielFormulaForHeim().orElseThrow();
        assertNotNull(heimFormula);
        assertThat(0).isEqualTo(heimFormulaUpdate.getWon());
        assertThat(0).isEqualTo(heimFormulaUpdate.getRemis());
        assertThat(1).isEqualTo(heimFormulaUpdate.getLost());
        assertThat(0).isEqualTo(heimFormulaUpdate.getPoints());

        SpielFormula gastFormulaUpdate = updatedMatch.getSpielFormulaForGast().orElseThrow();
        assertThat(1).isEqualTo(gastFormulaUpdate.getWon());
        assertThat(0).isEqualTo(gastFormulaUpdate.getRemis());
        assertThat(0).isEqualTo(gastFormulaUpdate.getLost());
        assertThat(3).isEqualTo(gastFormulaUpdate.getPoints());

    }

    @Test
    @Order(3)
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

    @Test
    @Order(4)
    void whenSaveInBatchForInitialization_thenMatchesShouldBeRetrieved() {
        compRoundDto.setCompId(savedComp.getId());
        compRoundDto.setId(savedCompRound.getId());
        compRoundDto.setMatchdaysSize(1);


        compRoundService.updateRound(compRoundDto.getId(), compRoundDto);

        MatchBatchRecord batch = new MatchBatchRecord(savedCompRound.getId(), savedTeam1.getId(), savedTeam2.getId());

        List<Spiel> spiele = matchService.saveBatch(batch);
        for (Spiel entity : spiele) {
            assertThat(entity.getId()).isNotNull();
            assertThat(entity.getAnpfiffdate()).isNotNull();
            assertThat(entity.getSpielNumber()).isIn(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertThat(entity.getHeimTore()).isEqualTo(0);
            assertThat(entity.getGastTore()).isEqualTo(0);
            assertThat(entity.getSpieltag().getSpieltagNumber()).isEqualTo(1);
            assertThat(entity.getGastTeam().getAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
            assertThat(entity.getHeimTeam().getAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
            SpielFormula heimFormula = entity.getSpielFormulaForHeim().orElseThrow();
            assertNotNull(heimFormula);
            assertThat(0).isEqualTo(heimFormula.getWon());
            assertThat(0).isEqualTo(heimFormula.getRemis());
            assertThat(0).isEqualTo(heimFormula.getLost());
            assertThat(0).isEqualTo(heimFormula.getPoints());
            assertThat(heimFormula.isHeimTeam()).isTrue();
            assertThat(heimFormula.getDiffTore()).isEqualTo(0);


            SpielFormula gastFormula = entity.getSpielFormulaForGast().orElseThrow();
            assertThat(0).isEqualTo(gastFormula.getWon());
            assertThat(0).isEqualTo(gastFormula.getRemis());
            assertThat(0).isEqualTo(gastFormula.getLost());
            assertThat(0).isEqualTo(gastFormula.getPoints());
            assertThat(gastFormula.isHeimTeam()).isFalse();
        }
    }

    @Test
    @Order(5)
    void whenSaveInBatch_thenMatchesShouldBeRetrieved() {
        compRoundDto.setCompId(savedComp.getId());
        compRoundDto.setId(savedCompRound.getId());
        compRoundDto.setMatchdaysSize(1);


        compRoundService.updateRound(compRoundDto.getId(), compRoundDto);

        MatchBatchRecord batch = new MatchBatchRecord(savedCompRound.getId(), savedTeam1.getId(), savedTeam2.getId());
        ModelMapper mapper = MapperUtil.getModelMapperForSpiel();

        List<SpielDto> dtos = new ArrayList<>();
        List<Spiel> spiele = matchService.saveBatch(batch);
        for (Spiel entity : spiele) {
            Random rand = new Random();
            // Generiert 0, 1, 2, 3 oder 4
            int randomNum = rand.nextInt(5);
            SpielDto spielDto = mapper.map(entity, SpielDto.class);
            spielDto.setHeimTore(2);
            spielDto.setGastTore(randomNum);
            spielDto.setStattgefunden(true);
            log.debug("SpielDto  {}", spielDto);
            dtos.add(spielDto);
        }
        List<Spiel> updateSpiele = matchService.updateList(savedMatchday.getId(), dtos);
        assertThat(updateSpiele.size()).isEqualTo(9);
        for (Spiel entity : updateSpiele) {
            assertThat(entity.getHeimTore()).isEqualTo(2);
            assertThat(entity.getGastTore()).isIn(0, 1, 2, 3, 4);
            SpielFormula heimFormula = entity.getSpielFormulaForHeim().orElseThrow();
            assertNotNull(heimFormula);
            assertThat(heimFormula.isHeimTeam()).isTrue();
            assertThat(heimFormula.getDiffTore()).isIn(-1, -2, -3, -4, 0, 1, 2, 3, 4);
            assertThat(heimFormula.getHeimTore()).isIn(2);
            assertThat(heimFormula.getGastTore()).isIn(0, 1, 2, 3, 4);
            assertThat(heimFormula.getTeamName()).isIn(savedTeam1.getName(), savedTeam2.getName());
            assertThat(heimFormula.getTeamNameAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());


            SpielFormula gastFormula = entity.getSpielFormulaForGast().orElseThrow();
            assertNotNull(gastFormula);
            assertThat(gastFormula.isHeimTeam()).isFalse();
            assertThat(gastFormula.getDiffTore()).isIn(-1, -2, -3, -4, 0, 1, 2, 3, 4);
            assertThat(gastFormula.getGastTore()).isIn(2);
            assertThat(gastFormula.getHeimTore()).isIn(0, 1, 2, 3, 4);
            assertThat(gastFormula.getTeamName()).isIn(savedTeam1.getName(), savedTeam2.getName());
            assertThat(gastFormula.getTeamNameAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());

        }
    }
}

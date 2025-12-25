package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MatchdayServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchdayServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    private static final int TEST_MATCH_DAY = 1;
    CompetitionRoundDto savedCompRound = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;

    @Autowired
    private SpieltagService spieltagService;

    @BeforeEach
    public void setup() {
        CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
        CompetitionFamilyDto savedFam = familyService.save(compFamilyDto).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        CompetitionDto savedComp = compService.save(compDto).orElseThrow();
        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false, savedComp.getId(), savedComp.getName());
        savedCompRound = compRoundService.save(compRoundDto).orElseThrow();

    }

    @AfterEach
    public void tearDown() {
        log.info("\n");
        log.info("Delete All Test data");

        familyService.deleteByName(TEST_COMP_FAM);
        compService.deleteByName(TEST_COMP);
        compRoundService.deleteByName(TEST_COMP_ROUND);
    }

    @Test
    void whenValidMatchday_thenMatchdayShouldBeSaved() {

        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        SpieltagDto savedMatchday = spieltagService.save(matchDayDto).orElseThrow();

        assertThat(savedMatchday.getId()).isNotNull();
        assertThat(savedMatchday.getSpieltagNumber()).isEqualTo(TEST_MATCH_DAY);
        assertThat(savedMatchday.getCompRoundName()).isEqualTo(savedCompRound.getName());
        assertThat(savedMatchday.getCompRoundId()).isEqualTo(savedCompRound.getId());
        assertThat(savedMatchday.getStartDate()).isEqualTo(matchDayDto.getStartDate());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidMatchday_thenMatchdayShouldBeUpdated() {

        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        SpieltagDto savedMatchday = spieltagService.save(matchDayDto).orElseThrow();
        savedMatchday.setSpieltagNumber(5);
        SpieltagDto updatedMatchday = spieltagService.updateMatchDay(savedMatchday.getId(), savedMatchday).orElseThrow();

        assertThat(updatedMatchday.getId()).isNotNull();
        assertThat(updatedMatchday.getSpieltagNumber()).isEqualTo(5);
        assertThat(updatedMatchday.getStartDate()).isEqualTo(matchDayDto.getStartDate());
        assertThat(updatedMatchday.getCompRoundName()).isEqualTo(savedCompRound.getName());
        assertThat(updatedMatchday.getCompRoundId()).isEqualTo(savedCompRound.getId());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidRound_thenAllMatchdaysShouldRetrieved() {

        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        SpieltagDto savedMatchday = spieltagService.save(matchDayDto).orElseThrow();

        List<SpieltagDto> matchdays = spieltagService.getAllForRound(savedCompRound.getId());
        assertThat(matchdays.size()).isEqualTo(1);
        for (SpieltagDto matchday : matchdays) {
            assertThat(matchday.getId()).isNotNull();
            assertThat(matchday.getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
            assertThat(matchday.getStartDate()).isNotNull();
            assertThat(matchday.getCompRoundName()).isEqualTo(savedCompRound.getName());
            assertThat(matchday.getCompRoundId()).isEqualTo(savedCompRound.getId());
            spieltagService.deleteById(matchday.getId());
        }
    }
}

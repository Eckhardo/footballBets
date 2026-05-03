package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sportbets.testdata.TestConstants.COMP_TEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Rollback(true)
public class MatchdayServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchdayServiceTest.class);
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    private static final int TEST_MATCH_DAY = 1;
    final CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    Competition savedComp = null;
    CompetitionRound savedCompRound = null;
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
        log.debug("set up Test data");
        CompetitionFamily savedFam = familyService.save(competitionFamily);
        CompetitionDto compDto = new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, competitionFamily.getName());
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        log.debug("set up Test data: saved competition ");
        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, compDto.getName(), 18, 17, 1);
        compRoundDto.setCompId(savedComp.getId());
        savedCompRound = compRoundService.save(compRoundDto);
        log.debug("set up Test data: saved competitionRound ");


    }

    @AfterEach
    public void tearDown() {

        log.debug("Delete All Test data");
        familyService.deleteByName(TestConstants.TEST_FAMILY.getName());
    }

    @Test
    void whenValidMatchday_thenMatchdayShouldBeSaved() {
        log.debug("whenValidMatchday_thenMatchdayShouldBeSaved");
        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        Spieltag savedMatchday = spieltagService.save(matchDayDto);

        assertThat(savedMatchday.getId()).isNotNull();
        assertThat(savedMatchday.getSpieltagNumber()).isEqualTo(TEST_MATCH_DAY);
        assertThat(savedMatchday.getCompetitionRound().getName()).isEqualTo(savedCompRound.getName());
        assertThat(savedMatchday.getCompetitionRound().getId()).isEqualTo(savedCompRound.getId());
        assertThat(savedMatchday.getStartDate()).isEqualTo(matchDayDto.getStartDate());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidMatchday_thenMatchdayShouldBeUpdated() {


        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        Spieltag savedMatchday = spieltagService.save(matchDayDto);
        matchDayDto.setId(savedMatchday.getId());
        matchDayDto.setSpieltagNumber(5);
        Spieltag updatedMatchday = spieltagService.updateMatchDay(savedMatchday.getId(), matchDayDto).orElseThrow();

        assertThat(updatedMatchday.getId()).isNotNull();
        assertThat(updatedMatchday.getSpieltagNumber()).isEqualTo(5);
        assertThat(updatedMatchday.getStartDate()).isEqualTo(matchDayDto.getStartDate());
        assertThat(updatedMatchday.getCompetitionRound().getName()).isEqualTo(savedCompRound.getName());
        assertThat(updatedMatchday.getCompetitionRound().getId()).isEqualTo(savedCompRound.getId());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidRound_thenAllMatchdaysShouldRetrieved() {
        SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        Spieltag savedMatchday = spieltagService.save(matchDayDto);

        List<Spieltag> matchdays = spieltagService.getAllForCompetition(savedComp.getId());
        assertThat(matchdays.size()).isEqualTo(1);
        for (Spieltag matchday : matchdays) {
            assertThat(matchday.getId()).isNotNull();
            assertThat(matchday.getSpieltagNumber()).isEqualTo(savedMatchday.getSpieltagNumber());
            assertThat(matchday.getStartDate()).isNotNull();
            assertThat(matchday.getCompetitionRound().getName()).isEqualTo(savedCompRound.getName());
            assertThat(matchday.getCompetitionRound().getId()).isEqualTo(savedCompRound.getId());
            spieltagService.deleteById(matchday.getId());
        }
    }
}

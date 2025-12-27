package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
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
        CompetitionFamily competitionFamily =  new CompetitionFamily(TEST_COMP_FAM, "Description of test liga", true, true);
        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        Competition competition = new Competition( TEST_COMP, "Description of Competition", 3, 1, savedFam);

        Competition savedComp = compService.save(competition);
        CompetitionRound compRound =  new CompetitionRound(1, TEST_COMP_ROUND, savedComp, false);
        savedCompRound = compRoundService.save(compRound);

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

        Spieltag matchDay =  new Spieltag(1, LocalDateTime.now(), savedCompRound);
        Spieltag savedMatchday = spieltagService.save(matchDay);

        assertThat(savedMatchday.getId()).isNotNull();
        assertThat(savedMatchday.getSpieltagNumber()).isEqualTo(TEST_MATCH_DAY);
        assertThat(savedMatchday.getCompetitionRound().getName()).isEqualTo(savedCompRound.getName());
        assertThat(savedMatchday.getCompetitionRound().getId()).isEqualTo(savedCompRound.getId());
        assertThat(savedMatchday.getStartDate()).isEqualTo(matchDay.getStartDate());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidMatchday_thenMatchdayShouldBeUpdated() {

        Spieltag matchDay = new Spieltag(1, LocalDateTime.now(), savedCompRound);
        Spieltag savedMatchday = spieltagService.save(matchDay);
        savedMatchday.setSpieltagNumber(5);
        Spieltag updatedMatchday = spieltagService.updateMatchDay(savedMatchday.getId(), savedMatchday).orElseThrow();

        assertThat(updatedMatchday.getId()).isNotNull();
        assertThat(updatedMatchday.getSpieltagNumber()).isEqualTo(5);
        assertThat(updatedMatchday.getStartDate()).isEqualTo(matchDay.getStartDate());
        assertThat(updatedMatchday.getCompetitionRound().getName()).isEqualTo(savedCompRound.getName());
        assertThat(updatedMatchday.getCompetitionRound().getId()).isEqualTo(savedCompRound.getId());
        spieltagService.deleteById(savedMatchday.getId());
    }

    @Test
    void whenValidRound_thenAllMatchdaysShouldRetrieved() {

        Spieltag matchDay =  new Spieltag(1, LocalDateTime.now(), savedCompRound);
        Spieltag savedMatchday = spieltagService.save(matchDay);

        List<Spieltag> matchdays = spieltagService.getAllForRound(savedCompRound.getId());
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

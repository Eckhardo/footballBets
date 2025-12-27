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
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CompRoundServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CompRoundServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "description of testliga", true, true);
    Competition savedComp = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;

    @BeforeEach
    public void setup() {

        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        Competition competition = new Competition( TEST_COMP, "Description of Competition", 3, 1, savedFam);

        savedComp = compService.save(competition);
    }

    @AfterEach
    public void tearDown() {
        log.info("\n");
        log.info("Delete All Test data");
        familyService.deleteByName(TEST_COMP_FAM);
        //compService.deleteByName(TEST_COMP);
        //  compRoundService.deleteByName(TEST_COMP_ROUND);
    }

    @Test
    void whenValidCompRound_thenCompRoundShouldBeSaved() {

        CompetitionRound compRound =new CompetitionRound(1, TEST_COMP_ROUND, savedComp, false);
        CompetitionRound savedCompRound = compRoundService.save(compRound);

            assertThat(savedCompRound.getId()).isNotNull();
            assertThat(savedCompRound.getName()).isEqualTo(TEST_COMP_ROUND);
            assertThat(savedCompRound.getCompetition().getName()).isEqualTo(savedComp.getName());
            assertThat(savedCompRound.getCompetition().getId()).isEqualTo(savedComp.getId());
            // check for ref inegrity
            Competition compModel = compService.findByIdTest(savedCompRound.getCompetition().getId()).orElseThrow();
            assertThat(compModel.getCompetitionRounds()).isNotNull();


    }

    @Test
    void whenValidCompRound_thenCompRoundShouldBeUpdated() {

        CompetitionRound compRound = new CompetitionRound(1, TEST_COMP_ROUND, savedComp, false);
        CompetitionRound savedRound = compRoundService.save(compRound);
        savedRound.setRoundNumber(2);
        CompetitionRound updatedCompRound = compRoundService.updateRound(savedRound.getId(), savedRound).orElseThrow();

        assertThat(updatedCompRound.getId()).isNotNull();
        assertThat(updatedCompRound.getName()).isEqualTo(TEST_COMP_ROUND);
        assertThat(updatedCompRound.getRoundNumber()).isEqualTo(2);
        assertThat(updatedCompRound.getCompetition().getName()).isEqualTo(savedComp.getName());
        assertThat(updatedCompRound.getCompetition().getId()).isEqualTo(savedComp.getId());

    }


}

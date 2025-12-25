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
    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    CompetitionDto savedComp = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;

    @BeforeEach
    public void setup() {

        CompetitionFamilyDto savedFam = familyService.save(compFamilyDto).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        savedComp = compService.save(compDto).orElseThrow();
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

        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false, savedComp.getId(), savedComp.getName());
        Optional<CompetitionRoundDto> savedCompRound = compRoundService.save(compRoundDto);
        if (savedCompRound.isPresent()) {
            assertThat(savedCompRound.get().getId()).isNotNull();
            assertThat(savedCompRound.get().getName()).isEqualTo(TEST_COMP_ROUND);
            assertThat(savedCompRound.get().getCompName()).isEqualTo(savedComp.getName());
            assertThat(savedCompRound.get().getCompId()).isEqualTo(savedComp.getId());
            // check for ref inegrity
            Competition compModel = compService.findByIdTest(savedCompRound.get().getCompId()).orElseThrow();
            assertThat(compModel.getCompetitionRounds()).isNotNull();

        }
    }

    @Test
    void whenValidCompRound_thenCompRoundShouldBeUpdated() {

        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false, savedComp.getId(), savedComp.getName());
        CompetitionRoundDto savedRound = compRoundService.save(compRoundDto).orElseThrow();
        savedRound.setRoundNumber(2);
        CompetitionRoundDto updatedCompRound = compRoundService.updateRound(savedRound.getId(), savedRound).orElseThrow();

        assertThat(updatedCompRound.getId()).isNotNull();
        assertThat(updatedCompRound.getName()).isEqualTo(TEST_COMP_ROUND);
        assertThat(updatedCompRound.getRoundNumber()).isEqualTo(2);
        assertThat(updatedCompRound.getCompName()).isEqualTo(savedComp.getName());
        assertThat(updatedCompRound.getCompId()).isEqualTo(savedComp.getId());

    }


}

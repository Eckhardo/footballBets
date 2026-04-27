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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sportbets.testdata.TestConstants.COMP_TEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class CompRoundServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CompRoundServiceTest.class);
    final CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    CompetitionDto compDto =  new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, competitionFamily.getName());
    CompetitionRoundDto compRoundDto = TestConstants.TEST_COMP_ROUND;

    Competition savedComp = null;

    CompetitionRound savedCompRound = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;

    @BeforeEach
    public void setup() {

        CompetitionFamily savedFam = familyService.save(competitionFamily);

        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
    }

    @AfterEach
    public void tearDown() {

        log.debug("Delete All Test data");
        familyService.deleteByName(competitionFamily.getName());

    }

    @Test
    void whenValidCompRound_thenCompRoundShouldBeSaved() {

        compRoundDto.setCompId(savedComp.getId());
        CompetitionRound savedCompRound = compRoundService.save(compRoundDto);

        assertThat(savedCompRound.getId()).isNotNull();
        assertThat(savedCompRound.getName()).isEqualTo(savedCompRound.getName());
        assertThat(savedCompRound.getCompetition().getName()).isEqualTo(savedComp.getName());
        assertThat(savedCompRound.getCompetition().getId()).isEqualTo(savedComp.getId());
        // check for ref inegrity
        Competition compModel = compService.findByIdTest(savedCompRound.getCompetition().getId()).orElseThrow();
        assertThat(compModel.getCompetitionRounds()).isNotNull();
    }

    @Test
    void whenValidCompRound_thenCompRoundShouldBeUpdated() {
        compRoundDto.setCompId(savedComp.getId());
        savedCompRound = compRoundService.save(compRoundDto);
        compRoundDto.setRoundNumber(2);
        compRoundDto.setId(savedCompRound.getId());
        CompetitionRound updatedCompRound = compRoundService.updateRound(compRoundDto.getId(), compRoundDto).orElseThrow();

        assertThat(updatedCompRound.getId()).isNotNull();
        assertThat(updatedCompRound.getName()).isEqualTo(savedCompRound.getName());
        assertThat(updatedCompRound.getRoundNumber()).isEqualTo(2);
        assertThat(updatedCompRound.getCompetition().getName()).isEqualTo(savedComp.getName());
        assertThat(updatedCompRound.getCompetition().getId()).isEqualTo(savedComp.getId());

    }
}
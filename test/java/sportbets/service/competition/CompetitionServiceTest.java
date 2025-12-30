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
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CompetitionServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CompetitionServiceTest.class);

    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";

    final CompetitionFamilyDto competitionFamily = new CompetitionFamilyDto(null, TEST_COMP_FAM, "description of testliga", true, true);
    CompetitionFamily savedFam = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompFamilyService compFamilyService;

    @BeforeEach
    public void setup() {
        log.info("setup");
        savedFam = familyService.save(competitionFamily).orElseThrow();
        log.info("saved family: {}", savedFam);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    void whenValidComp_thenCompShouldBeSaved() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        Competition savedComp = compService.save(compDto);

        assertThat(savedComp.getId()).isNotNull();
        assertThat(savedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(savedComp.getCompetitionFamily().getName()).isEqualTo(savedFam.getName());
        assertThat(savedComp.getCompetitionFamily().getId()).isEqualTo(savedFam.getId());



    }

    @Test
    void whenValidComp_thenCompShouldBeUpdated() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        Competition savedComp = compService.save(compDto);
        compDto.setWinMultiplicator(2);
        compDto.setId(savedComp.getId());
        Competition updatedComp = compService.updateComp(savedComp.getId(), compDto);

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(updatedComp.getWinMultiplicator()).isEqualTo(2);
        // assertThat(updatedComp.getCompetitionFamily()).isNotNull();

    }
}

package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class CompetitionServiceTest {

    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    final CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "description of testliga", true, true);
    CompetitionFamily savedFam = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompFamilyService compFamilyService;

    @BeforeEach
    public void setup() {

        savedFam = familyService.save(competitionFamily).orElseThrow();
    }

    @AfterEach
    public void tearDown() {
        compFamilyService.deleteByName(TEST_COMP_FAM);
        compService.deleteByName(TEST_COMP);
    }

    @Test
    void whenValidComp_thenCompShouldBeSaved() {

        Competition competition = new Competition(TEST_COMP, "Description of Competition", 3, 1, savedFam);
        Competition savedComp = compService.save(competition);

        assertThat(savedComp.getId()).isNotNull();
        assertThat(savedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(savedComp.getCompetitionFamily().getName()).isEqualTo(savedFam.getName());
        assertThat(savedComp.getCompetitionFamily().getId()).isEqualTo(savedFam.getId());
        CompetitionFamily famModel = familyService.findByIdTest(savedComp.getCompetitionFamily().getId()).orElseThrow();
        assertThat(famModel.getCompetitions()).isNotNull();


    }

    @Test
    void whenValidComp_thenCompShouldBeUpdated() {

        Competition compDto = new Competition(TEST_COMP, "Description of Competition", 3, 1, savedFam);
        Competition savedComp = compService.save(compDto);
        savedComp.setWinMultiplicator(2);
        Competition updatedComp = compService.updateComp(savedComp.getId(), savedComp);

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(updatedComp.getWinMultiplicator()).isEqualTo(2);
        // assertThat(updatedComp.getCompetitionFamily()).isNotNull();

    }
}

package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class CompetitionServiceTest {

    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    CompetitionFamilyDto savedFam = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompFamilyService compFamilyService;

    @BeforeEach
    public void setup() {

        savedFam = familyService.save(compFamilyDto).orElseThrow();
    }

    @AfterEach
    public void tearDown() {
        compFamilyService.deleteByName(TEST_COMP_FAM);
        compService.deleteByName(TEST_COMP);
    }

    @Test
    void whenValidComp_thenCompShouldBeSaved() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        Optional<CompetitionDto> savedComp = compService.save(compDto);
        if (savedComp.isPresent()) {
            assertThat(savedComp.get().getId()).isNotNull();
            assertThat(savedComp.get().getName()).isEqualTo(TEST_COMP);
            assertThat(savedComp.get().getFamilyName()).isEqualTo(savedFam.getName());
            assertThat(savedComp.get().getFamilyId()).isEqualTo(savedFam.getId());
            CompetitionFamily famModel = familyService.findByIdTest(savedComp.get().getFamilyId()).orElseThrow();
            assertThat(famModel.getCompetitions()).isNotNull();

        }


    }

    @Test
    void whenValidComp_thenCompShouldBeUpdated() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        CompetitionDto savedComp = compService.save(compDto).orElseThrow();
        savedComp.setWinMultiplicator(2);
        CompetitionDto updatedComp = compService.updateComp(savedComp.getId(), savedComp).orElseThrow();

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(updatedComp.getWinMultiplicator()).isEqualTo(2);
        assertThat(updatedComp.getFamilyName()).isEqualTo(savedFam.getName());
        assertThat(updatedComp.getFamilyId()).isEqualTo(savedFam.getId());

    }
}

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
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.service.authorization.CompetitionRoleService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CompetitionServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CompetitionServiceTest.class);

    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_2 = "TestLiga2: Saison 2025";

    final CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    CompetitionFamily savedFam = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompFamilyService compFamilyService;
    @Autowired
    private CompetitionRoleService competitionRoleService;

    @BeforeEach
    public void setup() {
        log.debug("setup");
        savedFam = familyService.save(competitionFamily).orElseThrow();
        log.debug("saved family: {}", savedFam);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    void whenValidComp_thenCompShouldBeSaved() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), savedFam.getName());
        Competition savedComp = compService.save(compDto);

        assertThat(savedComp.getId()).isNotNull();
        assertThat(savedComp.getName()).isEqualTo(TEST_COMP);
        assertThat(savedComp.getCompetitionFamily().getName()).isEqualTo(savedFam.getName());
        assertThat(savedComp.getCompetitionFamily().getId()).isEqualTo(savedFam.getId());



    }

    @Test
    void whenValidComp_thenCompShouldBeUpdated() {

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), savedFam.getName());
        Competition savedComp = compService.save(compDto);
        compDto.setName(TEST_COMP_2);
        compDto.setWinMultiplicator(5);
        compDto.setId(savedComp.getId());
        Competition updatedComp = compService.updateComp(savedComp.getId(), compDto).orElseThrow();

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo(TEST_COMP_2);
        assertThat(updatedComp.getWinMultiplicator()).isEqualTo(5);
        List<CompetitionRole> roles = competitionRoleService.getAllCompRoles();
        assertThat(roles).isNotNull();
        assertThat(roles.size()).isEqualTo(1);
        assertEquals(savedComp.getId(), roles.get(0).getCompetition().getId());
        assertEquals(savedComp.getName(), roles.get(0).getCompetition().getName());
        assertThat(savedComp.getName()).isEqualTo(roles.get(0).getName());
        // assertThat(updatedComp.getCompetitionFamily()).isNotNull();

    }
}

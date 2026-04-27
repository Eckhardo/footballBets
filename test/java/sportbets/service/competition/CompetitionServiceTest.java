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
import static sportbets.testdata.TestConstants.TEST_COMP_2;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

public class CompetitionServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CompetitionServiceTest.class);


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
        savedFam = familyService.save(competitionFamily);
        log.debug("saved family: {}", savedFam);
    }

    @AfterEach
    public void tearDown() {
     familyService.deleteByName(savedFam.getName());
    }

    @Test
    void whenValidComp_thenCompShouldBeSaved() {

        CompetitionDto compDto = TestConstants.TEST_COMP;
        compDto.setFamilyId(savedFam.getId());
        Competition savedComp = compService.save(compDto);

        assertThat(savedComp.getId()).isNotNull();
        assertThat(compDto.getName()).isEqualTo(savedComp.getName());
        assertThat(savedComp.getCompetitionFamily().getName()).isEqualTo(savedFam.getName());
        assertThat(savedComp.getCompetitionFamily().getId()).isEqualTo(savedFam.getId());


    }

    @Test
    void whenValidComp_thenCompShouldBeUpdated() {

        CompetitionDto compDto =TestConstants.TEST_COMP;
        compDto.setFamilyId(savedFam.getId());
        Competition savedComp = compService.save(compDto);
        compDto.setName(TEST_COMP_2.getName());
        compDto.setWinMultiplicator(5);
        compDto.setId(savedComp.getId());
        Competition updatedComp = compService.updateComp(savedComp.getId(), compDto).orElseThrow();

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(TEST_COMP_2.getName()).isEqualTo(updatedComp.getName());
        assertThat(5).isEqualTo(updatedComp.getWinMultiplicator());
        List<CompetitionRole> roles = competitionRoleService.getAllCompRoles();
        assertThat(roles).isNotNull();
        for (CompetitionRole role : roles) {
            System.out.println(role.toString());
        }
        CompetitionRole updatedRole = roles.stream().filter((r) -> r.getName().equals(TEST_COMP_2.getName())).findFirst().orElseThrow();
        assertThat(updatedRole).isNotNull();
        assertEquals(updatedComp.getId(), updatedRole.getCompetition().getId());
        assertThat(updatedComp.getName()).isEqualTo(updatedRole.getName());
        assertEquals(updatedComp.getName(), updatedRole.getCompetition().getName());

        // assertThat(updatedComp.getCompetitionFamily()).isNotNull();

    }
}

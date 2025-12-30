package sportbets.service.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.service.community.TipperService;
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.web.dto.authorization.CompetitionRoleDto;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CompetitionRoleServiceTest {


    private static final Logger log = LoggerFactory.getLogger(CompetitionRoleServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_USERNAME = "TEST_USER";
    final CompetitionFamilyDto competitionFamily = new CompetitionFamilyDto(null, TEST_COMP_FAM, "description of testliga", true, true);
    Competition savedComp = null;
    Tipper savedTipper = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompetitionRoleService competitionRoleService;

    @Autowired
    private TipperService tipperService;

    @BeforeEach
    public void setup() {

        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);

        savedComp = compService.save(compDto);
        assertNotNull(savedComp);
        TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "root", "hint", "eki@gmx.de", savedComp.getId());
        savedTipper = tipperService.save(testTipper);


    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void saveCompRole() {
        log.info("saveCompRole");
        CompetitionRole compRole = competitionRoleService.findByCompName(savedComp.getName()).orElseThrow();

        assertEquals(savedComp.getName(), compRole.getName());
        assertEquals(savedComp.getId(), compRole.getCompetition().getId());
        assertEquals(savedComp.getName(), compRole.getCompetition().getName());
        Competition myComp = compService.findByIdTest(savedComp.getId()).orElseThrow();
        assertThat(myComp.getCompetitionRoles()).isNotNull();
    }


    @Test
    public void findAllCompRoles() {
        log.info("findCompRole");

        List<CompetitionRole> roles = competitionRoleService.getAllCompRoles();
        assertThat(roles).isNotNull();
        assertThat(roles.size()).isEqualTo(1);
        assertEquals(savedComp.getId(), roles.get(0).getCompetition().getId());
        assertEquals(savedComp.getName(), roles.get(0).getCompetition().getName());
    }


}

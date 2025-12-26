package sportbets.service.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CompetitionRoleServiceTest {


    private static final Logger log = LoggerFactory.getLogger(CompetitionRoleServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_USERNAME = "TEST_USER";
    CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "description of testliga", true, true);
    Competition savedComp = null;
    TipperDto savedTipper = null;
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
        Competition competition =new Competition( TEST_COMP, "Description of Competition", 3, 1, savedFam);
        savedComp = compService.save(competition);
        assertNotNull(savedComp);
        TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "root", "hint", "eki@gmx.de", savedComp.getId());
        savedTipper = tipperService.save(testTipper).orElseThrow();


    }

    @AfterEach
    public void tearDown() {
        familyService.deleteByName(TEST_COMP_FAM);
        compService.deleteByName(TEST_COMP);
        tipperService.deleteByUserName(TEST_USERNAME);

    }

    @Test
    public void saveCompRole() {
        log.info("saveTipperRole");
        CompetitionRoleDto compRole = new CompetitionRoleDto(null, TEST_COMP, "", savedComp.getId(), savedComp.getName());
        CompetitionRoleDto savedRole = competitionRoleService.save(compRole);
        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertEquals(savedComp.getName(), compRole.getName());
        assertEquals(savedComp.getId(), compRole.getCompetitionId());
        assertEquals(savedComp.getName(), compRole.getCompetitionName());
        Competition myComp = compService.findByIdTest(savedComp.getId()).orElseThrow();
        assertThat(myComp.getCompetitionRoles()).isNotNull();
    }


    @Test
    public void findAllCompRoles() {
        log.info("saveTipperRole");
        CompetitionRoleDto compRole = new CompetitionRoleDto(null, TEST_COMP, "", savedComp.getId(), savedComp.getName());
        CompetitionRoleDto savedRole = competitionRoleService.save(compRole);
        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertEquals(savedComp.getName(), compRole.getName());
        assertEquals(savedComp.getId(), compRole.getCompetitionId());
        assertEquals(savedComp.getName(), compRole.getCompetitionName());
        List<CompetitionRoleDto> roles = competitionRoleService.getAllCompRoles();
        assertThat(roles).isNotNull();
        assertThat(roles.size()).isEqualTo(1);
        assertEquals(savedComp.getId(), roles.get(0).getCompetitionId());
        assertEquals(savedComp.getName(), roles.get(0).getCompetitionName());
    }


}

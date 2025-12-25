package sportbets.service.authorization;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.service.community.TipperService;
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.web.dto.authorization.CompetitionRoleDto;
import sportbets.web.dto.authorization.TipperRoleDto;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TipperRoleServiceTest {
    // Real service being tested
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_USERNAME = "TEST_USER";

    private static final Logger log = LoggerFactory.getLogger(TipperRoleServiceTest.class);
    @Autowired
    private CompFamilyService familyService;
    @Autowired
    private CompService compService;
    @Autowired
    private CompetitionRoleService competitionRoleService;
    @Autowired
    private TipperRoleService tipperRoleService;
    @Autowired
    private TipperRoleRepository tipperRoleRepository;
    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);


    @Autowired
    private TipperService tipperService;
    CompetitionDto savedComp = null;
    TipperDto savedTipper;

   @BeforeEach
    public void setup() {

        CompetitionFamilyDto savedFam = familyService.save(compFamilyDto).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        savedComp = compService.save(compDto).orElseThrow();
        assertNotNull(savedComp);


    }

    @AfterEach
    public void tearDown() {
       log.info("\n");
       log.info("Delete All Test data");

        tipperRoleRepository.deleteAll();
      familyService.deleteByName(TEST_COMP_FAM);
     //   compService.deleteByName(TEST_COMP);
        tipperService.deleteByUserName(TEST_USERNAME);

    }
    @Test
    public void testSave() {
        log.info("testSave");
    }

   @Test
    public void saveTipperRole() {
        log.info("saveTipperRole");
        CompetitionRoleDto compRole = new CompetitionRoleDto(null, TEST_COMP, "", savedComp.getId(), savedComp.getName());
        CompetitionRoleDto savedRole = competitionRoleService.save(compRole);
        assertNotNull(savedRole);
        log.info("savedRole {}", savedRole);
        assertNotNull(savedRole.getId());
        assertEquals(savedComp.getName(), compRole.getName());
        assertEquals(savedComp.getId(), compRole.getCompetitionId());
        assertEquals(savedComp.getName(), compRole.getCompetitionName());
        Competition myComp = compService.findByIdTest(savedComp.getId()).orElseThrow();
        assertThat(myComp.getCompetitionRoles()).isNotNull();
        TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "root", "hint", "eki@gmx.de", savedComp.getId());
        savedTipper = tipperService.save(testTipper).orElseThrow();

        TipperRoleDto tipperRoleDto = new TipperRoleDto(null, savedTipper.getId(), savedTipper.getUsername(), savedRole.getId(), savedRole.getName());


        log.info("\n");
        TipperRoleDto savedTR = tipperRoleService.save(tipperRoleDto).orElseThrow(()-> new EntityNotFoundException("tipperRole not found"));
        assertNotNull(savedTR);
        log.info("savedTR {}", savedTR);
        assertNotNull(savedTR.getId());
        assertEquals(savedRole.getId(), savedTR.getRoleId());

        assertEquals(savedRole.getName(), savedTR.getRoleName());
        assertEquals(savedTipper.getId(), savedTR.getTipperId());
        assertEquals(savedTipper.getUsername(),savedTR.getTipperUserName());
        log.info("\n");
       tipperRoleService.deleteById(savedTR.getId());

    }

}

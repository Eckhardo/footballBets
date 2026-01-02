package sportbets.service.authorization;

import jakarta.persistence.EntityNotFoundException;
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
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.service.community.TipperService;
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.web.dto.authorization.CompetitionRoleDto;
import sportbets.web.dto.authorization.TipperRoleDto;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TipperRoleServiceTest {
    // Real service being tested
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_USERNAME = "TEST_USER";

    private static final Logger log = LoggerFactory.getLogger(TipperRoleServiceTest.class);
    final CompetitionFamilyDto competitionFamily = new CompetitionFamilyDto(null, TEST_COMP_FAM, "description of testliga", true, true);
    Competition savedComp = null;
    Tipper savedTipper;
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
    @Autowired
    private TipperService tipperService;

    @BeforeEach
    public void setup() {

        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        savedComp = compService.save(compDto    );
        assertNotNull(savedComp);


    }

    @AfterEach
    public void tearDown() {
        log.info("\n");
        log.info("Delete All Test data");


    }

    @Test
    public void testSave() {
        log.info("testSave");
    }

    @Test
    public void saveTipperRole() {
        log.info("saveTipperRole");
        Competition myComp = compService.findById(savedComp.getId()).orElseThrow();
        assertThat(myComp.getCompetitionRoles()).isNotNull();
        TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "root", "hint", "eki@gmx.de", savedComp.getId());
        savedTipper = tipperService.save(testTipper);
        CompetitionRole savedRole = competitionRoleService.findByCompName(savedComp.getName()).orElseThrow();

        TipperRoleDto tipperRoleDto = new TipperRoleDto(null, savedTipper.getId(), savedTipper.getUsername(), savedRole.getId(), savedRole.getName());


        log.info("\n");
        TipperRole savedTipperRole = tipperRoleService.save(tipperRoleDto).orElseThrow(() -> new EntityNotFoundException("savedTipperRole not found"));
        assertNotNull(savedTipperRole);
        log.info("savedTipperRole {}", savedTipperRole);
        assertNotNull(savedTipperRole.getId());

        assertEquals(savedRole.getId(), savedTipperRole.getRole().getId());

        assertEquals(savedRole.getName(), savedTipperRole.getRole().getName());
        assertEquals(savedTipper.getId(), savedTipperRole.getTipper().getId());
        assertEquals(savedTipper.getUsername(), savedTipperRole.getTipper().getUsername());
        log.info("\n");

    }

}

package sportbets.service.community;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommunityWizardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityWizardServiceTest.class);

    @Autowired
    private CommunityWizardService communityWizardService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CompFamilyService compFamilyService;
    @Autowired
    private TipperService tipperService;
    @Autowired
    private CompService compService;


    final CompetitionFamilyDto familyDto = TestConstants.createValidFamilyDto();
    final CommunityDto commDto = TestConstants.createValidCommunityDto();
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    final TipperDto testTipper = TipperConstants.createValidTipperDto();

    private Competition savedComp = null;
    private Tipper savedTipper = null;
    private CommunityWizardRecord testRecord = null;

    @BeforeEach
    public void setup() {
        log.info("setup");
        //  tipperService.deleteByUserName(testTipper.getUsername());
        CompetitionFamily savedFam = compFamilyService.save(familyDto);
        compDto.setFamilyId(savedFam.getId());
        compDto.setFamilyName(savedFam.getName());


        savedComp = compService.save(compDto);

        savedTipper = tipperService.save(testTipper);
        log.info("setup end");
    }

    @AfterEach
    public void tearDown() {
        log.info("tearDown");
        compFamilyService.deleteByName(familyDto.getName());
        tipperService.deleteByUserName(testTipper.getUsername());
        communityService.deleteByName(commDto.getName());

        log.info("tearDown end");
    }

    @Test
    @Order(1)
    void whenSaveCommunityWithGivenCompetition_thenCompetitionMembershipShouldBeCreated() {
        testRecord = new CommunityWizardRecord(commDto.getName(), commDto.getDescription(), savedComp.getId(), savedComp.getName(), savedTipper.getUsername());
        CommunityWizardRecord saved = communityWizardService.save(testRecord);

        assertThat(saved.commName()).isNotNull();
        assertThat(saved.commDescription()).isEqualTo(commDto.getDescription());
        assertEquals(saved.compId(), savedComp.getId());
        assertEquals(saved.compName(), savedComp.getName());
        assertEquals(testTipper.getUsername(), saved.tipperUserName());
        Community community = communityService.findByName(commDto.getName()).orElseThrow(() -> new EntityNotFoundException("Community with name " + commDto.getName() + " not found"));
        Tipper tipper = tipperService.findById(savedTipper.getId()).orElseThrow(() -> new EntityNotFoundException("entity tipper not found"));
        assertThat(tipper.getDefaultCommunityId()).isEqualTo(community.getId());
        Set<TipperRole> roles = tipper.getTipperRoles();
        assertNotNull(roles);
    }


}

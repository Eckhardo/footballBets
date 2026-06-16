package sportbets.service.community;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommunityWizardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityWizardServiceTest.class);

    @Autowired
    private TipperService tipperService;
    @Autowired
    CommunityWizardService communityWizardService;
    @Autowired
    CommunityService communityService;
    @Autowired
    CompFamilyService compFamilyService;
    @Autowired
    private CompService compService;

    final CompetitionFamilyDto familyDto =  TestConstants.createValidFamilyDto();
    TipperDto testTipper = TipperConstants.WERNER_DTO;

    final CommunityDto commDto = TestConstants.TEST_COMMUNITY_DTO;
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    Competition savedComp = null;
    Tipper savedTipper = null;

    CommunityWizardRecord testRecord = null;

    @BeforeEach
    public void setup() {
        log.info("setup");
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

        communityService.deleteByName(commDto.getName());
        tipperService.deleteByUserName(testTipper.getUsername());

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
    }


}

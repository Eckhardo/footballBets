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
import sportbets.service.competition.CompFamilyService;
import sportbets.service.competition.CompService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionMembershipDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    final TipperDto adminTipperDto = TipperConstants.createValidTipperDto();
    final TipperDto memberTipperDto = TipperConstants.createValidTipperDto2();
    private Competition savedComp = null;
    private Tipper adminTipper = null;
    private CommunityWizardRecord testRecord = null;
    TippModusTotoDto toto = TestConstants.createValidTippModusTotoDto();
    TippModusResultDto result = TestConstants.createValidTippModusResultDto();

    private List<Long> tipperIds = new ArrayList<>();
    List<TippModusDto> tippModdi = List.of(toto,result);

    @BeforeEach
    public void setup() {
        log.info("setup");
        //  tipperService.deleteByUserName(testTipper.getUsername());
        CompetitionFamily savedFam = compFamilyService.save(familyDto);
        compDto.setFamilyId(savedFam.getId());
        compDto.setFamilyName(savedFam.getName());
        savedComp = compService.save(compDto);

        adminTipper = tipperService.save(adminTipperDto);
        Tipper memberTipper = tipperService.save(memberTipperDto);
        tipperIds.add(memberTipper.getId());
        log.info("setup end");
    }

    @AfterEach
    public void tearDown() {
        log.info("tearDown");
        compFamilyService.deleteByName(familyDto.getName());
        tipperService.deleteByUserName(adminTipperDto.getUsername());
        tipperService.deleteByUserName(memberTipperDto.getUsername());

        communityService.deleteByName(commDto.getName());

        log.info("tearDown end");
    }

    @Test
    @Order(1)
    void whenSaveCommunityWithGivenCompetition_thenCompetitionMembershipShouldBeCreated() {
        testRecord = new CommunityWizardRecord(commDto.getName(), commDto.getDescription(), savedComp.getId(), savedComp.getName(), adminTipper.getUsername(), tipperIds, tippModdi);
        CompetitionMembershipDto saved = communityWizardService.save(testRecord);
        assertThat(saved.getCompId()).isNotNull();
        assertThat(saved.getCommId()).isNotNull();
        assertThat(saved.getCommName()).isEqualTo(commDto.getName());
        assertThat(saved.getCompName()).isEqualTo(savedComp.getName());
    }

    @Test
    @Order(2)
    void whenDeleteCommunityPreparedByWizard_thenCompetitionMembershipShouldBeCreated() {
        testRecord = new CommunityWizardRecord(commDto.getName(), commDto.getDescription(), savedComp.getId(), savedComp.getName(), adminTipper.getUsername(), tipperIds, tippModdi);
        CompetitionMembershipDto saved = communityWizardService.save(testRecord);
        assertThat(saved.getCompId()).isNotNull();
        assertThat(saved.getCommId()).isNotNull();
        assertThat(saved.getCommName()).isEqualTo(commDto.getName());
        assertThat(saved.getCompName()).isEqualTo(savedComp.getName());

        communityService.deleteById(saved.getCommId());

    }
}

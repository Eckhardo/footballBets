package sportbets.service.tipps;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.service.community.CommunityService;
import sportbets.service.competition.*;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.testdata.TestConstants.COMM_TEST;
import static sportbets.testdata.TestConstants.COMP_TEST;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TippConfigServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippConfigServiceTest.class);
    static CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    static CommunityDto communityDto = new CommunityDto(null, COMM_TEST, "Description of Community");

    @Autowired
    TippConfigService tippConfigService;

    private static CompFamilyService familyService;
    private static CompService compService; // Real service being tested
    private static CompRoundService compRoundService;
    private static SpieltagService spieltagService;
    private static TeamService teamService;
    private static CommunityService communityService;
    private static TippModusService tippModusService;
    private static CompetitionMembershipService membershipService;


    @Autowired
    public void setRepos(CompFamilyService familyService, CompService compService,
                         CompRoundService compRoundService, SpieltagService spieltagService,
                         TeamService teamService, CommunityService communityService,
                         CompetitionMembershipService membershipService,
                         TippModusService tippModusService) {
        TippConfigServiceTest.familyService = familyService;
        TippConfigServiceTest.compService = compService;
        TippConfigServiceTest.compRoundService = compRoundService;
        TippConfigServiceTest.spieltagService = spieltagService;
        TippConfigServiceTest.teamService = teamService;
        TippConfigServiceTest.communityService = communityService;
        TippConfigServiceTest.membershipService = membershipService;
        TippConfigServiceTest.tippModusService = tippModusService;
    }

    @BeforeAll
    static void setupOnce() {
        log.debug("setupOnce");

        CompetitionFamily savedFam = familyService.save(TestConstants.TEST_FAMILY);
        CompetitionDto compDto = new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, competitionFamily.getName());
        compDto.setFamilyId(savedFam.getId());
        Competition savedComp = compService.save(compDto);
        CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, compDto.getName(), 18, 17, 1);
        compRoundDto.setCompId(savedComp.getId());
        CompetitionRound savedCompRound = compRoundService.save(compRoundDto);
        SpieltagDto matchDayDto = new SpieltagDto(null, 1, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        Spieltag savedMatchday = spieltagService.save(matchDayDto);
        CommunityDto commDto = new CommunityDto(null, COMM_TEST, "Description of Community");
        Community savedComm = communityService.save(commDto);
        CompetitionMembershipDto competitionMembershipDto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedComm.getId(), savedComm.getName());
        CompetitionMembership savedCommunityMembership = membershipService.save(competitionMembershipDto);
        TippModusTotoDto tippModusTotoDto = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedComm.getId(), savedComm.getName());
        TippModusTotoDto dto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);
    }

    @AfterAll
    static void tearDownOnce() {
        log.debug("tearDownOnce");
        familyService.deleteByName(competitionFamily.getName());
        teamService.deleteByName(TestConstants.TEAM_1.getName());
        teamService.deleteByName(TestConstants.TEAM_2.getName());
        communityService.deleteByName(communityDto.getName());
    }

    @Test
    @Order(1)
    public void ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved() {
        log.debug("ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved");
        Long compMembId = 1L;
        List<TippConfigRow> configRows = tippConfigService.findTippConfigRows(compMembId);
        assertNotNull(configRows);
        assertEquals(34, configRows.size());
        for (TippConfigRow row : configRows) {
            log.debug("row: {}", row);
        }


    }

    @Test
    @Order(2)
    public void ifCompMembIdAndRoundIdIsProvided_ThenAllTippConfigsAreRetrieved() {
        log.debug("ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved");
        Long compMembId = 1L;
        List<TippConfigRow> configRows = tippConfigService.findAllForRound(compMembId, 1L);
        assertNotNull(configRows);
        assertEquals(17, configRows.size());
        for (TippConfigRow row : configRows) {
            log.debug("row: {}", row);
        }


    }


}

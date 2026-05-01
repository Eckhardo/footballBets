package sportbets.service.tipps;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.service.community.CommunityService;
import sportbets.service.competition.*;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippConfigDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.testdata.TestConstants.COMM_TEST;
import static sportbets.testdata.TestConstants.COMP_TEST;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional // mandatory to check ref integrity
public class TippConfigServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippConfigServiceTest.class);
    static CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    static CommunityDto communityDto = new CommunityDto(null, COMM_TEST, "Description of Community");

    @Autowired
    TippConfigService tippConfigService;
    @Autowired
    TippModusRepository tippModusRepository;


    private static CompFamilyService familyService;
    private static CompService compService; // Real service being tested
    private static CompRoundService compRoundService;
    private static SpieltagService spieltagService;

    private static CommunityService communityService;
    private static TippModusService tippModusService;
    private static CompetitionMembershipService membershipService;

    static Spieltag savedMatchday;
    static CompetitionMembership savedCompMemb;
    static TippModusTotoDto savedTippModus;
    static CompetitionRound savedCompRound;

    @Autowired
    public void setRepos(CompFamilyService familyService, CompService compService,
                         CompRoundService compRoundService, SpieltagService spieltagService,
                         CommunityService communityService,
                         CompetitionMembershipService membershipService,
                         TippModusService tippModusService) {
        TippConfigServiceTest.familyService = familyService;
        TippConfigServiceTest.compService = compService;
        TippConfigServiceTest.compRoundService = compRoundService;
        TippConfigServiceTest.spieltagService = spieltagService;

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
        savedCompRound = compRoundService.save(compRoundDto);
        SpieltagDto matchDayDto = new SpieltagDto(null, 66, LocalDateTime.now(), savedCompRound.getId(), savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto);
        CommunityDto commDto = new CommunityDto(null, COMM_TEST, "Description of Community");
        Community savedComm = communityService.save(commDto);
        CompetitionMembershipDto competitionMembershipDto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedComm.getId(), savedComm.getName());
        savedCompMemb = membershipService.save(competitionMembershipDto);
        TippModusTotoDto tippModusTotoDto = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedComm.getId(), savedComm.getName());
        savedTippModus = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);

    }

    @AfterAll
    static void tearDownOnce() {
        log.debug("tearDownOnce");
       familyService.deleteByName(competitionFamily.getName());
       communityService.deleteByName(communityDto.getName());
    }

    @Test
    @Order(1)
    public void ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved() {
        log.debug("ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved");
        TippConfigDto tippConfigDto = new TippConfigDto(null, savedCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTippModus.getId());
        log.debug("tippConfigDto: {}", tippConfigDto);
        TippConfigDto savedTippConfig = tippConfigService.save(tippConfigDto);
        assertNotNull(savedTippConfig.getId());
        List<TippConfigRow> configRows = tippConfigService.findTippConfigRows(savedCompMemb.getId());
        assertEquals(1, configRows.size());

    }

    @Test
    @Order(2)
    public void ifCompMembIdAndRoundIdIsProvided_ThenAllTippConfigsAreRetrieved() {
        log.debug("ifCompMembIdIsProvided_ThenAllTippConfigsAreRetrieved");
        TippConfigDto tippConfigDto = new TippConfigDto(null, savedCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTippModus.getId());
        log.debug("tippConfigDto: {}", tippConfigDto);
        TippConfigDto savedTippConfig = tippConfigService.save(tippConfigDto);
        assertNotNull(savedTippConfig.getId());
        List<TippConfigRow> configRows = tippConfigService.findAllForRound(savedCompRound.getId(),savedCompMemb.getId());
        assertNotNull(configRows);
        assertEquals(1, configRows.size());
    }

    @Test
    @Order(3)
    public void ifTippConfigIsSaved_ThenRetrievalSucceeds() {
        log.debug("ifTippConfigIsSaved_ThenRetrievalSucceeds");
        TippConfigDto tippConfigDto = new TippConfigDto(null, savedCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTippModus.getId());
        log.debug("tippConfigDto: {}", tippConfigDto);
        TippConfigDto savedTippConfig = tippConfigService.save(tippConfigDto);
        assertNotNull(savedTippConfig.getId());
        assertEquals(tippConfigDto.getSpieltagNumber(), savedTippConfig.getSpieltagNumber());
        assertEquals(tippConfigDto.getSpieltagId(), savedTippConfig.getSpieltagId());
        assertEquals(tippConfigDto.getTippModusId(), savedTippConfig.getTippModusId());
        assertEquals(tippConfigDto.getCompMembId(), savedTippConfig.getCompMembId());


        //Check that ref integrity is provided (parents keep the TippConfig entity)
        CompetitionMembership cm = membershipService.findById(savedCompMemb.getId()).orElseThrow();
        Set<TippConfig> configs = cm.getTippConfigs();
        TippConfig config = configs.stream().findFirst().orElseThrow();
        assertEquals(savedTippConfig.getId(), config.getId());

        Spieltag sp = spieltagService.findById(savedMatchday.getId()).orElseThrow();
        TippConfig spConfigs = sp.getTippConfig();
        assertEquals(savedTippConfig.getId(), spConfigs.getId());

        TippModus modus = tippModusRepository.findById(savedTippModus.getId()).orElseThrow();
        Set<TippConfig> tmConfigs = modus.getTippConfigs();
        TippConfig tmConfig = tmConfigs.stream().findFirst().orElseThrow();
        assertEquals(savedTippConfig.getId(), tmConfig.getId());
    }

    @Test
    @Order(4)
    public void ifTippConfigIsSaved_ThenRetrievalByParentIdsSucceeds() {
        log.debug("ifTippConfigIsSaved_ThenRetrievalByParentIdsSucceeds");
        TippConfigDto tippConfigDto = new TippConfigDto(null, savedCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedTippModus.getId());

        TippConfigDto savedTippConfig = tippConfigService.save(tippConfigDto);
        assertNotNull(savedTippConfig.getId());
        TippConfigDto retrievedByParents= tippConfigService.findByParents(savedCompMemb.getId(),savedTippModus.getId(),savedMatchday.getId()).orElseThrow();
        assertNotNull(retrievedByParents);
        assertEquals(savedTippConfig.getId(), retrievedByParents.getId());
    }
}
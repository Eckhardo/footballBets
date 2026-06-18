package sportbets.service.tipps;


import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.service.community.CommunityMembershipService;
import sportbets.service.community.CommunityService;
import sportbets.service.community.TipperService;
import sportbets.service.competition.*;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TippServiceTest {


    private static final Logger log = LoggerFactory.getLogger(TippServiceTest.class);
    @Autowired
    TipperService tipperService;
    CompetitionFamilyDto competitionFamily = TestConstants.createValidFamilyDto();
    TeamDto savedTeamDto = null;
    TeamDto savedTeamDto2 = null;
    Competition savedComp = null;
    CompetitionRound savedCompRound = null;
    Spieltag savedMatchday = null;
    Spiel savedSpiel = null;
    Spiel savedSpiel2 = null;
    TippModusTotoDto savedTippModusToto = null;
    TippModusPointDto savedTippModusPoint = null;
    Community savedCommunity = null;
    Tipper savedTipper = null;
    CompetitionMembership savedCompMemb = null;
    CommunityMembership savedCommunityMembership = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SpieltagService spieltagService;
    @Autowired
    private SpielService matchService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMembershipService communityMembershipService;
    @Autowired
    private CompetitionMembershipService competitionMembershipService;
    @Autowired
    private TippModusService tippModusService;
    @Autowired
    private TippService tippService;

    @BeforeEach
    public void setUp() {
        CompetitionFamily savedFam = familyService.save(competitionFamily);
        CompetitionDto compDto = TestConstants.createValidCompetitionDto();
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        CompetitionRoundDto compRoundDto = TestConstants.createValidCompRoundDto();
        compRoundDto.setCompId(savedComp.getId());
        savedCompRound = compRoundService.save(compRoundDto);
        SpieltagDto matchDayDto = TestConstants.createValidSpieltagDto();
        matchDayDto.setCompRoundId(savedCompRound.getId());
        matchDayDto.setCompRoundName(savedCompRound.getName());
        savedMatchday = spieltagService.save(matchDayDto);
        TeamDto team = TestConstants.TEAM_DTO_1;
        TeamDto team1 = TestConstants.TEAM_DTO_2;

        savedTeamDto = teamService.save(team);
        savedTeamDto2 = teamService.save(team1);

        SpielDto spielDto = TestConstants.TEST_SPIEL_DTO;
        spielDto.setHeimTeamId(savedTeamDto.getId());
        spielDto.setGastTeamId(savedTeamDto2.getId());
        spielDto.setSpieltagId(savedMatchday.getId());
        savedSpiel = matchService.save(spielDto);

        SpielDto spielDto2 = TestConstants.TEST_SPIEL_DTO_2;
        spielDto2.setHeimTeamId(savedTeamDto2.getId());
        spielDto2.setGastTeamId(savedTeamDto.getId());
        spielDto2.setSpieltagId(savedMatchday.getId());
        savedSpiel2 = matchService.save(spielDto2);
        CommunityDto commDto = TestConstants.createValidCommunityDto();
        savedCommunity = communityService.save(commDto);
        assertNotNull(savedCommunity);
        CompetitionMembershipDto competitionMembershipDto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(), savedCommunity.getName());
        savedCompMemb = competitionMembershipService.save(competitionMembershipDto);
        TippModusTotoDto tippModusTotoDto = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName());

        savedTippModusToto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);

        TippModusPointDto tippModusPointDto = new TippModusPointDto(null, "PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName(), 4);
        savedTippModusPoint = (TippModusPointDto) tippModusService.save(tippModusPointDto);

        TipperDto testTipper = new TipperDto(null, "Kalle", "Wernersen", "Kalleo", "banane", "frucht", "werner@gmx.de", null);

        savedTipper = tipperService.save(testTipper);
        CommunityMembershipDto commMembDto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());
        savedCommunityMembership = communityMembershipService.save(commMembDto);

    }

    @AfterEach
    public void tearDown() {

        log.debug("Delete All Test data");
        familyService.deleteByName(competitionFamily.getName());
        teamService.deleteByName(savedTeamDto.getName());
        teamService.deleteByName(savedTeamDto2.getName());
        tipperService.deleteById(savedTipper.getId());
        communityService.deleteById(savedCommunity.getId());


    }

    @Test
    public void whenSaveOrUpdateTipp_thenRetrievalSucceeds() {
        log.debug("saveOrUpdateTipp_thenRetrievalSucceeds");

        TippDto tippDto = new TippDto(null, 4, 0, 0, null);
        tippDto.setSpielId(savedSpiel.getId());
        tippDto.setSpielNumber(savedSpiel.getSpielNumber());
        tippDto.setCommMembId(savedCommunityMembership.getId());
        tippDto.setTippModusId(savedTippModusPoint.getId());
        tippDto.setTippModusType(savedTippModusPoint.getType());
        TippDto savedTippDto = tippService.saveOne(tippDto);

        log.debug("savedTipp: {}", savedTippDto);
        assertNotNull(savedTippDto.getId());
        assertEquals(tippDto.getTippModusId(), savedTippDto.getTippModusId());
        assertEquals(tippDto.getCommMembId(), savedTippDto.getCommMembId());
        assertEquals(tippDto.getSpielId(), savedTippDto.getSpielId());
        assertEquals(tippDto.getHeimTipp(), savedTippDto.getHeimTipp());
        assertEquals(tippDto.getRemisTipp(), savedTippDto.getRemisTipp());
        assertEquals(tippDto.getGastTipp(), savedTippDto.getGastTipp());
        assertEquals(4, savedTippDto.getWinPoints());

        //update
        savedTippDto.setHeimTipp(2);
        savedTippDto.setGastTipp(2);
        savedTippDto.setRemisTipp(0);
        TippDto updatedTipp = tippService.updateOne(savedTippDto.getId(), savedTippDto).orElseThrow();
        assertEquals(2, updatedTipp.getHeimTipp());
        assertEquals(0, updatedTipp.getRemisTipp());
        assertEquals(2, updatedTipp.getGastTipp());
        assertEquals(2, updatedTipp.getWinPoints());

    }

    @Test
    public void whenSaveOrUpdateTipp_thenWinPointsAreCalculatedCorrectly() {
        log.debug("whenSaveOrUpdateTipp_thenWinPointsAreCalculatedCorrectly");

        TippDto tippDto = new TippDto(null, 4, 0, 0, null);
        tippDto.setSpielId(savedSpiel.getId());
        tippDto.setSpielNumber(savedSpiel.getSpielNumber());
        tippDto.setCommMembId(savedCommunityMembership.getId());
        tippDto.setTippModusId(savedTippModusPoint.getId());
        tippDto.setTippModusType(savedTippModusPoint.getType());
        TippDto savedTippDto = tippService.saveOne(tippDto);
        assertEquals(4, savedTippDto.getWinPoints());

        //update
        savedTippDto.setHeimTipp(1);
        savedTippDto.setGastTipp(3);
        savedTippDto.setRemisTipp(0);
        TippDto updatredTipp = tippService.updateOne(savedTippDto.getId(), savedTippDto).orElseThrow();
        assertEquals(1, updatredTipp.getWinPoints());

    }

    @Test
    public void whenSaveTipp_thenWinPointsAreCalculatedCorrectlyForTotoTipp() {
        log.debug("saveOrUpdateTipp_thenRetrievalSucceeds");

        TippDto tippDto = new TippDto(null, 1, 0, 0, null);
        tippDto.setSpielId(savedSpiel.getId());
        tippDto.setSpielNumber(savedSpiel.getSpielNumber());
        tippDto.setCommMembId(savedCommunityMembership.getId());
        tippDto.setTippModusId(savedTippModusToto.getId());
        tippDto.setTippModusType(savedTippModusToto.getType());
        TippDto savedTippDto = tippService.saveOne(tippDto);
        assertEquals(1, savedTippDto.getWinPoints());


    }
}

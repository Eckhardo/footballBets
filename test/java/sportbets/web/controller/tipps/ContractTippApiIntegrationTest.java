package sportbets.web.controller.tipps;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.SpielFormula;
import sportbets.service.initTestData.ControllerTestDataService;
import sportbets.service.tipps.TippService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.tipps.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTippApiIntegrationTest {

    public static final CompetitionFamilyDto TEST_COMP_FAM_DTO = TestConstants.createValidFamilyDto();
    public static final CompetitionDto TEST_COMP_DTO = TestConstants.createValidCompetitionDto();
    private static final Logger log = LoggerFactory.getLogger(ContractTippApiIntegrationTest.class);
    final TippDto tippDtoHomeWin = new TippDto(null, 4, 0, 0, null);
    final TippDto tippDtoRemis = new TippDto(null, 2, 2, 0, null);
    final TippDto tippDtoGuestWin = new TippDto(null, 0, 1, 3, null);
    @Autowired
    ControllerTestDataService controllerTestDataService;
    TippRecord tippRecord;
    CommunityMembership commMemb;
    Spiel homeWin;
    Spiel remis;
    Spiel guestWin;
    TippModusTotoDto tippModusTotoDto;
    TippModusPointDto tippModusPointDto;
    @Autowired
    private TippService tippService;

    @BeforeEach
    public void setUp() {
        log.debug("setUp");
        tippRecord = controllerTestDataService.initCompWithGamesWitFamAndComp(TEST_COMP_FAM_DTO, TEST_COMP_DTO);
        assertThat(tippRecord.comMemb().getId()).isNotNull();
        assertThat(tippRecord.spiele().size()).isEqualTo(3);
        assertThat(tippRecord.tippModi().size()).isEqualTo(2);
        List<Spiel> spiele = tippRecord.spiele();
        homeWin = spiele.get(0);
        remis = spiele.get(1);
        guestWin = spiele.get(2);
        List<TippModusDto> tippModi = tippRecord.tippModi();
        tippModusTotoDto = (TippModusTotoDto) tippModi.get(0);
        tippModusPointDto = (TippModusPointDto) tippModi.get(1);
        commMemb = tippRecord.comMemb();
    }


    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");

        controllerTestDataService.deleteCompWithGames(TEST_COMP_FAM_DTO.getName());
    }

    @Test
    @Order(1)
    void checkGamesAndFormulas_withValidInput_thenSuccess() {
        log.debug("checkGamesAndFormulas_withValidInput_thenSuccess");


        SpielFormula heimFormelWin = homeWin.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelWin.getHeimTore()).isGreaterThan(heimFormelWin.getGastTore());
        assertThat(heimFormelWin.getPoints()).isEqualTo(3);
        assertThat(heimFormelWin.getWon()).isEqualTo(1);
        SpielFormula gastFormelWin = homeWin.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelWin.getHeimTore()).isLessThan(gastFormelWin.getGastTore());
        assertThat(gastFormelWin.getPoints()).isEqualTo(0);
        assertThat(gastFormelWin.getWon()).isEqualTo(0);


        // remis tipp

        SpielFormula heimFormelRemis = remis.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelRemis.getPoints()).isEqualTo(1);
        assertThat(heimFormelRemis.getRemis()).isEqualTo(1);
        SpielFormula gastFormelRemis = remis.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelRemis.getPoints()).isEqualTo(1);
        assertThat(gastFormelRemis.getRemis()).isEqualTo(1);


        SpielFormula heimFormelGuest = guestWin.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelGuest.getHeimTore()).isLessThan(heimFormelGuest.getGastTore());
        assertThat(heimFormelGuest.getPoints()).isEqualTo(0);
        assertThat(heimFormelGuest.getLost()).isEqualTo(1);
        SpielFormula gastFormelGuest = guestWin.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelGuest.getHeimTore()).isGreaterThan(gastFormelGuest.getGastTore());
        assertThat(gastFormelGuest.getPoints()).isEqualTo(3);
        assertThat(gastFormelGuest.getWon()).isEqualTo(1);


    }

    @Test
    @Order(2)
    void saveTipps_withValidInput_thenSuccess() {
        log.debug("saveTipps_withValidInput_thenSuccess");


        tippDtoHomeWin.setSpielId(homeWin.getId());
        tippDtoHomeWin.setSpielNumber(homeWin.getSpielNumber());
        tippDtoHomeWin.setCommMembId(commMemb.getId());
        tippDtoHomeWin.setTippModusId(tippModusPointDto.getId());
        tippDtoHomeWin.setTippModusType(tippModusPointDto.getType());
        TippDto savedTippDto = tippService.saveOne(tippDtoHomeWin);
        assertThat(savedTippDto).isNotNull();


        // remis tipp


        tippDtoRemis.setSpielId(remis.getId());
        tippDtoRemis.setSpielNumber(remis.getSpielNumber());
        tippDtoRemis.setCommMembId(commMemb.getId());
        tippDtoRemis.setTippModusId(tippModusPointDto.getId());
        tippDtoRemis.setTippModusType(tippModusPointDto.getType());
        TippDto savedTippDtoRemis = tippService.saveOne(tippDtoRemis);
        assertThat(savedTippDtoRemis).isNotNull();


        tippDtoGuestWin.setSpielId(guestWin.getId());
        tippDtoGuestWin.setSpielNumber(guestWin.getSpielNumber());
        tippDtoGuestWin.setCommMembId(commMemb.getId());
        tippDtoGuestWin.setTippModusId(tippModusPointDto.getId());
        tippDtoGuestWin.setTippModusType(tippModusPointDto.getType());
        TippDto savedTippDtGuest = tippService.saveOne(tippDtoGuestWin);
        assertThat(savedTippDtGuest).isNotNull();


    }

}

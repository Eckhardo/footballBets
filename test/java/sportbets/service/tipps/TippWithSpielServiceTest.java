package sportbets.service.tipps;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.SpielFormula;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.service.initTestData.ControllerTestDataService;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.tipps.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TippWithSpielServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippWithSpielServiceTest.class);


    public static final CompetitionFamilyDto TEST_COMP_FAM_DTO = new CompetitionFamilyDto(null, "COMP_FAM_TEST", "description of testliga", true, true, Country.GERMANY);
    public static final CompetitionDto TEST_COMP_DTO = new CompetitionDto(null, "COMP_TEST", "Description of Competition", 3, 1, null, TEST_COMP_FAM_DTO.getName());

    @Autowired
    ControllerTestDataService controllerTestDataService;

    @Autowired
    private TippService tippService;

    TippRecord tippRecord;
    CommunityMembership commMemb;
    Spieltag spieltag;
    Spiel gameHomeWin;
    Spiel gameRemis;
    Spiel gameGuestWin;
    TippModusTotoDto tippModusTotoDto;
    TippModusPointDto tippModusPointDto;

    final TippDto tippDtoHomeWin = new TippDto(null, 4, 0, 0, null);
    final TippDto tippDtoRemis = new TippDto(null, 2, 2, 0, null);
    final TippDto tippDtoGuestWin = new TippDto(null, 0, 1, 3, null);

    @BeforeEach
    public void setUp() {
        log.debug("setUp");
        tippRecord = controllerTestDataService.initCompWithGamesWitFamAndComp(TEST_COMP_FAM_DTO, TEST_COMP_DTO);
        assertThat(tippRecord.comMemb().getId()).isNotNull();
        assertThat(tippRecord.spiele().size()).isEqualTo(3);
        assertThat(tippRecord.tippModi().size()).isEqualTo(2);
        List<Spiel> spiele = tippRecord.spiele();
        gameHomeWin = spiele.get(0);
        gameRemis = spiele.get(1);
        gameGuestWin = spiele.get(2);
        spieltag = gameRemis.getSpieltag();
        List<TippModusDto> tippModi = tippRecord.tippModi();
        tippModusTotoDto = (TippModusTotoDto) tippModi.get(0);
        tippModusPointDto = (TippModusPointDto) tippModi.get(1);
        commMemb = tippRecord.comMemb();

        tippDtoHomeWin.setSpielId(gameHomeWin.getId());
        tippDtoHomeWin.setSpielNumber(gameHomeWin.getSpielNumber());
        tippDtoHomeWin.setCommMembId(commMemb.getId());
        tippDtoHomeWin.setTippModusId(tippModusPointDto.getId());
        tippDtoHomeWin.setTippModusType(tippModusPointDto.getType());

        tippDtoRemis.setSpielId(gameRemis.getId());
        tippDtoRemis.setSpielNumber(gameRemis.getSpielNumber());
        tippDtoRemis.setCommMembId(commMemb.getId());
        tippDtoRemis.setTippModusId(tippModusPointDto.getId());
        tippDtoRemis.setTippModusType(tippModusPointDto.getType());

        tippDtoGuestWin.setSpielId(gameGuestWin.getId());
        tippDtoGuestWin.setSpielNumber(gameGuestWin.getSpielNumber());
        tippDtoGuestWin.setCommMembId(commMemb.getId());
        tippDtoGuestWin.setTippModusId(tippModusPointDto.getId());
        tippDtoGuestWin.setTippModusType(tippModusPointDto.getType());
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


        SpielFormula heimFormelWin = gameHomeWin.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelWin.getHeimTore()).isGreaterThan(heimFormelWin.getGastTore());
        assertThat(heimFormelWin.getPoints()).isEqualTo(3);
        assertThat(heimFormelWin.getWon()).isEqualTo(1);
        SpielFormula gastFormelWin = gameHomeWin.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelWin.getHeimTore()).isLessThan(gastFormelWin.getGastTore());
        assertThat(gastFormelWin.getPoints()).isEqualTo(0);
        assertThat(gastFormelWin.getWon()).isEqualTo(0);


        // remis tipp

        SpielFormula heimFormelRemis = gameRemis.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelRemis.getPoints()).isEqualTo(1);
        assertThat(heimFormelRemis.getRemis()).isEqualTo(1);
        SpielFormula gastFormelRemis = gameRemis.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelRemis.getPoints()).isEqualTo(1);
        assertThat(gastFormelRemis.getRemis()).isEqualTo(1);


        SpielFormula heimFormelGuest = gameGuestWin.getSpielFormulaForHeim().orElseThrow();
        assertThat(heimFormelGuest.getHeimTore()).isLessThan(heimFormelGuest.getGastTore());
        assertThat(heimFormelGuest.getPoints()).isEqualTo(0);
        assertThat(heimFormelGuest.getLost()).isEqualTo(1);
        SpielFormula gastFormelGuest = gameGuestWin.getSpielFormulaForGast().orElseThrow();
        assertThat(gastFormelGuest.getHeimTore()).isGreaterThan(gastFormelGuest.getGastTore());
        assertThat(gastFormelGuest.getPoints()).isEqualTo(3);
        assertThat(gastFormelGuest.getWon()).isEqualTo(1);


    }

    @Test
    @Order(2)
    void saveTipps_withValidInput_thenSuccess() {
        log.debug("saveTipps_withValidInput_thenSuccess");


        TippDto savedTippDto = tippService.saveOne(tippDtoHomeWin);
        assertThat(savedTippDto.getId()).isNotNull();

        TippDto savedTippDtoRemis = tippService.saveOne(tippDtoRemis);
        assertThat(savedTippDtoRemis.getId()).isNotNull();



        TippDto savedTippDtGuest = tippService.saveOne(tippDtoGuestWin);
        assertThat(savedTippDtGuest).isNotNull();
        assertThat(savedTippDtGuest.getId()).isNotNull();


    }

    @Test
    @Order(3)
    void saveTippsList_withValidInput_thenSuccess() {
        log.debug("saveTipps_withValidInput_thenSuccess");


        List<TippDto> savedTipps = tippService.saveList(List.of(tippDtoHomeWin, tippDtoRemis, tippDtoGuestWin));
        assertThat(savedTipps).isNotEmpty();
        assertThat(savedTipps.size()).isEqualTo(3);
        for (TippDto dto : savedTipps) {
            assertThat(dto.getId()).isNotNull();
            assertThat(dto.getCommMembId()).isEqualTo(commMemb.getId());
        }
    }


    @Test
    @Order(4)
    void updateTippsList_withValidInput_thenSuccess() {
        log.debug("updateTippsList_withValidInput_thenSuccess");

        List<TippDto> savedTipps = tippService.saveList(List.of(tippDtoHomeWin, tippDtoRemis, tippDtoGuestWin));
        assertThat(savedTipps).isNotEmpty();
        assertThat(savedTipps.size()).isEqualTo(3);
        for (TippDto saved : savedTipps) {
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getCommMembId()).isEqualTo(commMemb.getId());
            saved.setGastTipp(4);
            saved.setRemisTipp(0);
            saved.setHeimTipp(0);
            log.debug(saved.toString());
        }


        List<TippDto> updated = tippService.updateList(savedTipps);
        assertThat(updated).isNotEmpty();
        assertThat(updated.size()).isEqualTo(3);
        for (TippDto dto : updated) {
            assertThat(dto.getId()).isNotNull();
            assertThat(dto.getCommMembId()).isEqualTo(commMemb.getId());
            assertThat(dto.getGastTipp()).isEqualTo(4);
            assertThat(dto.getRemisTipp()).isEqualTo(0);
            assertThat(dto.getHeimTipp()).isEqualTo(0);
        }


    }
}

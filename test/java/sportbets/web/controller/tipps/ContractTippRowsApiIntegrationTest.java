/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.controller.tipps;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionRoundRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;
import sportbets.web.dto.tipps.TippVO;
import sportbets.web.dto.tipps.TippsContainerDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sportbets.persistence.builder.CompetitionConstants.BUNDESLIGA_NAME_2025;
import static sportbets.persistence.builder.CompetitionConstants.BUNDESLIGA_NAME_2026;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTippRowsApiIntegrationTest {

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

    @Autowired
    private TippRepository tippRepo;

    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    private CompetitionRoundRepository compRoundRepo;
    @Autowired
    private SpieltagRepository matchdayRepo;
    @Autowired
    private TipperRepository tipperRepo;

    @Autowired
    private CommunityRepository commRepo;
    @Autowired
    private CommunityMembershipRepository commMembRepo;

    private static final Logger log = LoggerFactory.getLogger(ContractTippRowsApiIntegrationTest.class);

    Competition savedComp = null;
    CompetitionRound savedRound = null;
    Spieltag savedSpieltag = null;
    Tipper savedTipper = null;
    Community savedCommunity = null;
    CommunityMembership savedCommunityMembership = null;


    @BeforeEach
    public void setup() {
        log.info("setup");
        savedComp = compRepo.findByName(BUNDESLIGA_NAME_2026).orElseThrow(() -> new RuntimeException("Competition not found!"));
        savedRound = compRoundRepo.findByNameAndCompId("Hinrunde", savedComp.getId()).orElseThrow(() -> new RuntimeException("Round not found!"));
        savedTipper = tipperRepo.findByUsername("Eckhardo").orElseThrow(() -> new RuntimeException("Tipper not found!"));
        log.debug("savedTipper={}", savedTipper);
        savedCommunity = commRepo.findByName("Bulitipper").orElseThrow(() -> new RuntimeException("Community not found!"));
        savedCommunityMembership = commMembRepo.findByCommIdAndTipperId(savedCommunity.getId(), savedTipper.getId()).orElseThrow(() -> new RuntimeException("Membership not found!"));
        savedSpieltag = matchdayRepo.findByNumberAndRound(1, savedRound.getId()).orElseThrow(() -> new RuntimeException("Matchday not found!"));
    }

    @AfterEach
    public void teardown() {
        log.info("teardown");
        tippRepo.deleteAll();

    }

    @Order(1)
    @Test
    public void whenTippRowsInContainerAreRetrieved_thenFillingThemSucceedsInCreation_AndUpdatingThemAlsoSucceeds() {
        log.info("whenEmptyTippRowsAreRetrieved_thenFillingThemSucceedsInCreation_AndUpdatingThemAlsoSucceeds");
        EntityExchangeResult<TippsContainerDto> result = webClient.get()
                .uri("/tippRows/" + savedSpieltag.getId() + "/container/" + savedCommunityMembership.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TippsContainerDto.class).returnResult();
        TippsContainerDto actualBody = result.getResponseBody();
        assertNotNull(actualBody);
        assertThat(actualBody.getMatchdayId().equals(savedSpieltag.getId()));
        assertThat(actualBody.getCommMembId().equals(savedCommunityMembership.getId()));

        assertEquals(9, actualBody.getTippRows().size());
        for (TippRow tippRow : actualBody.getTippRows()) {
            assertThat(tippRow.getCompetitionName()).isEqualTo(BUNDESLIGA_NAME_2026);
            assertThat(tippRow.getRoundName()).isEqualTo("Hinrunde");
            assertThat(tippRow.getCommMembId()).isEqualTo(savedCommunityMembership.getId());

        }

        for (TippRow row : actualBody.getTippRows()) {
            row.setHeimTipp(4);
            row.setRemisTipp(0);
            row.setGastTipp(0);
            row.setCommMembId(savedCommunityMembership.getId());
        }
        log.info("create tipps");
        webClient.post()
                .uri("/tipps")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actualBody.getTippRows())
                .exchange()
                .expectStatus()
                .isCreated();

        EntityExchangeResult<List<TippRow>> resultCreate = webClient.get()
                .uri("/tippRows/" + savedSpieltag.getId() + "/container/" + savedCommunityMembership.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TippRow.class).returnResult();
        List<TippRow> actualBodyCreate = resultCreate.getResponseBody();
        log.debug("created tipps behave as expected");
        assertNotNull(actualBodyCreate);
        EntityExchangeResult<TippsContainerDto> resultRead =
                webClient.get()
                        .uri("/tippRows/" + savedSpieltag.getId() + "/container/" + savedCommunityMembership.getId())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(TippsContainerDto.class).returnResult();
        TippsContainerDto actualCreateBody = resultRead.getResponseBody();
        assertNotNull(actualCreateBody);
        assertThat(actualCreateBody.getMatchdayId().equals(savedSpieltag.getId()));
        assertThat(actualCreateBody.getCommMembId().equals(savedCommunityMembership.getId()));
        assertTrue(actualCreateBody.isUpdate());
        log.debug("updated tipps behave as expected");
        for (TippRow tippRow : actualCreateBody.getTippRows()) {
            assertThat(tippRow.getCompetitionName()).isEqualTo(BUNDESLIGA_NAME_2026);
            assertThat(tippRow.getRoundName()).isEqualTo("Hinrunde");
            assertThat(tippRow.getCommMembId()).isEqualTo(savedCommunityMembership.getId());
            assertThat(tippRow.getHeimTipp()).isEqualTo(4);

        }

        for (TippRow row : actualCreateBody.getTippRows()) {
            row.setHeimTipp(2);
            row.setRemisTipp(1);
            row.setGastTipp(1);

        }
        log.info("update tipps");

        webClient.put()
                .uri("/tipps/" + savedSpieltag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actualCreateBody.getTippRows())
                .exchange()
                .expectStatus()
                .isNoContent();


        EntityExchangeResult<TippsContainerDto> resultUpdate =
                webClient.get()
                        .uri("/tippRows/" + savedSpieltag.getId() + "/container/" + savedCommunityMembership.getId())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(TippsContainerDto.class).returnResult();
        TippsContainerDto actualUpdateBody = resultUpdate.getResponseBody();
        assertNotNull(actualUpdateBody);
        assertThat(actualUpdateBody.getMatchdayId().equals(savedSpieltag.getId()));
        assertThat(actualUpdateBody.getCommMembId().equals(savedCommunityMembership.getId()));
        assertTrue(actualUpdateBody.isUpdate());
        log.debug("updated tipps behave as expected");
        for (TippRow tippRow : actualUpdateBody.getTippRows()) {
            assertThat(tippRow.getCompetitionName()).isEqualTo(BUNDESLIGA_NAME_2026);
            assertThat(tippRow.getRoundName()).isEqualTo("Hinrunde");
            assertThat(tippRow.getCommMembId()).isEqualTo(savedCommunityMembership.getId());
            assertThat(tippRow.getHeimTipp()).isEqualTo(2);
            tippRepo.deleteById(tippRow.getTippId());
        }

    }
    @Order(2)
    @Test
    public void whenTippsRowsAreRetrieved_thenSortedListIsReturned() {
        log.info("whenTippsRowsAreRetrieved_thenSortedListIsReturned");
        TippVO vo=new TippVO(null,null,savedSpieltag.getId(),savedCommunity.getId(),null,null,null);
        EntityExchangeResult<List> result =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/tippRows")
                                .queryParam("spieltagId", savedSpieltag.getId())
                                .queryParam("commId", savedCommunity.getId())
                                .build())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(List.class).returnResult();
        List<TippsRow> actualBody = result.getResponseBody();
        assertNotNull(actualBody);
          log.debug("updated tipps behave as expected");
        for (TippsRow tippsRow : actualBody) {
            assertThat(tippsRow.getRoundName()).isEqualTo("Hinrunde");
            tippRepo.deleteById(tippsRow.getTippId());

        }
    }
}
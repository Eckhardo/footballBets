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
import sportbets.persistence.rowObject.SumWinPointsSummaryRow;
import sportbets.web.dto.tipps.TippVO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.persistence.builder.CompetitionConstants.BUNDESLIGA_NAME_2026;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTippTableApiIntegrationTest {

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


    }
    @Order(2)
    @Test
    public void whenTippsTableIsRetrieved_thenSortedListIsReturned() {
        log.info("whenTippsTableIsRetrieved_thenSortedListIsReturned");
        TippVO vo=new TippVO(null,null,null,savedCommunity.getId(),null,1,2);
        EntityExchangeResult<List<SumWinPointsSummaryRow>> result=
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/tippTable")
                                .queryParam("spieltagId", savedSpieltag.getId())
                                .queryParam("commId", savedCommunity.getId())
                                .queryParam("startSpieltag", 1)
                                .queryParam("stopSpieltag", 1)
                                .build())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(SumWinPointsSummaryRow.class).returnResult();
        List<SumWinPointsSummaryRow> actualBody = result.getResponseBody();
        assertNotNull(actualBody);

        log.debug("log {}", actualBody.getClass().getName());
        log.debug("updated tipps behave as expected {}", actualBody.get(0).getClass().getName());

    }
}

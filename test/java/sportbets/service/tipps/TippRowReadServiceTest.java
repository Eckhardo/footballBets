/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.rowObject.TippRow;
import sportbets.service.community.CommunityMembershipService;
import sportbets.service.community.CommunityService;
import sportbets.service.community.TipperService;
import sportbets.service.competition.CompRoundService;
import sportbets.service.competition.CompService;
import sportbets.service.competition.SpieltagService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sportbets.persistence.builder.CompetitionConstants.BUNDESLIGA_NAME_2026;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TippRowReadServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippRowServiceTest.class);
    @Autowired
    TipperService tipperService;
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;
    @Autowired
    private SpieltagService spieltagService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMembershipService communityMembershipService;
    @Autowired
    private TippService tippService;
    @Autowired
    private SpielRepository spielRepo;

    Competition savedComp = null;

    CompetitionRound savedCompRound = null;
    Spieltag savedMatchday = null;
    Community savedCommunity = null;
    Tipper savedTipper = null;
    CommunityMembership savedCommunityMembership = null;

    @BeforeEach
    public void setUp() {
        log.debug("setUp");
        savedComp = compService.findByName(BUNDESLIGA_NAME_2026).orElseThrow(() -> new RuntimeException("Competition not found!"));
        savedCompRound = compRoundService.findByNameAndCompId("Hinrunde", savedComp.getId()).orElseThrow(() -> new RuntimeException("Competition Round not found!"));
        savedMatchday = spieltagService.findByNumberAndRound(1, savedCompRound.getId()).orElseThrow(() -> new RuntimeException("Matchday not found!"));
        savedCommunity = communityService.findByName("Bulitipper").orElseThrow(() -> new RuntimeException("Community not found!"));
        savedTipper = tipperService.findByUsername("Eckhardo").orElseThrow(() -> new RuntimeException("Tipper not found!"));
        savedCommunityMembership = communityMembershipService.findByCommIdAndTipperId(savedCommunity.getId(), savedTipper.getId()).orElseThrow(() -> new RuntimeException("Membership not found!"));
    }

    @AfterEach
    public void tearDown() {
        log.debug("tearDown");
    }

    @Test
    public void retrieveEmptyTippRows() {

        List<TippRow> rows = tippService.findEmptyTippRowsForTipper(savedMatchday.getId());
        assertEquals(9, rows.size());
        rows.forEach(System.out::println);

    }

    @Test
    public void retrieveTippRows() {

        List<TippRow> rows = tippService.findTippRowsForTipper(savedMatchday.getId(), savedCommunityMembership.getId());

        rows.forEach(System.out::println);
        assertEquals(9, rows.size());

    }


    @Test
    public void retrieveTippRowsAfterOneMatchOfMatchdaySaved() {
        List<TippRow> myRows = tippService.findTippRowsForTipper(savedMatchday.getId(), savedCommunityMembership.getId());
        assertEquals(9, myRows.size());
        Spiel savedMatch = spielRepo.findById(307L).orElseThrow(()-> new RuntimeException(""));
        savedMatch.setHeimTore(5);
        savedMatch.setGastTore(0);
        savedMatch.setStattgefunden(true);
        Spiel updatedMatch= spielRepo.save(savedMatch);
        List<TippRow> rows = tippService.findTippRowsForTipper(savedMatchday.getId(), savedCommunityMembership.getId());

        rows.forEach(System.out::println);
        assertEquals(9, rows.size());

        updatedMatch.setHeimTore(0);
        updatedMatch.setGastTore(0);
        updatedMatch.setStattgefunden(false);
        spielRepo.save(updatedMatch);
        List<TippRow> updatedRows = tippService.findTippRowsForTipper(savedMatchday.getId(), savedCommunityMembership.getId());
        assertEquals(9, updatedRows.size());

    }

}

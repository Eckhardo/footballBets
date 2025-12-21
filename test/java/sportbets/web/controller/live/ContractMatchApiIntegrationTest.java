package sportbets.web.controller.live;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.*;
import sportbets.persistence.repository.*;
import sportbets.web.dto.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractMatchApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractCompRoundApiIntegrationTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    private static final String TEST_COMP_ROUND_2 = "Saison 2025: Rueckrunde";

    private static final int TEST_MATCH_DAY = 1000;
    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    CompetitionRoundRepository competitionRoundRepository;
    @Autowired
    SpieltagRepository spieltagRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    SpielRepository spielRepository;

    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null, TEST_COMP_FAM);
    CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false);
    SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now());

    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    TeamDto teamDto = new TeamDto(null, TEAM_NAME, "Braunschweig");
    TeamDto teamDto1 = new TeamDto(null, TEAM_NAME_2, "Kiel");
    SpielDto testSpiel1 = new SpielDto(null, 1, 3, 1, false, LocalDateTime.now(), matchDayDto.getId(), matchDayDto.getSpieltagNumber(), teamDto.getId(), teamDto.getAcronym(), teamDto1.getId(), teamDto1.getAcronym());
    SpielDto testSpiel2 = new SpielDto(null, 2, 3, 3, false, LocalDateTime.now(), matchDayDto.getId(), matchDayDto.getSpieltagNumber(), teamDto1.getId(), teamDto1.getAcronym(), teamDto.getId(), teamDto.getAcronym());

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");

        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        log.info("delete team with id::" + id);
        webClient.delete()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
        Team team2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id2 = team2.getId();
        log.info("delete team with id::" + id2);
        webClient.delete()
                .uri("/teams/" + id2)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @BeforeEach
    public void setUp() {

        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated()
        ;
        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        compDto.setFamilyId(fam.getId());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        compRoundDto.setCompId(comp.getId());
        compRoundDto.setCompName(comp.getName());

        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isCreated().expectBody().jsonPath("$.compId")
                .exists();


        CompetitionRound round = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        matchDayDto.setCompRoundId(round.getId());
matchDayDto.setCompRoundName(round.getName());
        webClient.post()
                .uri("/matchdays")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(matchDayDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.spieltagNumber")
                .exists()
                .jsonPath("$.compRoundId")
                .exists();

        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto)
                .exchange()
                .expectStatus()
                .isCreated();


        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto1)
                .exchange()
                .expectStatus()
                .isCreated();

        Spieltag spieltag = spieltagRepository.findByNumberWithRoundId(TEST_MATCH_DAY, round.getId()).orElseThrow(() -> new EntityNotFoundException(String.valueOf(TEST_MATCH_DAY)));
        assertNotNull(spieltag);
        Team entity = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));

        Team entity2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));

        testSpiel1.setHeimTeamId(entity.getId());
        testSpiel1.setGastTeamId(entity2.getId());
        testSpiel1.setSpieltagId(spieltag.getId());

        webClient.post()
                .uri("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testSpiel1)
                .exchange()
                .expectStatus()
                .isCreated();


        testSpiel2.setHeimTeamId(entity2.getId());
        testSpiel2.setGastTeamId(entity.getId());
        testSpiel2.setSpieltagId(spieltag.getId());

        webClient.post()
                .uri("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testSpiel2)
                .exchange()
                .expectStatus()
                .isCreated();

    }




    @Test
    @Order(1)
    void whenMatchdayIdProvided_ThenFetchMatches() {

        CompetitionRound round = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        assertNotNull(round);
        Spieltag spieltag = spieltagRepository.findByNumberWithRoundId(TEST_MATCH_DAY, round.getId()).orElseThrow(() -> new EntityNotFoundException(String.valueOf(TEST_MATCH_DAY)));
        assertNotNull(spieltag);

        webClient.get()
                .uri("/matchdays/" + spieltag.getId() + "/matches")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(SpielDto.class).hasSize(2);


    }


    @Test
    @Order(2)
    void whenMatchIsUpdated_ThenDetailsHaveChanged() {
        CompetitionRound round = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        assertNotNull(round);
        Spieltag spieltag = spieltagRepository.findByNumberWithRoundId(TEST_MATCH_DAY, round.getId()).orElseThrow(() -> new EntityNotFoundException(String.valueOf(TEST_MATCH_DAY)));
        assertNotNull(spieltag);

        List<Spiel> spiele = spielRepository.findAllForMatchday(spieltag.getId());
        assertNotNull(spiele);
        Spiel spiel = spiele.stream().findFirst().orElseThrow(() -> new EntityNotFoundException(String.valueOf(spieltag.getId())));
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
  //change heimTore and set both teams to the same value (TeamA vs TeamA)
        testSpiel1.setId(spiel.getId());
        testSpiel1.setHeimTore(5);
        testSpiel1.setHeimTeamId(team.getId());
        testSpiel1.setGastTeamId(team.getId());


        webClient.put()
                .uri("/matches/" + spiel.getId())
                .bodyValue(testSpiel1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.heimTore").isEqualTo(5)
                .jsonPath("$.heimTeamId").isEqualTo(team.getId())
                .jsonPath("$.gastTeamId").isEqualTo(team.getId());

    }
}

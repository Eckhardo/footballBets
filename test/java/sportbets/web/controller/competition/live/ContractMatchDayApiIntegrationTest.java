package sportbets.web.controller.competition.live;

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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionRoundRepository;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractMatchDayApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractMatchDayApiIntegrationTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";

    private static final int TEST_MATCH_DAY = 1;
    final CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    final CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null, TEST_COMP_FAM);
    final CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false);
    final SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now());
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionRoundRepository competitionRoundRepository;

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.debug("cleanup");

        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        webClient.delete()
                .uri("/families/" + fam.getId())
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
    }


    @Test
    @Order(1)
    void whenRoundIdProvided_ThenFetchAllMatchDays() {

        CompetitionRound entity = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        Long id = entity.getId();
        webClient.get()
                .uri("/rounds/" + id + "/matchdays")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(SpieltagDto.class).hasSize(1);

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetAllMatchdays_thenResponseIsOK() {

        webClient.get()
                .uri("/matchdays")
                .exchange()
                .expectStatus()
                .isOk();

    }

}
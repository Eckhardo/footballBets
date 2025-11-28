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
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.persistence.repository.CompetitionRepository;
import sportbets.persistence.repository.CompetitionRoundRepository;
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionFamilyDto;
import sportbets.web.dto.CompetitionRoundDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompRoundApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractCompRoundApiIntegrationTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_ROUND = "Saison 2025: Hinrunde";
    private static final String TEST_COMP_ROUND_2 = "Saison 2025: Rueckrunde";
    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    CompetitionRoundRepository competitionRoundRepository;


    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null);
    CompetitionRoundDto compRoundDto = new CompetitionRoundDto(null, 1, TEST_COMP_ROUND, false);

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
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        compRoundDto.setCompId(comp.getId());

        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    @Order(1)
    void createNewRound_withValidDtoInput_thenSuccess() throws Exception {
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        compRoundDto.setCompId(comp.getId());
        compRoundDto.setName(TEST_COMP_ROUND_2);
        log.info("createNewRound_withValidDtoInput_thenSuccess {}", compRoundDto.getCompId());
        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_ROUND_2)
                .jsonPath("$.hasGroups")
                .isEqualTo(false);

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetSingleRound_thenResponseContainsFields() {


        CompetitionRound entity = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        Long id = entity.getId();
        webClient.get()
                .uri("/rounds/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_ROUND)
                .jsonPath("$.hasGroups")
                .exists()
                .jsonPath("$.compId")
                .exists();

    }


    @Test
    @Order(3)
    void updateRound_withValidCompJsonInput_thenSuccess() throws Exception {

        CompetitionRound entity = competitionRoundRepository.findByName(TEST_COMP_ROUND).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_ROUND));
        compRoundDto.setCompId(entity.getCompetition().getId());
        // given
        compRoundDto.setRoundNumber(100);

        // test and verify
        webClient.put()
                .uri("/rounds/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_ROUND)
                .jsonPath("$.roundNumber")
                .isEqualTo(100)
                .jsonPath("$.compId")
                .exists();

    }
}
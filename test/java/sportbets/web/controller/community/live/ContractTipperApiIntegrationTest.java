package sportbets.web.controller.community.live;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTipperApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractTipperApiIntegrationTest.class);

    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_USERNAME = "Testuser";
    TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "passwd", "hint", "eki@gmx.de");
    Competition savedComp = null;

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    TipperRepository tipperRepository;

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


        Tipper tipper = tipperRepository.findByUsername(TEST_USERNAME).orElseThrow(() -> new EntityNotFoundException(TEST_USERNAME));
        Long id = tipper.getId();
        log.debug("delete tipper with id::{}", id);
        webClient.delete()
                .uri("/tipper/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @BeforeEach
    public void setUp() {
        CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null, TEST_COMP_FAM);

        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated();
        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        compDto.setFamilyId(fam.getId());
        compDto.setFamilyName(fam.getName());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();
        savedComp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        assertThat(savedComp, notNullValue());
    }


    @Test
    @Order(1)
    void createNewTipper_withValidDtoInput_thenSuccess() {
        testTipper.setDefaultCompetitionId(savedComp.getId());
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);

    }

    @Test
    @Order(2)
    void createNewTipper_withInvalidDtoInput_thenFailure() {

        testTipper.setDefaultCompetitionId(savedComp.getId());
        testTipper.setEmail("abc.gmx.de");

        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isBadRequest();

        testTipper.setEmail("ecki@gmx.de");
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);

    }

    @Test
    @Order(3)
    void updateTipper_withValidInput_thenSuccess() {
        String updatedName="Werner";

        testTipper.setDefaultCompetitionId(savedComp.getId());
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);


        Tipper tipper = tipperRepository.findByUsername(TEST_USERNAME).orElseThrow(() -> new EntityNotFoundException(TEST_USERNAME));
        testTipper.setId(tipper.getId());
        testTipper.setFirstname(updatedName);

        // test and verify
        webClient.put()
                .uri("/tipper/" + testTipper.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.firstname")
                .isEqualTo(updatedName)
                .jsonPath("$.lastname")
                .exists()
                .jsonPath("$.passwort")
                .exists();

    }

    @Test
    @Order(4)
    void deleteTipper_withValidInput_thenSuccess() {

        testTipper.setDefaultCompetitionId(savedComp.getId());
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);

        Tipper tipper = tipperRepository.findByUsername(TEST_USERNAME).orElseThrow(() -> new EntityNotFoundException(TEST_USERNAME));
        testTipper.setId(tipper.getId());

        webClient.delete()
                .uri("/tipper/" + testTipper.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        testTipper.setDefaultCompetitionId(savedComp.getId());
        testTipper.setId(null);
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);


    }
    @Test
    @Order(2)
    void createDuplicateTipper_withSameUserNane_thenFailure() {

        testTipper.setDefaultCompetitionId(savedComp.getId());
        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME);

        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class)
                .value(problem -> {
                    Assertions.assertThat(problem.getTitle()).isEqualTo("duplicate entity");
                    Assertions.assertThat(problem.getStatus()).isEqualTo(400);
                    Assertions.assertThat(problem.getDetail()).contains("Tipper already exist with given username");

                });

    }
}
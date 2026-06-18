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
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.TipperDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTipperApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractTipperApiIntegrationTest.class);

    private static final String TEST_COMM = "My Test Community";
    private static final String TEST_USERNAME = "Testuser";
    CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");

    TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "passwd", "hint", "eki@gmx.de", null);
    Community savedCommunity = null;

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    TipperRepository tipperRepository;

    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");
        Community savedComm = communityRepository.findByName(TEST_COMM).orElseThrow();
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
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
        log.debug("setup tipper and community");

        webClient.post()
                .uri("/communities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(communityDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMM);
    }


    @Test
    @Order(1)
    void createNewTipper_withValidDtoInput_thenSuccess() {
        savedCommunity = communityRepository.findByName(TEST_COMM).orElseThrow();

        assertThat(testTipper.getUsername(), notNullValue());
        testTipper.setDefaultCommunityId(savedCommunity.getId());

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
        savedCommunity = communityRepository.findByName(TEST_COMM).orElseThrow();

        testTipper.setDefaultCommunityId(savedCommunity.getId());
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
        savedCommunity = communityRepository.findByName(TEST_COMM).orElseThrow();

        testTipper.setDefaultCommunityId(savedCommunity.getId());
        String updatedName = "Werner";


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
        savedCommunity = communityRepository.findByName(TEST_COMM).orElseThrow();

        testTipper.setDefaultCommunityId(savedCommunity.getId());

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
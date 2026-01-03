package sportbets.web.controller.community.live;

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
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.web.dto.community.CommunityDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCommunityApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractCommunityApiIntegrationTest.class);

    private static final String TEST_COMM = "My Test Community";
    private static final String TEST_COMM_2 = "My Test Community 2";


    CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;


    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");
        Community savedComm = communityRepository.findByName(TEST_COMM).orElseThrow();
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @BeforeEach
    public void setUp() {

    }

    @Test
    @Order(1)
    void createNewCommunity_withValidDtoInput_thenSuccess() {

        log.info("createNewCommunity_withValidDtoInput_thenSuccess");
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
    @Order(2)
    void createNewCommunity_withInvalidDtoInput_thenFailure() {

        log.info("createNewCommunity_withInvalidDtoInput_thenFailure");
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

        // same community again
        webClient.post()
                .uri("/communities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(communityDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class)
                .value(problem -> {
                    assertThat(problem.getTitle()).isEqualTo("duplicate entity");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).contains("Community already exist with given name");
                    // Access custom properties if any were added
                    // assertThat(problem.getProperties().get("timestamp")).isNotNull();
                });
    }

    @Test
    @Order(3)
    void updateCommunity_withValidInput_thenSuccess() {
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


        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        communityDto.setId(community.getId());
        communityDto.setName(TEST_COMM_2);

        // test and verify
        webClient.put()
                .uri("/communities/" + communityDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(communityDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMM_2)
                .jsonPath("$.description")
                .exists();
        // test and verify
        communityDto.setName(TEST_COMM);
        webClient.put()
                .uri("/communities/" + communityDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(communityDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.description")
                .exists();


    }
    @Test
    @Order(4)
    void createNewCommunity_thenGetAll_isSuccess() {

        log.info("createNewCommunity_thenGetAll_isSuccess");
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

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        communityDto.setId(community.getId());


        webClient.get()
                .uri("/communities")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CommunityDto.class).contains(communityDto);


    }

}

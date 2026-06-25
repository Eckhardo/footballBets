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
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCommApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractCommApiIntegrationTest.class);

    CommunityDto communityDto = TestConstants.createValidCommunityDto();
    CommunityDto communityDto2 = TestConstants.createValidCommunityDto2();

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;


    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");
        Community savedComm = communityRepository.findByName(communityDto.getName()).orElseThrow();
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

        log.debug("createNewCommunity_withValidDtoInput_thenSuccess");
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
                .isEqualTo(communityDto.getName());


    }


    @Test
    @Order(2)
    void createNewCommunity_withInvalidDtoInput_thenFailure() {

        log.debug("createNewCommunity_withInvalidDtoInput_thenFailure");
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
                .isEqualTo(communityDto.getName());

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
                .isEqualTo(communityDto.getName());


        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        communityDto.setId(community.getId());
        communityDto.setName(communityDto2.getName());

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
                .isEqualTo(communityDto2.getName())
                .jsonPath("$.description")
                .exists();
        // test and verify
        communityDto.setName(communityDto.getName());
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
                .isEqualTo(communityDto.getName())
                .jsonPath("$.description")
                .exists();


    }

    @Test
    @Order(4)
    void createNewCommunity_thenGetAll_isSuccess() {

        log.debug("createNewCommunity_thenGetAll_isSuccess");
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
                .isEqualTo(communityDto.getName());

        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        communityDto.setId(community.getId());


        webClient.get()
                .uri("/communities")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CommunityDto.class).contains(communityDto);

    }
    @Test
    @Order(5)
    void createNewCommunity_thenGetOne_isSuccess() {

        log.debug("createNewCommunity_thenGetOne_isSuccess");
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
                .isEqualTo(communityDto.getName());

        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();


        webClient.get()
                .uri("/communities/"+ community.getId())
                .exchange()
                .expectStatus()
                .isOk();

    }

}

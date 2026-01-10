package sportbets.web.controller.authorization.live;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.web.dto.authorization.LoginRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractAuthApiIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(ContractAuthApiIntegrationTest.class);
    private static final String TEST_USERNAME = "Eckhardo";

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    TipperRepository tipperRepository;

    LoginRequestDto loginRequest = new LoginRequestDto();

    @Test
    @Order(1)
    void createNewTipper_withValidDtoInput_thenSuccess() {

        Tipper tipper = tipperRepository.findByUsername(TEST_USERNAME).orElseThrow(() -> new EntityNotFoundException(TEST_USERNAME));
        loginRequest.setUserName(tipper.getUsername());
        loginRequest.setPassword(tipper.getPasswort());
        webClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.username")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.defaultCommunityId").exists()
                .jsonPath("$.defaultCompetitionId").exists()
                .jsonPath("$.tipperCommunities").isNotEmpty()
                .jsonPath("$.adminCommunities").isNotEmpty()
                .jsonPath("$.adminCompetitions").isNotEmpty();


    }

}

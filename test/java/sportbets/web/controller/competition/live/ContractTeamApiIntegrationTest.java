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
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.web.dto.competition.TeamDto;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTeamApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractTeamApiIntegrationTest.class);
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    final TeamDto teamDto = new TeamDto(null, TEAM_NAME, "Braunschweig",true);
    final TeamDto teamDto1 = new TeamDto(null, TEAM_NAME_2, "Kiel",true);
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    TeamRepository teamRepository;

    @BeforeEach
    public void setUp() {

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
        log.debug("setup finished");
    }

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.debug("cleanup");
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        log.debug("delete team with id::{}", id);
        webClient.delete()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();

        Team team2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id2 = team2.getId();
        log.debug("delete team with id::{}", id2);
        webClient.delete()
                .uri("/teams/" + id2)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(1)
    void givenPreloadedData_whenGetSingleTeam_thenResponseContainsFields() {
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        webClient.get()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(id))
                .jsonPath("$.name")
                .isEqualTo(TEAM_NAME)
                .jsonPath("$.acronym")
                .isEqualTo("Braunschweig");

    }


    @Test
    @Order(2)
    void updateTeam_withValidTeamJsonInput_thenSuccess() {
        log.debug("updateTeam_withValidTeamJsonInput_thenSuccess");

        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        teamDto.setAcronym("Changed Description");
        Long id = team.getId();
        webClient.put()
                .uri("/teams/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEAM_NAME)
                .jsonPath("$.acronym")
                .isEqualTo("Changed Description");

    }

    @Test
    @Order(3)
    void whenCallForAll_ThenNewTeamIsPartOfCollection() {
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        teamDto.setId(team.getId());

        webClient.get()
                .uri("/teams")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TeamDto.class).contains(teamDto);
    }
}

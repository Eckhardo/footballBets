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
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.TeamRepository;
import sportbets.web.dto.TeamDto;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTeamApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractTeamApiIntegrationTest.class);
    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

    @Autowired
    TeamRepository teamRepository;


    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    TeamDto teamDto = new TeamDto(null, TEAM_NAME, "Braunschweig");
    TeamDto teamDto1 = new TeamDto(null, TEAM_NAME_2, "Kiel");


    @BeforeEach
    public void setUp() {
        log.info("setup {}", teamDto);
        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto)
                .exchange()
                .expectStatus()
                .isCreated();
        log.info("setup finished");
    }

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        log.info("delete team with id::" + id);
        webClient.delete()
                .uri("/teams/" + id)
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
    void updateTeam_withValidTeamJsonInput_thenSuccess() throws Exception {
        log.info("updateTeam_withValidTeamJsonInput_thenSuccess");

        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        teamDto.setAcronym("Changed Description");
        Long id = team.getId();
        log.info("updateFamily with id::" + id);
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
    void whenCompIdProvided_ThenFetchAllTeams() {
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

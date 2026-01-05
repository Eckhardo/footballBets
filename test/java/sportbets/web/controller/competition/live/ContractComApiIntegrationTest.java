package sportbets.web.controller.competition.live;


import jakarta.persistence.EntityNotFoundException;
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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractComApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractComApiIntegrationTest.class);

    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_2 = "TestLiga: Saison 2026";
    final CompetitionFamilyDto compFamilyDto = TestConstants.TEST_FAMILY;
    final CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null, compFamilyDto.getName());
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository repository;

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.debug("cleanup");

        CompetitionFamily fam = competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
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
        CompetitionFamily fam = competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        compDto.setFamilyId(fam.getId());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    // @Test
    //  @Order(1)


    @Test
    @Order(1)
    void createNewComp_withValidCompJsonInput_thenSuccess() {
        CompetitionFamily fam = competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        compDto.setFamilyId(fam.getId());
        compDto.setName(TEST_COMP_2);

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_2)
                .jsonPath("$.winMultiplicator")
                .isEqualTo(3)
                .jsonPath("$.remisMultiplicator")
                .exists();

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetSingleComp_thenResponseContainsFields() {
        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_2));
        Long id = entity.getId();
        webClient.get()
                .uri("/competitions/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(id))
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP)
                .jsonPath("$.winMultiplicator")
                .exists()
                .jsonPath("$.familyId")
                .exists()
                .jsonPath("$.familyName")
                .exists();

    }


    @Test
    @Order(3)
    void updateComp_withValidCompJsonInput_thenSuccess() {
        log.debug("updateComp_withValidCompJsonInput_thenSuccess");

        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Long id = entity.getId();
        compDto.setDescription("changed description");
        log.debug("updateComp with id::{}", id);
        webClient.put()
                .uri("/competitions/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP)
                .jsonPath("$.description")
                .isEqualTo("changed description")
                .jsonPath("$.familyId")
                .exists()
                .jsonPath("$.familyName")
                .exists();

    }

    @Test
    @Order(4)
    void whenCompIdProvided_ThenFetchAllTeams() {
        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_2));
        Long id = entity.getId();
        webClient.get()
                .uri("/competitions/" + id + "/teams")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TeamDto.class).hasSize(0);
    }

    @Test
    @Order(4)
    void whenFindAllForComp_ThenFetchAll() {
        Competition comp = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));

        webClient.get()
                .uri("/competitions/" + comp.getId() + "/rounds")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompetitionRoundDto.class).hasSize(0);
    }




    @Test
    @Order(5)
    void createNewComp_withInvalidDtoInput_thenFailure() {
        CompetitionFamily fam = competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        compDto.setFamilyId(fam.getId());
        compDto.setName(TEST_COMP_2);

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_2)
                .jsonPath("$.winMultiplicator")
                .isEqualTo(3)
                .jsonPath("$.remisMultiplicator")
                .exists();


        // same community again
        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class)
                .value(problem -> {
                    assertThat(problem.getTitle()).isEqualTo("duplicate entity");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).contains("Comp  already exist with given name");

                });
    }


}

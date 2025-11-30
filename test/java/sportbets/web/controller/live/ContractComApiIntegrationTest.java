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
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionFamilyDto;
import sportbets.web.dto.SpieltagDto;
import sportbets.web.dto.TeamDto;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractComApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractComApiIntegrationTest.class);

    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;

    @Autowired
    CompetitionRepository repository;
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_2 = "TestLiga: Saison 2026";
    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null);



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

    }

    // @Test
    //  @Order(1)


    @Test
    @Order(1)
    void createNewComp_withValidCompJsonInput_thenSuccess() throws Exception {
        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
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
                .exists();

    }


    @Test
    @Order(3)
    void updateComp_withValidCompJsonInput_thenSuccess() throws Exception {
        log.info("updateComp_withValidCompJsonInput_thenSuccess");

        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Long id = entity.getId();
        compDto.setDescription("changed description");
        log.info("updateComp with id::" + id);
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
                .exists();

    }

    @Test
    @Order(4)
    void whenCompIdProvided_ThenFetchAllTeams() {
        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_2));
        Long id = entity.getId();
        webClient.get()
                .uri("/competitions/"+id +"/teams")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TeamDto.class).hasSize(0);
    }

}

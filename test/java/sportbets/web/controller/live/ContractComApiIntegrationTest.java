package sportbets.web.controller.live;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.FileCopyUtils;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.persistence.repository.CompetitionRepository;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractComApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractComApiIntegrationTest.class);
    private static final String TEST_COMP_NAME_ZWEITE_BUNDESLIGA = "Liga 2 Saison 2025";
    private static final String COMP_FAM_ZWEITE_BUNDESLIGA = "2. Bundesliga";

    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    CompetitionFamilyRepository competitionFamilyRepository;
    @Value("classpath:compFamily/compFamily.json")
    Resource resourceFamily;
    @Autowired
    CompetitionRepository repository;
    @Value("classpath:comp/comp.json")
    Resource resource;

    @Value("classpath:comp/compFamily.json")
    Resource resourceFam;


    @Value("classpath:comp/compUpdate.json")
    Resource updateResource;


    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");


    }
    // @Test
    //  @Order(1)

    /**
     * Initiate once in order to provide a valid comFamily Id in the json test files for competition.
     *
     * @throws Exception
     */
  //  @Test
  //  @Order(1)
    void createNewFamily_withValidFamilyJsonInput_thenSuccess() throws Exception {
        String familyJson = generateFamilyInput();

        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(familyJson)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM_ZWEITE_BUNDESLIGA)
                .jsonPath("$.description")
                .isEqualTo("DFL 2. Bundesliga")
                .jsonPath("$.hasLigaModus")
                .exists();

    }

    @Test
    @Order(1)
    void createNewComp_withValidCompJsonInput_thenSuccess() throws Exception {
        String testJSON = generateCompInput();

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testJSON)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_NAME_ZWEITE_BUNDESLIGA)
                .jsonPath("$.winMultiplicator")
                .isEqualTo(3)
                .jsonPath("$.remisMultiplicator")
                .exists();

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetSingleComp_thenResponseContainsFields() {
        Competition entity = repository.findByName(TEST_COMP_NAME_ZWEITE_BUNDESLIGA).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_NAME_ZWEITE_BUNDESLIGA));
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
                .isEqualTo(TEST_COMP_NAME_ZWEITE_BUNDESLIGA)
                .jsonPath("$.winMultiplicator")
                .exists()
                .jsonPath("$.familyId")
                .exists();

    }


    @Test
    @Order(3)
    void updateComp_withValidCompJsonInput_thenSuccess() throws Exception {
        log.info("updateComp_withValidCompJsonInput_thenSuccess");
        String testJSON = generateCompUpdateInput();
        Competition entity = repository.findByName(TEST_COMP_NAME_ZWEITE_BUNDESLIGA).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_NAME_ZWEITE_BUNDESLIGA));
        Long id = entity.getId();
        log.info("updateComp with id::" + id);
        webClient.put()
                .uri("/competitions/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testJSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(TEST_COMP_NAME_ZWEITE_BUNDESLIGA)
                .jsonPath("$.description")
                .isEqualTo("changed description");

    }

    @Test
    @Order(4)
    void deleteComp_withValidCompJsonInput_thenSuccess() throws Exception {
        log.info("deleteComp_withValidCompJsonInput_thenSuccess");

        Competition entity = repository.findByName(TEST_COMP_NAME_ZWEITE_BUNDESLIGA).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_NAME_ZWEITE_BUNDESLIGA));
        Long id = entity.getId();
        log.info("deleteComp with id::" + id);
        webClient.delete()
                .uri("/competitions/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();


    }

    private String generateCompInput() throws Exception {
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }



    private String generateCompUpdateInput() throws Exception {
        Reader reader = new InputStreamReader(updateResource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }


    private String generateFamilyInput() throws Exception {
        Reader reader = new InputStreamReader(resourceFam.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}

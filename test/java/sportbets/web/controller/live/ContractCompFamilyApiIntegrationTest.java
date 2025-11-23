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
import sportbets.config.TestProfileDatabase;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.repository.CompetitionFamilyRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes  = {FootballBetsApplication.class, TestProfileDatabase.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompFamilyApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractCompFamilyApiIntegrationTest.class);
    @Autowired(required = true)
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    ;
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Value("classpath:compFamily.json")
    Resource resource;

    @Value("classpath:compFamilyWithComp.json")
    Resource resourceWithComp;

    @Value("classpath:compFamilyUpdate.json")
    Resource updateResource;

    @Value("classpath:compFamilyWithCompUpdate.json")
    Resource updateWithCompResource;

    @AfterEach
    public  void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");


    }
  private static String COMP_FAM="2. Bundesliga";

    @Test
    @Order(1)
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
                .isEqualTo(COMP_FAM)
                .jsonPath("$.description")
                .isEqualTo("DFL 2. Bundesliga")
                .jsonPath("$.hasLigaModus")
                .exists();

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetSingleCampaign_thenResponseContainsFields() {
        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        ;
        Long id = fam.getId();
        webClient.get()
                .uri("/families/"+id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(id))
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM)
                .jsonPath("$.hasLigaModus")
                .exists()
                .jsonPath("$.competitions")
                .isEmpty();

    }


    @Test
    @Order(3)
    void updateFamily_withValidFamilyJsonInput_thenSuccess() throws Exception {
        log.info("updateFamily_withValidFamilyJsonInput_thenSuccess");
        String familyJson = generateFamilyUpdateInput();
        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        ;
        Long id = fam.getId();
        log.info("updateFamily with id::" + id);
        webClient.put()
                .uri("/families/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(familyJson)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM)
                .jsonPath("$.description")
                .isEqualTo("changed description")
                .jsonPath("$.hasLigaModus")
                .exists();

    }

    @Test
    @Order(4)
    void deleteFamily_withValidFamilyJsonInput_thenSuccess() throws Exception {
        log.info("deleteFamily_withValidFamilyJsonInput_thenSuccess");

        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        ;
        Long id = fam.getId();
        log.info("deleteFamily with id::" + id);
        webClient.delete()
                .uri("/families/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();


    }
    @Test
    @Order(5)
    void createNewFamilyWithComp_withValidFamilyJsonInput_thenSuccess() throws Exception {
        String familyJson = generateFamilyWithCompInput();

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
                .isEqualTo(COMP_FAM)
                .jsonPath("$.description")
                .isEqualTo("DFL 2. Bundesliga")
                .jsonPath("$.hasLigaModus")
                .isEqualTo(true)
                .jsonPath("$.competitions")
                .exists()
                .jsonPath("$.competitions..name")
                .exists()
                .jsonPath("$.competitions..winMultiplicator")
                .value(hasItem(3))
                .jsonPath("$.competitions..description")
                .value(hasItem("Beschreibung des Wettbewerbs"));

    }

    @Test
    @Order(6)
    void updateNewFamilyWithComp_withValidFamilyJsonInput_thenSuccess() throws Exception {
        log.info("updateNewFamilyWithComp_withValidFamilyJsonInput_thenSuccess");
        String familyJson = generateFamilyWithCompUpdateInput();
        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        ;
        Long id = fam.getId();
        log.info("updateFamily with id::" + id);
        webClient.put()
                .uri("/families/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(familyJson)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM)
                .jsonPath("$.description")
                .isEqualTo("change of description")
                .jsonPath("$.hasLigaModus")
                .exists();

    }



    @Test
    @Order(7)
    void deleteFamily2_withValidFamilyJsonInput_thenSuccess() throws Exception {
        log.info("deleteFamily2_withValidFamilyJsonInput_thenSuccess");

        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        ;
        Long id = fam.getId();
        log.info("deleteFamily with id::" + id);
        webClient.delete()
                .uri("/families/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();


    }
    private String generateFamilyInput() throws Exception {
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    private String generateFamilyWithCompInput() throws Exception {
        Reader reader = new InputStreamReader(resourceWithComp.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    private String generateFamilyUpdateInput() throws Exception {
        Reader reader = new InputStreamReader(updateResource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
    private String generateFamilyWithCompUpdateInput() throws IOException {
        Reader reader = new InputStreamReader(updateWithCompResource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}

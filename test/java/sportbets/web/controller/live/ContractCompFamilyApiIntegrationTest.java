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
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.web.dto.CompetitionFamilyDto;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompFamilyApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractCompFamilyApiIntegrationTest.class);
    private static final String COMP_FAM = "Premier League";
    private static final String COMP_FAM_2 = "Serie A Italia";
    CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, COMP_FAM, "Description of TestLiga", true, true);

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;


    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.info("cleanup");
        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        Long id = fam.getId();
        log.info("deleteFamily with id::" + id);
        webClient.delete()
                .uri("/families/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();


    }
    @BeforeEach
    public void setUp(){
        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated();

    }


    @Test
    @Order(1)
    void givenPreloadedData_whenGetSingleFamily_thenResponseContainsFields() {
        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
        Long id = fam.getId();
        webClient.get()
                .uri("/families/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(id))
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM)
                .jsonPath("$.hasLigaModus")
                .isEqualTo(true)
                .jsonPath("$.hasClubs")
                .isEqualTo(true);


    }


    @Test
    @Order(2)
    void updateFamily_withValidFamilyJsonInput_thenSuccess() throws Exception {
        log.info("updateFamily_withValidFamilyJsonInput_thenSuccess");

        CompetitionFamily fam = competitionFamilyRepository.findByName(COMP_FAM).orElseThrow(() -> new EntityNotFoundException(COMP_FAM));
       compFamilyDto.setDescription("Changed Description");
        Long id = fam.getId();
        log.info("updateFamily with id::" + id);
        webClient.put()
                .uri("/families/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(COMP_FAM)
                .jsonPath("$.description")
                .isEqualTo("Changed Description")
                .jsonPath("$.hasLigaModus")
                .isEqualTo(true)
                .jsonPath("$.hasClubs")
                .isEqualTo(true);

    }



}

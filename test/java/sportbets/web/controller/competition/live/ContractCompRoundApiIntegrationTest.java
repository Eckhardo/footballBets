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
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionRoundRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.SpieltagDto;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompRoundApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractCompRoundApiIntegrationTest.class);

    private static final int TEST_MATCH_DAY = 1;
    final CompetitionFamilyDto compFamilyDto = TestConstants.createValidFamilyDto();
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    final CompetitionRoundDto compRoundDto = TestConstants.createValidCompRoundDto();
    final CompetitionRoundDto compRoundDto2 = TestConstants.createValidCompRoundDto2();
    final SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now());
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionRoundRepository competitionRoundRepository;

    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
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
        compDto.setFamilyName(fam.getName());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();
        Competition comp = competitionRepository.findByName(compDto.getName()).orElseThrow(() -> new EntityNotFoundException(compDto.getName()));
        compRoundDto.setCompId(comp.getId());
        compRoundDto.setCompName(comp.getName());

        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isCreated();
        CompetitionRound round = competitionRoundRepository.findByName(compRoundDto.getName()).orElseThrow(() -> new EntityNotFoundException(compRoundDto.getName()));
        matchDayDto.setCompRoundId(round.getId());
        matchDayDto.setCompRoundName(round.getName());

        webClient.post()
                .uri("/matchdays")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(matchDayDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody().jsonPath("$.compRoundId")
                .exists();
    }


    @Test
    @Order(1)
    void createNewRound_withValidDtoInput_thenSuccess() {
        Competition comp = competitionRepository.findByName(compDto.getName()).orElseThrow(() -> new EntityNotFoundException(compDto.getName()));
        compRoundDto2.setCompId(comp.getId());
        compRoundDto2.setCompName(comp.getName());

        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto2)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(compRoundDto2.getName())
                .jsonPath("$.hasGroups")
                .isEqualTo(false);

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenGetSingleRound_thenResponseContainsFields() {


        CompetitionRound entity = competitionRoundRepository.findByName(compRoundDto.getName()).orElseThrow(() -> new EntityNotFoundException(compRoundDto.getName()));
        Long id = entity.getId();
        webClient.get()
                .uri("/rounds/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(compRoundDto.getName())
                .jsonPath("$.hasGroups")
                .exists()
                .jsonPath("$.compId")
                .exists()
                .jsonPath("$.compName")
                .exists();

    }


    @Test
    @Order(3)
    void updateRound_withValidCompJsonInput_thenSuccess() {

        CompetitionRound entity = competitionRoundRepository.findByName(compRoundDto.getName()).orElseThrow(() -> new EntityNotFoundException(compRoundDto.getName()));
        compRoundDto.setCompId(entity.getCompetition().getId());
        // given
        compRoundDto.setRoundNumber(100);

        // test and verify
        webClient.put()
                .uri("/rounds/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(compRoundDto.getName())
                .jsonPath("$.roundNumber")
                .isEqualTo(100)
                .jsonPath("$.compId")
                .exists()
                .jsonPath("$.compName")
                .exists();

    }

    @Test
    @Order(4)
    void whenCompIdProvided_ThenFetchAllMatchDays() {
        Competition comp = competitionRepository.findByName(compDto.getName()).orElseThrow(() -> new EntityNotFoundException(compDto.getName()));


        webClient.get()
                .uri("/competitions/" + comp.getId() + "/matchdays")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(SpieltagDto.class).hasSize(1);
    }


    @Test
    @Order(4)
    void whenFindAllForComp_ThenFetchAll() {
        Competition comp = competitionRepository.findByName(compDto.getName()).orElseThrow(() -> new EntityNotFoundException(compDto.getName()));

        webClient.get()
                .uri("/competitions/" + comp.getId() + "/rounds")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompetitionRoundDto.class).hasSize(1);
    }
}
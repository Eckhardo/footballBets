package sportbets.web.controller.tipps;


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
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractTippModusApiIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(ContractTippModusApiIntegrationTest.class);

    private static final String TEST_COMM = "My Test Community";
    CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;


    @Autowired
    TippModusRepository tippModusRepository;
    TippModusTotoDto totoTest = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, null, communityDto.getName());
    TippModusResultDto resultTest = new TippModusResultDto(null, "ErgebnisTest", TippModusType.TIPPMODUS_RESULT.getDisplayName(), 1, null, communityDto.getName(), 3, 1);
    TippModusPointDto pointTest = new TippModusPointDto(null, "PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, null, communityDto.getName(), 4);

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.debug("cleanup");
        Community savedComm = communityRepository.findByName(TEST_COMM).orElseThrow();
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @BeforeEach
    public void setUp() {
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
                .isEqualTo(TEST_COMM);


    }

    @Test
    @Order(1)
    void saveRetrieveAndUpdateTippModusToto_withValidInput_thenSuccess() {
        log.debug("saveRetrieveAndUpdateTippModusToto_withValidInput_thenSuccess");
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        totoTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/toto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(totoTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());
        log.debug("SAVED: ");

        List<TippModusToto> totoList = tippModusRepository.findTippModusToto(community.getId());
        Long totoId = totoList.get(0).getId();
        webClient.get()
                .uri("/tippModus/toto/" + totoId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());
        totoTest.setId(totoId);
        totoTest.setDeadline(5);
        webClient.put()
                .uri("/tippModus/toto/" + totoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(totoTest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.deadline")
                .isEqualTo(totoTest.getDeadline())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());

        webClient.delete()
                .uri("/tippModus/" + totoId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    @Order(2)
    void saveRetrieveAndUpdateTippModusResult_withValidInput_thenSuccess() {

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        resultTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/result")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resultTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.bonusPoints")
                .isEqualTo(resultTest.getBonusPoints())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_RESULT.getDisplayName());

        List<TippModusResult> resultList = tippModusRepository.findTippModusResult(community.getId());
        Long id = resultList.get(0).getId();
        webClient.get()
                .uri("/tippModus/result/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.bonusPoints")
                .isEqualTo(resultTest.getBonusPoints())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_RESULT.getDisplayName());

        resultTest.setId(id);
        resultTest.setBonusPoints(5);
        webClient.put()
                .uri("/tippModus/result/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resultTest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.bonusPoints")
                .isEqualTo(resultTest.getBonusPoints())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_RESULT.getDisplayName());

    }


    @Test
    @Order(3)
    void saveRetrieveAndUpdateTippModusPoint_withValidInput_thenSuccess() {

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        pointTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/point")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pointTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.totalPoints")
                .isEqualTo(pointTest.getTotalPoints())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());

        List<TippModusPoint> pointList = tippModusRepository.findTippModusPoint(community.getId());
        Long id = pointList.get(0).getId();
        webClient.get()
                .uri("/tippModus/point/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.totalPoints")
                .isEqualTo(pointTest.getTotalPoints())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());

        pointTest.setTotalPoints(10);
        pointTest.setId(id);
        webClient.put()
                .uri("/tippModus/point/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pointTest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.totalPoints")
                .isEqualTo(pointTest.getTotalPoints())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());

    }


    @Test
    @Order(4)
    void deleteTippModusPoint_withValidInput_thenSuccess() {

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        pointTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/point")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pointTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());

        List<TippModusPoint> pointList = tippModusRepository.findTippModusPoint(community.getId());
        Long id = pointList.get(0).getId();
        webClient.get()
                .uri("/tippModus/point/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.totalPoints")
                .isEqualTo(pointTest.getTotalPoints())
                .jsonPath("$.commId")
                .isEqualTo(community.getId())
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());


        webClient.delete()
                .uri("/tippModus/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();


        webClient.get()
                .uri("/tippModus/point/" + id)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class)
                .value(problem -> {
                    assertThat(problem.getTitle()).isEqualTo("Invalid associated entity");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).contains("Invalid associated entity: TippModus not found");

                });

    }


    @Test
    @Order(5)
    void retrieveTippModusTotoListForCommunity_withValidInput_thenSuccess() {

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        totoTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/toto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(totoTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());

        totoTest.setName("toot2");

        webClient.post()
                .uri("/tippModus/toto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(totoTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());

        webClient.get()
                .uri("/tippModus/toto/community/" + community.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TippModusTotoDto.class).hasSize(2);

    }


    @Test
    @Order(6)
    void retrieveTippModusListForCommunity_withValidInput_thenSuccess() {

        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();
        totoTest.setCommId(community.getId());

        webClient.post()
                .uri("/tippModus/toto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(totoTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_TOTO.getDisplayName());

        pointTest.setCommId(community.getId());
        webClient.post()
                .uri("/tippModus/point")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pointTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());
        resultTest.setCommId(community.getId());
        webClient.post()
                .uri("/tippModus/result")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resultTest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_RESULT.getDisplayName());


        webClient.get()
                .uri("/tippModus/community/" + community.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TippModusDto.class).hasSize(3);
    }
}
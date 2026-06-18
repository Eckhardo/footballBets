package sportbets.web.controller.tipps;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.competition.*;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippConfigDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
public class ContractTippConfigApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ContractTippConfigApiIntegrationTest.class);
    private static final int TEST_MATCH_DAY = 1;
    final CompetitionFamilyDto compFamilyDto = TestConstants.createValidFamilyDto();
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    final CompetitionRoundDto compRoundDto = TestConstants.createValidCompRoundDto();
    final SpieltagDto matchDayDto = new SpieltagDto(null, TEST_MATCH_DAY, LocalDateTime.now());
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository familyRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionRoundRepository roundRepository;
    @Autowired
    SpieltagRepository spieltagRepository;
    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    CompetitionMembershipRepository compMembRepo;
    @Autowired
    TippModusRepository tippModusRepository;
    CommunityDto communityDto = TestConstants.createValidCommunityDto();

    final TippModusResultDto resultTest = new TippModusResultDto(null, "ErgebnisTest", TippModusType.TIPPMODUS_RESULT.getDisplayName(), 1, null, communityDto.getName(), 3, 1);
    final TippModusPointDto pointTest = new TippModusPointDto(null, "PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, null, communityDto.getName(), 4);


    // saved Entities
    Spieltag savedMatchday;
    Community savedCommunity;
    TippModus savedPointModus;
    TippModus savedResultModus;
    Competition savedComp;
    CompetitionMembership savedoCompMemb;

    @BeforeEach
    public void setUp() {
        log.debug("setUp");


        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated();

        CompetitionFamily fam = familyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        compDto.setFamilyId(fam.getId());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();
        Competition savedComp = competitionRepository.findByName(compDto.getName()).orElseThrow(() -> new EntityNotFoundException(compDto.getName()));
        compRoundDto.setCompId(savedComp.getId());
        compRoundDto.setCompName(savedComp.getName());

        webClient.post()
                .uri("/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compRoundDto)
                .exchange()
                .expectStatus()
                .isCreated().expectBody().jsonPath("$.compId")
                .exists();


        CompetitionRound round = roundRepository.findByName(compRoundDto.getName()).orElseThrow(() -> new EntityNotFoundException(compRoundDto.getName()));
        matchDayDto.setCompRoundId(round.getId());
        matchDayDto.setCompRoundName(round.getName());
        webClient.post()
                .uri("/matchdays")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(matchDayDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.spieltagNumber")
                .exists()
                .jsonPath("$.compRoundId")
                .exists();

        savedMatchday = spieltagRepository.findByNumberWithRoundId(TEST_MATCH_DAY, round.getId()).orElseThrow(() -> new EntityNotFoundException(String.valueOf(TEST_MATCH_DAY)));
        assertNotNull(savedMatchday);

        // community
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
                .isEqualTo(communityDto.getName());

        savedCommunity = communityRepository.findByName(communityDto.getName()).orElseThrow();
        resultTest.setCommId(savedCommunity.getId());
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
                .isEqualTo(communityDto.getName())
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_RESULT.getDisplayName());
        savedResultModus = tippModusRepository.findByNameAndCommunity(resultTest.getName(), savedCommunity.getId()).orElseThrow();
        assertNotNull(savedResultModus);

        pointTest.setCommId(savedCommunity.getId());
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
                .isEqualTo(communityDto.getName())
                .jsonPath("$.type")
                .isEqualTo(TippModusType.TIPPMODUS_POINT.getDisplayName());
        savedPointModus = tippModusRepository.findByNameAndCommunity(pointTest.getName(), savedCommunity.getId()).orElseThrow();
        assertNotNull(savedPointModus);

        CompetitionMembershipDto dto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(), savedCommunity.getName());

        webClient.post()
                .uri("/compMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(communityDto.getName())
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();
        savedoCompMemb = compMembRepo.findByCommIdAndCompId(savedCommunity.getId(), savedComp.getId()).orElseThrow();
        assertNotNull(savedoCompMemb);

    }

    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");


        CompetitionFamily fam = familyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        assertNotNull(fam);
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus()
                .isNoContent();


        Community savedComm = communityRepository.findByName(communityDto.getName()).orElseThrow();
        assertNotNull(savedComm);
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    @Order(1)
    void saveRetrieveAndUpdateTippConfig_withValidInput_thenSuccess() {
        log.debug("saveRetrieveAndUpdateTippConfig_withValidInput_thenSuccess");
        TippConfigDto tippConfigDto = new TippConfigDto(null, savedoCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedPointModus.getId());
        webClient.post()
                .uri("/tippConfig")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tippConfigDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.spieltagNumber")
                .exists()
                .jsonPath("$.id")
                .exists();


//        TippConfigDto tippConfigDto2 = new TippConfigDto(null, savedoCompMemb.getId(), savedMatchday.getId(), savedMatchday.getSpieltagNumber(), savedResultModus.getId());
//        webClient.post()
//                .uri("/tippConfig")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(tippConfigDto2)
//                .exchange()
//                .expectStatus()
//                .isCreated()
//                .expectBody()
//                .jsonPath("$.spieltagNumber")
//                .exists()
//                .jsonPath("$.id")
//                .exists();
    }
}

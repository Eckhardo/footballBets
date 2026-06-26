package sportbets.web.controller.community.live;

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
import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.community.TipperDto;

import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCommMembApiIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(ContractCommMembApiIntegrationTest.class);


    CommunityDto communityDto = TestConstants.createValidCommunityDto();
    TipperDto testTipper = TipperConstants.createValidTipperDto();
    TipperDto testTipper2 = TipperConstants.createValidTipperDto2();

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    TipperRepository tipperRepo;
    @Autowired
    CommunityMembershipRepository commMembRepo;
    CommunityMembershipDto dto = TestConstants.createValidCommunityMembershipDto();


    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");
        Community savedComm = communityRepository.findByName(communityDto.getName()).orElseThrow();
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
                .exchange()
                .expectStatus()
                .isNoContent();


        Tipper savedTipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        webClient.delete()
                .uri("/tipper/" + savedTipper.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        Tipper savedTipper2 = tipperRepo.findByUsername(testTipper2.getUsername()).orElseThrow();
        webClient.delete()
                .uri("/tipper/" + savedTipper2.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @BeforeEach
    public void setUp() {
        log.debug("setup tipper and community");

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

        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(testTipper.getUsername());


        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTipper2)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(testTipper2.getUsername());

    }

    @Test
    @Order(1)
    void createNewCommunityMembership_withValidDtoInput_thenSuccess() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        dto.setTipperId(tipper.getId());
        dto.setCommId(community.getId());
        dto.setCommName(communityDto.getName());

        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(testTipper.getUsername())
                .jsonPath("$.tipperId")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(communityDto.getName())
                .jsonPath("$.commId")
                .exists();


         webClient.get()
                .uri("/commMembs/" + tipper.getUsername()+"/communities")
                 .exchange()
                 .expectStatus()
                 .isOk()
                 .expectBodyList(CommunityDto.class).hasSize(1);
    }

    @Test
    @Order(2)
    void updateNewCommunityMembership_withValidDtoInput_thenSuccess() {

        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        dto.setTipperId(tipper.getId());
        dto.setCommId(community.getId());
        dto.setCommName(communityDto.getName());

        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();

        Tipper tipper2 = tipperRepo.findByUsername(testTipper2.getUsername()).orElseThrow();
        CommunityMembership commMemb = commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();
        dto.setTipperId(tipper2.getId());
        dto.setTipperName(tipper2.getUsername());
        log.debug("update now");
        webClient.put()
                .uri("/commMembs/" + commMemb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(communityDto.getName())
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(testTipper2.getUsername())
                .jsonPath("$.tipperId")
                .exists();
    }


    @Test
    @Order(3)
    void deleteExistingCommunityMembership_withValidDtoInput_thenSuccess() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();

        dto.setTipperId(tipper.getId());
        dto.setCommId(community.getId());
        dto.setCommName(community.getName());

        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();

        CommunityMembership commMemb = commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();

        webClient.delete()
                .uri("/commMembs/" + commMemb.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(4)
    void givenPreloadedData_whenGetSingleCommMemb_thenResponseContainsFields() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        dto.setTipperId(tipper.getId());
        dto.setCommId(community.getId());
        dto.setCommName(community.getName());

        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();

        CommunityMembership commMemb = commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();
        webClient.get()
                .uri("/commMembs/" + commMemb.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(commMemb.getId()))
                .jsonPath("$.commName")
                .isEqualTo(communityDto.getName())
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(testTipper.getUsername())
                .jsonPath("$.tipperId")
                .exists();
    }


    @Test
    @Order(5)
    void createTwoCommunityMemberships_thenTwoTippsShouldBeFound() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        Community community = communityRepository.findByName(communityDto.getName()).orElseThrow();
        dto.setTipperId(tipper.getId());
        dto.setCommId(community.getId());
        dto.setCommName(communityDto.getName());

        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();

        Tipper tipper2 = tipperRepo.findByUsername(testTipper2.getUsername()).orElseThrow();
        dto.setTipperId(tipper2.getId());
        dto.setTipperName(tipper2.getUsername());
        webClient.post()
                .uri("/commMembs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();

        webClient.get()
                .uri("/commMembs/" + community.getId()+"/tipper")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TipperDto.class).hasSize(2);

    }
}

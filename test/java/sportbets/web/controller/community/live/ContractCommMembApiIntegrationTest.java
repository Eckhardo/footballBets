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
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
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


    private static final String TEST_COMM = "My Test Community";
    private static final String TEST_COMM_2 = "My Test Community 2";
    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_USERNAME2 = "TEST_USER2";

    CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
    TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "password", "hint", "eki@gmx.de");
    TipperDto testTipper2 = new TipperDto(null, "Werner", "Wernersen", TEST_USERNAME2, "password", "hint", "werner@gmx.de");

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    TipperRepository tipperRepo;
    @Autowired
    CommunityMembershipRepository commMembRepo;


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


        Tipper savedTipper = tipperRepo.findByUsername(TEST_USERNAME).orElseThrow();
        webClient.delete()
                .uri("/tipper/" + savedTipper.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        Tipper savedTipper2 = tipperRepo.findByUsername(TEST_USERNAME2).orElseThrow();
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
                .isEqualTo(TEST_COMM);

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
                .isEqualTo(TEST_USERNAME);


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
                .isEqualTo(TEST_USERNAME2);

    }

    @Test
    @Order(1)
    void createNewCommunityMembership_withValidDtoInput_thenSuccess() {
        Tipper tipper = tipperRepo.findByUsername(TEST_USERNAME).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CommunityMembershipDto dto = new CommunityMembershipDto(null, tipper.getId(), tipper.getUsername(), community.getId(), community.getName());

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
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.tipperId")
                .exists();


    }


    @Test
    @Order(2)
    void updateNewCommunityMembership_withValidDtoInput_thenSuccess() {



        Tipper tipper = tipperRepo.findByUsername(TEST_USERNAME).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CommunityMembershipDto dto = new CommunityMembershipDto(null, tipper.getId(), tipper.getUsername(), community.getId(), community.getName());

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
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.tipperId")
                .exists();

        Tipper tipper2 = tipperRepo.findByUsername(TEST_USERNAME2).orElseThrow();
        CommunityMembership commMemb=commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();
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
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME2)
                .jsonPath("$.tipperId")
                .exists();

    }


    @Test
    @Order(3)
    void deleteExistingCommunityMembership_withValidDtoInput_thenSuccess() {
        Tipper tipper = tipperRepo.findByUsername(TEST_USERNAME).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CommunityMembershipDto dto = new CommunityMembershipDto(null, tipper.getId(), tipper.getUsername(), community.getId(), community.getName());

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
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.tipperId")
                .exists();

        CommunityMembership commMemb=commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();

        webClient.delete()
                .uri("/commMembs/" + commMemb.getId())
                .exchange()
                .expectStatus()
                .isNoContent();


    }
    @Test
    @Order(4)
    void givenPreloadedData_whenGetSingleCommMemby_thenResponseContainsFields() {
        Tipper tipper = tipperRepo.findByUsername(TEST_USERNAME).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CommunityMembershipDto dto = new CommunityMembershipDto(null, tipper.getId(), tipper.getUsername(), community.getId(), community.getName());

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
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.tipperId")
                .exists();

        CommunityMembership commMemb=commMembRepo.findByCommIdAndTipperId(community.getId(), tipper.getId()).orElseThrow();

        webClient.get()
                .uri("/commMembs/" + commMemb.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(commMemb.getId()))
                .jsonPath("$.commName")
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.tipperName")
                .isEqualTo(TEST_USERNAME)
                .jsonPath("$.tipperId")
                .exists();


    }


}

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
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionMembershipRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionMembershipDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompMembApiIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(ContractCompMembApiIntegrationTest.class);


    private static final String TEST_COMM = "My Test Community";

    final CompetitionFamilyDto compFamilyDto = TestConstants.createValidFamilyDto();
    final CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    final CompetitionDto compDto2 = TestConstants.createValidCompetitionDto2();
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CommunityRepository communityRepository;


    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository compRepo;
    @Autowired
    CompetitionMembershipRepository compMembRepo;

    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");

        CompetitionFamily fam = competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus().isNoContent();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        webClient.delete()
                .uri("/communities/" + community.getId())
                .exchange()
                .expectStatus().isNoContent();



    }

    @BeforeEach
    public void setUp() {
        log.debug("setup competition and community");
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
        compDto.setFamilyName(compFamilyDto.getName());

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
                .isEqualTo(compDto.getName());

        compDto2.setFamilyId(fam.getId());
        compDto2.setFamilyName(compFamilyDto.getName());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto2)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.name")
                .isEqualTo(compDto2.getName());

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
    void createNewCompetiitonMembership_withValidDtoInput_thenSuccess() {

        Competition competition = compRepo.findByName(compDto.getName()).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CompetitionMembershipDto dto = new CompetitionMembershipDto(competition.getId(), competition.getName(), community.getId(), community.getName());

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
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();

    }

    @Test
    @Order(2)
    void updateNewCommunityMembership_withValidDtoInput_thenSuccess() {


        Competition competition = compRepo.findByName(compDto.getName()).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CompetitionMembershipDto dto = new CompetitionMembershipDto(competition.getId(), competition.getName(), community.getId(), community.getName());

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
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();
        Competition competition2 = compRepo.findByName(compDto.getName()).orElseThrow();
        assertNotNull(competition2);
        CompetitionMembership compMemb = compMembRepo.findByCommIdAndCompId(community.getId(), competition.getId()).orElseThrow();
        assertNotNull(compMemb);
        dto.setId(compMemb.getId());
        dto.setCompId(competition2.getId());
        dto.setCompName(competition2.getName());
        log.debug("update now");
        webClient.put()
                .uri("/compMembs/" + compMemb.getId())
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
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();
    }


    @Test
    @Order(3)
    void deleteExistingCommunityMembership_withValidDtoInput_thenSuccess() {

        Competition competition = compRepo.findByName(compDto.getName()).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CompetitionMembershipDto dto = new CompetitionMembershipDto(competition.getId(), competition.getName(), community.getId(), community.getName());

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
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();

        CompetitionMembership compMemb = compMembRepo.findByCommIdAndCompId(community.getId(), competition.getId()).orElseThrow();

        webClient.delete()
                .uri("/commMembs/" + compMemb.getId())
                .exchange()
                .expectStatus()
                .isNoContent();


    }

    @Test
    @Order(4)
    void givenPreloadedData_whenGetSingleCommMemby_thenResponseContainsFields() {
        Competition competition = compRepo.findByName(compDto.getName()).orElseThrow();
        Community community = communityRepository.findByName(TEST_COMM).orElseThrow();

        CompetitionMembershipDto dto = new CompetitionMembershipDto(competition.getId(), competition.getName(), community.getId(), community.getName());

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
                .isEqualTo(TEST_COMM)
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();

        CompetitionMembership compMemb = compMembRepo.findByCommIdAndCompId(community.getId(), competition.getId()).orElseThrow();

        webClient.get()
                .uri("/compMembs/" + compMemb.getId())
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
                .jsonPath("$.compName")
                .isEqualTo(compDto.getName())
                .jsonPath("$.compId")
                .exists();


    }
}

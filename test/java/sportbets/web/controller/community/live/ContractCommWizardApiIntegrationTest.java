package sportbets.web.controller.community.live;


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
import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCommWizardApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractCommWizardApiIntegrationTest.class);

    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();


    @Autowired
    TipperRepository tipperRepo;
    @Autowired
    CompetitionFamilyRepository famRepo;
    @Autowired
    CompetitionRepository compRepo;
    @Autowired
    CommunityRepository commRepo;

    TipperDto tipperDto = TipperConstants.createValidTipperDto();
    TipperDto memberTipperDto = TipperConstants.createValidTipperDto2();
    CompetitionFamilyDto compFamilyDto = TestConstants.createValidFamilyDto();
    CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    CommunityDto commDto = TestConstants.createValidCommunityDto();
    CommunityWizardRecord wizardRecord;
    List<Long> tipperIds = new ArrayList<>(List.of(10L));
    TippModusTotoDto toto = TestConstants.createValidTippModusTotoDto();
    TippModusResultDto result = TestConstants.createValidTippModusResultDto();
    List<TippModusDto> tippModi = List.of(toto,result);

    @BeforeEach
    public void setUp() {
        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated();

        log.debug("setUp");
        CompetitionFamily fam = famRepo.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        compDto.setFamilyId(fam.getId());

        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();


        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tipperDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(tipperDto.getUsername());

        webClient.post()
                .uri("/tipper")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(memberTipperDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .exists()
                .jsonPath("$.username")
                .isEqualTo(memberTipperDto.getUsername());


    }

    @AfterEach
    public void tearDown() {
        log.debug("cleanup");

        CompetitionFamily fam = famRepo.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
        Tipper tipper = tipperRepo.findByUsername(tipperDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(tipperDto.getUsername()));

        log.debug("delete tipper with id::{}", tipper.getId());
        webClient.delete()
                .uri("/tipper/" + tipper.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        Tipper tipper2 = tipperRepo.findByUsername(memberTipperDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(memberTipperDto.getUsername()));

        log.debug("delete tipper with id::{}", tipper2);
        webClient.delete()
                .uri("/tipper/" + tipper2.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
        Community savedComm = commRepo.findByName(commDto.getName()).orElseThrow();
        webClient.delete()
                .uri("/communities/" + savedComm.getId())
                .exchange()
                .expectStatus()
                .isNoContent();


    }

    @Test
    public void whenValidWizardIsSaved_thenResponseSucceeds() {
        Competition comp = compRepo.findByName(compDto.getName()).orElseThrow();
        Tipper tipper = tipperRepo.findByUsername(tipperDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(tipperDto.getUsername()));
        Tipper member = tipperRepo.findByUsername(memberTipperDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(memberTipperDto.getUsername()));
        tipperIds.add(member.getId());

        wizardRecord = new CommunityWizardRecord(commDto.getName(), commDto.getDescription(), comp.getId(), comp.getName(), tipper.getUsername(), tipperIds,tippModi);

        webClient.post()
                .uri("/commWizard")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wizardRecord)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.commId")
                .exists()
                .jsonPath("$.compId")
                .exists()
                .jsonPath("$.commName")
                .isEqualTo(commDto.getName());

    }
}

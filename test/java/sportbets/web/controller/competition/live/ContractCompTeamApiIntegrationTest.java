package sportbets.web.controller.competition.live;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionTeamRepository;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionTeamDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompTeamApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractCompTeamApiIntegrationTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    final CompetitionFamilyDto compFamilyDto = new CompetitionFamilyDto(null, TEST_COMP_FAM, "Description of TestLiga", true, true);
    final CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, null, TEST_COMP_FAM);
    final TeamDto teamDto = new TeamDto(null, TEAM_NAME, "Braunschweig",true);
    final TeamDto teamDto1 = new TeamDto(null, TEAM_NAME_2, "Kiel",true);
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionTeamRepository compTeamRepo;
    @Autowired
    TeamRepository teamRepository;

    @AfterEach
    public void cleanup() {
        // Clean up all books created during tests
        log.debug("cleanup");

        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        log.debug("delete team with id::{}", id);
        webClient.delete()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
        Team team2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id2 = team2.getId();
        log.debug("delete team with id::{}", id2);
        webClient.delete()
                .uri("/teams/" + id2)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @BeforeEach
    public void setUp() {
        // save new family
        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated()
        ;
        CompetitionFamily fam = competitionFamilyRepository.findByName(TEST_COMP_FAM).orElseThrow(() -> new EntityNotFoundException(TEST_COMP_FAM));
        compDto.setFamilyId(fam.getId());
        // save new competition
        webClient.post()
                .uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compDto)
                .exchange()
                .expectStatus()
                .isCreated();
        // save new team 1
        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto)
                .exchange()
                .expectStatus()
                .isCreated();

        // save new team 2
        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto1)
                .exchange()
                .expectStatus()
                .isCreated();
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));

        Team entity = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamDto.setId(entity.getId());
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, comp.getId(), comp.getName(), teamDto.getId(), teamDto.getAcronym(), true);

        Team entity2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamDto1.setId(entity2.getId());
        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, comp.getId(), comp.getName(), teamDto1.getId(), teamDto1.getAcronym(), true);
        log.debug("Post competition team 1{}", compTeamDto);
        // save newcompTeam dto 1
        webClient.post()
                .uri("/compTeam")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compTeamDto)
                .exchange()
                .expectStatus()
                .isCreated();
        log.debug("post compTeam 2{}", compTeamDto2);
        // save newcompTeam dto 2
        webClient.post()
                .uri("/compTeam")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compTeamDto2)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @Order(1)
    void givenPreloadedData_whenGetSingleTeam_thenResponseContainsFields() {
        log.debug("givenPreloadedData_whenGetSingleTeam_thenResponseContainsFields");

        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException(TEAM_NAME));
        Long id = team.getId();
        webClient.get()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(Long.class, equalTo(id))
                .jsonPath("$.name")
                .isEqualTo(TEAM_NAME)
                .jsonPath("$.acronym")
                .value(String.class, equalTo(team.getAcronym()));

    }


    @Test
    @Order(2)
    void whenCompTeamIsUpdated_ThenDetailsHaveChanged() {
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Team team = teamRepository.findByName(TEAM_NAME).orElseThrow(() -> new EntityNotFoundException("entity not found"));
        List<CompetitionTeam> compTeams = compTeamRepo.getAllForComp(comp.getId());
        assertNotNull(compTeams);
        CompetitionTeam compTeam = compTeams.stream().findFirst().orElseThrow(() -> new EntityNotFoundException("entity not found"));
        assertNotNull(compTeam);
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(compTeam.getId(), comp.getId(), comp.getName(), team.getId(), team.getAcronym(), true);


        webClient.put()
                .uri("/compTeam/" + compTeam.getId())
                .bodyValue(compTeamDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.teamId").isEqualTo(team.getId())
                .jsonPath("$.compId").isEqualTo(comp.getId())
                .jsonPath("$.teamAcronym").isEqualTo(team.getAcronym())
                .jsonPath("$.compName").isEqualTo(comp.getName());


        Team team2 = teamRepository.findByName(TEAM_NAME_2).orElseThrow(() -> new EntityNotFoundException("entity not found"));
        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(compTeam.getId(), comp.getId(), comp.getName(), team2.getId(), team2.getAcronym(), true);

        webClient.put()
                .uri("/compTeam/" + compTeam.getId())
                .bodyValue(compTeamDto2)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.teamId").isEqualTo(team2.getId())
                .jsonPath("$.compId").isEqualTo(comp.getId())
                .jsonPath("$.teamAcronym").isEqualTo(team2.getAcronym())
                .jsonPath("$.compName").isEqualTo(comp.getName());


    }

    @Test
    @Order(2)
    void whenCompIdIsProvided_ThenAllCompTeamsAreRetrieved() {
        Competition comp = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));

        webClient.get()
                .uri("/compTeams/" + comp.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompetitionTeamDto.class).hasSize(2);


    }

    @Test
    @Order(3)
    void whenCompIdIsProvided_ThenRegisteredAndUnregisteredCompTeamsAreRetrieved() {
        String TEST_COMP = "1. Bundesliga Saison 2025";
        Competition entity = competitionRepository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Long id = entity.getId();

        EntityExchangeResult<List<List>> result =   webClient.get()
                .uri("/compTeams/" +id+"/teams")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(List.class).hasSize(1)
                .returnResult();

        List<List> actualBody = result.getResponseBody();

        assertNotNull(actualBody);
        assertEquals( 1,actualBody.size());

            log.info("unregistered competition team: " + actualBody);

        for (List<CompetitionTeamDto> o : actualBody) {
            log.info("competition team: " + o.getClass().getSimpleName());
        }


    }

}

package sportbets.web.controller.competition.live;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
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
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionTeamDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompTeamApiIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ContractCompTeamApiIntegrationTest.class);
    final CompetitionFamilyDto compFamilyDto = TestConstants.createValidFamilyDto();
    final CompetitionDto compDto = TestConstants.createValidCompetitionDto();
    final TeamDto teamDto = TestConstants.createValidTeamDto();
    final TeamDto teamDto1 = TestConstants.createValidTeamDto2();
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



    @BeforeEach
    public void setUp() {
        // save new family
        webClient.post()
                .uri("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compFamilyDto)
                .exchange()
                .expectStatus()
                .isCreated();

        CompetitionFamily fam = getCompetitionFamily();
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
        saveNewTeam(teamDto);
        saveNewTeam(teamDto1);
        Competition comp = getComp(compDto.getName());

        CompetitionTeamDto compTeamDto = getCompTeamDto(teamDto, comp);
        CompetitionTeamDto compTeamDto2 = getCompTeamDto(teamDto1, comp);
        log.debug("Post competition team 1{}", compTeamDto);
        saveNewCompTeam(compTeamDto);
        log.debug("post compTeam 2{}", compTeamDto2);
        saveNewCompTeam(compTeamDto2);
    }
    @AfterEach
    public void cleanup() {
        // Clean up all entities created during tests
        log.debug("cleanup");

        CompetitionFamily fam = getCompetitionFamily();
        webClient.delete()
                .uri("/families/" + fam.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
        deleteTeam(teamDto);
        deleteTeam(teamDto1);
    }


    @Test
    @Order(1)
    void givenPreloadedData_whenGetSingleTeam_thenResponseContainsFields() {
        log.debug("givenPreloadedData_whenGetSingleTeam_thenResponseContainsFields");

        Team team = getTeam(teamDto, teamDto.getName());
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
                .isEqualTo(teamDto.getName())
                .jsonPath("$.acronym")
                .value(String.class, equalTo(team.getAcronym()));
    }


    @Test
    @Order(2)
    void whenCompTeamIsUpdated_ThenDetailsHaveChanged() {
        Competition comp = getComp(compDto.getName());
        Team team = getTeam(teamDto, "entity not found");
        List<CompetitionTeam> compTeams = compTeamRepo.getAllForComp(comp.getId());
        assertNotNull(compTeams);
        CompetitionTeam compTeam = compTeams.stream().findFirst().orElseThrow(() -> new EntityNotFoundException("entity not found"));
        assertNotNull(compTeam);

        updateCompTeam(compTeam, comp, team);
        Team team2 = getTeam(teamDto1, "entity not found");
        updateCompTeam(compTeam, comp, team2);


    }


    @Test
    @Order(2)
    void whenCompIdIsProvided_ThenAllCompTeamsAreRetrieved() {
        Competition comp = getComp(compDto.getName());
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
        Competition entity = getComp(TEST_COMP);
        Long id = entity.getId();

        EntityExchangeResult<List<CompetitionTeamDto>> result = webClient.get()
                .uri("/compTeams/" + id + "/teams")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompetitionTeamDto.class)
                .returnResult();

        List<CompetitionTeamDto> actualBody = result.getResponseBody();

        assertNotNull(actualBody);
        assertEquals(0, actualBody.size());


    }

    @NonNull
    private Team getTeam(TeamDto teamDto, String entity_not_found) {
        return teamRepository.findByName(teamDto.getName()).orElseThrow(() -> new EntityNotFoundException(entity_not_found));
    }

    private void updateCompTeam(CompetitionTeam compTeam, Competition comp, Team team) {
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
    }
    @NonNull
    private CompetitionFamily getCompetitionFamily() {
        return competitionFamilyRepository.findByName(compFamilyDto.getName()).orElseThrow(() -> new EntityNotFoundException(compFamilyDto.getName()));
    }

    private void deleteTeam(TeamDto teamDto) {
        Team team = getTeam(teamDto, teamDto.getName());
        Long id = team.getId();
        log.debug("delete team with id::{}", id);
        webClient.delete()
                .uri("/teams/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @NonNull
    private CompetitionTeamDto getCompTeamDto(TeamDto teamDto, Competition comp) {
        Team entity = getTeam(teamDto, "Team not found");
        teamDto.setId(entity.getId());
        return new CompetitionTeamDto(null, comp.getId(), comp.getName(), teamDto.getId(), teamDto.getAcronym(), true);
    }

    @NonNull
    private Competition getComp(String compDto) {
        return competitionRepository.findByName(compDto).orElseThrow(() -> new EntityNotFoundException(compDto));
    }

    private void saveNewTeam(TeamDto teamDto) {
        webClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamDto)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    private void saveNewCompTeam(CompetitionTeamDto compTeamDto) {
        webClient.post()
                .uri("/compTeam")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(compTeamDto)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
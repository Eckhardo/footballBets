package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")

public class CompTeamServiceTestNonTransactional {
    private static final Logger log = LoggerFactory.getLogger(CompTeamServiceTest.class);
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    final CompetitionFamilyDto competitionFamily = TestConstants.TEST_FAMILY;
    final Team team = new Team(TEAM_NAME, "Braunschweig", true);
    final Team team1 = new Team(TEAM_NAME_2, "Kiel",true );

    Competition savedComp = null;
    Team savedTeam1 = null;
    Team savedTeam2 = null;
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private TeamService teamService;
    @Autowired
    private CompTeamService compTeamService;

    @BeforeEach
    public void setup() {
        log.debug("\n");
        log.debug("setup All Test data");
        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        CompetitionDto compDto = TestConstants.TEST_COMP;
        compDto.setFamilyId(savedFam.getId());
        savedComp = compService.save(compDto);
        savedTeam1 = teamService.save(team);
        savedTeam2 = teamService.save(team1);
    }

    @AfterEach
    public void tearDown() {
      familyService.deleteByName(competitionFamily.getName());
      teamService.deleteByName(team.getName());
      teamService.deleteByName(team1.getName());

    }

    @Test
    void whenValidCompTeam_thenCompTeamShouldBeSaved() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeam savedCompTeam = compTeamService.save(compTeamDto);

        assertThat(savedCompTeam.getId()).isNotNull();
        assertThat(savedCompTeam.getCompetition().getId()).isEqualTo(savedComp.getId());
        assertThat(savedCompTeam.getTeam().getId()).isEqualTo(savedTeam1.getId());


    }

    @Test
    void whenValidCompTeam_thenCompTeamShouldBeUpdated() {
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeam savedCompTeam = compTeamService.save(compTeamDto);


        compTeamDto.setTeamId(savedTeam2.getId());
        compTeamDto.setTeamAcronym(savedTeam2.getAcronym());
        CompetitionTeam updatedCompTeam = compTeamService.update(savedCompTeam.getId(), compTeamDto).orElseThrow();

        assertThat(updatedCompTeam.getId()).isNotNull();
        assertThat(updatedCompTeam.getCompetition().getId()).isEqualTo(savedComp.getId());
        assertThat(updatedCompTeam.getTeam().getId()).isEqualTo(savedTeam2.getId());


    }

    @Test
    void whenValidCompTeams_thenCompTeamsShouldBeSavedInBatch() {
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());

        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());


        List<CompetitionTeam> savedCompTeams = compTeamService.saveAll(List.of(compTeamDto, compTeamDto2));
        if (!savedCompTeams.isEmpty()) {
            for (CompetitionTeam compTeam : savedCompTeams) {
                assertThat(compTeam.getId()).isNotNull();
                assertThat(compTeam.getCompetition().getId()).isEqualTo(savedComp.getId());
                assertThat(compTeam.getCompetition().getName()).isEqualTo(savedComp.getName());
                assertThat(compTeam.getTeam().getAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
                assertThat(compTeam.getTeam().getId()).isIn(savedTeam1.getId(), savedTeam2.getId());

            }
        }
    }

    @Test
    void whenValidCompTeams_thenCompTeamsShouldBeDeletedInBatch() {
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());

        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());

        List<CompetitionTeam> savedCompTeams = compTeamService.saveAll(List.of(compTeamDto, compTeamDto2));

        List<Long> ids = savedCompTeams.stream().map(CompetitionTeam::getId).toList();


    }


    @Test
    void whenCompIsDeleted_thenCompTeamShouldBeDeleted() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeam savedCompTeam = compTeamService.save(compTeamDto);

        assertThat(savedCompTeam.getId()).isNotNull();
        assertThat(savedCompTeam.getCompetition().getId()).isEqualTo(savedComp.getId());
        assertThat(savedCompTeam.getTeam().getId()).isEqualTo(savedTeam1.getId());
        log.debug("\n");
        familyService.deleteByName(competitionFamily.getName());
        Optional<Competition> deletedComp = compService.findByName(savedComp.getName());
        assertThat(deletedComp.isEmpty());
        Optional<CompetitionTeam> deletedCompTeam = compTeamService.findById(savedCompTeam.getId());
        assertThat(deletedCompTeam.isEmpty());
        log.debug("\n");


    }

    @Test
    void whenValidComp_thenAllCompTeamsShouldBeRetrieved() {
        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());

        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());

        List<CompetitionTeam> savedCompTeams = compTeamService.saveAll(List.of(compTeamDto, compTeamDto2));

        List<CompetitionTeam> compTeams = compTeamService.getAllFormComp(savedComp.getId());
        assertThat(compTeams.size()).isEqualTo(savedCompTeams.size());

        for (CompetitionTeam compTeam : compTeams) {
            assertThat(compTeam.getId()).isNotNull();
            assertThat(compTeam.getCompetition().getId()).isEqualTo(savedComp.getId());
            assertThat(compTeam.getCompetition().getName()).isEqualTo(savedComp.getName());
            assertThat(compTeam.getTeam().getAcronym()).isIn(savedTeam1.getAcronym(), savedTeam2.getAcronym());
            assertThat(compTeam.getTeam().getId()).isIn(savedTeam1.getId(), savedTeam2.getId());

        }
    }
}

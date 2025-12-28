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
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionTeamDto;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CompTeamServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CompTeamServiceTest.class);
    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    @Autowired
    CompetitionRepository compRepo;
    CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "description of testliga", true, true);

    Team teamDto = new Team(TEAM_NAME, "Braunschweig");
    Team teamDto1 = new Team( TEAM_NAME_2, "Kiel");
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
        log.info("\n");
        log.info("Delete All Test data");
        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();
        Competition competition =new Competition( TEST_COMP, "Description of Competition", 3, 1, savedFam);
        savedComp = compService.save(competition);
        savedTeam1 = teamService.save(teamDto);
        savedTeam2 = teamService.save(teamDto1);
    }

    @AfterEach
    public void tearDown() {
        familyService.deleteByName(TEST_COMP_FAM);
        teamService.deleteByName(TEAM_NAME);
        teamService.deleteByName(TEAM_NAME_2);

    }

    @Test
    void whenValidCompTeam_thenCompTeamShouldBeSaved() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        Optional<CompetitionTeamDto> savedCompTeam = compTeamService.save(compTeamDto);
        if (savedCompTeam.isPresent()) {
            assertThat(savedCompTeam.get().getId()).isNotNull();
            assertThat(savedCompTeam.get().getCompId()).isEqualTo(savedComp.getId());
            assertThat(savedCompTeam.get().getCompName()).isEqualTo(savedComp.getName());
            assertThat(savedCompTeam.get().getTeamId()).isEqualTo(savedTeam1.getId());
            assertThat(savedCompTeam.get().getTeamAcronym()).isEqualTo(savedTeam1.getAcronym());
            compTeamService.deleteById(savedCompTeam.get().getId());
        }

    }

    @Test
    void whenValidCompTeam_thenCompTeamShouldBeUpdated() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeamDto savedCompTeam = compTeamService.save(compTeamDto).orElseThrow();
        savedCompTeam.setTeamId(savedTeam2.getId());
        savedCompTeam.setTeamAcronym(savedTeam2.getAcronym());
        CompetitionTeamDto updatedCompTeam = compTeamService.update(savedCompTeam.getId(), savedCompTeam).orElseThrow();

        assertThat(updatedCompTeam.getId()).isNotNull();
        assertThat(updatedCompTeam.getTeamAcronym()).isEqualTo(savedTeam2.getAcronym());
        assertThat(updatedCompTeam.getTeamId()).isEqualTo(savedTeam2.getId());
        assertThat(updatedCompTeam.getCompId()).isEqualTo(savedComp.getId());
        assertThat(updatedCompTeam.getCompName()).isEqualTo(savedComp.getName());
        compTeamService.deleteById(updatedCompTeam.getId());

    }

    @Test
    void whenValidCompTeams_thenCompTeamsShouldBeSavedInBatch() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());

        List<CompetitionTeamDto> savedCompTeams = compTeamService.saveAll(List.of(compTeamDto, compTeamDto2));
        if (!savedCompTeams.isEmpty()) {
            for (CompetitionTeamDto compTeam : savedCompTeams) {
                assertThat(compTeam.getId()).isNotNull();
                assertThat(compTeam.getCompId()).isEqualTo(savedComp.getId());
                assertThat(compTeam.getCompName()).isEqualTo(savedComp.getName());
                assertThat(compTeam.getTeamAcronym()).isIn(compTeamDto.getTeamAcronym(), compTeamDto2.getTeamAcronym());
                assertThat(compTeam.getTeamId()).isNotNull();
                compTeamService.deleteById(compTeam.getId());
            }
        }
    }

    @Test
    void whenValidCompTeams_thenCompTeamsShouldBeDeletedInBatch() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());

        List<CompetitionTeamDto> savedCompTeams = compTeamService.saveAll(List.of(compTeamDto, compTeamDto2));
        List<Long> ids = savedCompTeams.stream().map(CompetitionTeamDto::getId).toList();
        compTeamService.deleteAll(ids);

    }

    @Test
    void whenValidComp_thenAllCompTeamsShouldBeRetrieved() {

        CompetitionTeamDto compTeamDto = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getAcronym());
        CompetitionTeamDto savedCompTeam = compTeamService.save(compTeamDto).orElseThrow();

        CompetitionTeamDto compTeamDto2 = new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getAcronym());
        CompetitionTeamDto savedCompTeam2 = compTeamService.save(compTeamDto2).orElseThrow();

        List<CompetitionTeamDto> competitionTeamDtos = compTeamService.getAllFormComp(savedComp.getId());
        assertThat(competitionTeamDtos.size()).isEqualTo(2);

        for (CompetitionTeamDto competitionTeam : competitionTeamDtos) {
            assertThat(competitionTeam.getId()).isNotNull();
            assertThat(competitionTeam.getCompId()).isEqualTo(savedComp.getId());
            assertThat(competitionTeam.getCompName()).isEqualTo(savedComp.getName());
            assertThat(competitionTeam.getTeamAcronym()).isIn(compTeamDto.getTeamAcronym(), compTeamDto2.getTeamAcronym());
            assertThat(competitionTeam.getTeamId()).isIn(savedCompTeam.getTeamId(), savedCompTeam2.getTeamId());

        }


    }
}

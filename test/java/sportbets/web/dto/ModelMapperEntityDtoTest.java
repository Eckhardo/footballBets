package sportbets.web.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.competition.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ModelMapperEntityDtoTest {


    private static final Logger log = LoggerFactory.getLogger(ModelMapperEntityDtoTest.class);
    public static final String TEST_FAM = "TestFam";
    public static final String TEST_FAM_DESCR = "Test Fussball Bundesliga";
    public static final String TEST_COMP = "Saison 2025/26";
    public static final String TEST_COMP_DESCR = "TEST  Fussball Bundesliga Saison 2025/26";
    public static final String COMP_ROUND = "Hinrunde";
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    public static final String TEAM_ACR_1 = "Kiel";
    public static final String TEAM_ACR_2 = "Braunschweig";

    ModelMapper modelMapper = new ModelMapper();

    @Test
    void checkModelMapper() {
        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);

        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false);


        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);
        log.info("Comp:: " + compDto.toString());

        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: " + famDto.toString());


    }


    @Test
    public void checkFamily() {
        log.info("\n validate family");
        modelMapper.createTypeMap(CompetitionFamilyDto.class, CompetitionFamily.class);
        modelMapper.validate();
        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: {}", famDto.toString());

    }

    @Test
    public void checkComp() {
        log.info("\n validate competition");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);

        log.info("Comp:: " + compDto.toString());
    }

    @Test
    public void checkRound() {
        log.info("\n validate comp round");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false);
        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());

    }

    @Test
    public void checkSpieltag() {
        log.info("\n validate spieltag");


        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false);
        testRound.setId(7L);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        Spieltag testSpieltag2 = new Spieltag(2, LocalDateTime.now(), testRound);
        Spieltag testSpieltag3 = new Spieltag(4, LocalDateTime.now(), testRound);
        List<Spieltag> spieltage = List.of(testSpieltag, testSpieltag2, testSpieltag3);
        final ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltage.forEach(spieltag -> {
            SpieltagDto spieltagDto = myMapper.map(spieltag, SpieltagDto.class);
            log.info("Spieltag:: " + spieltagDto.toString());
        });
    }

    @Test
    public void checkSpiel() {
        log.info("\n validate spiel");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false);
        testRound.setId(7L);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        testSpieltag.setId(7L);
        Team team1 = new Team("Test1", "1");
        Team team2 = new Team("Test2", "2");
        Team team3 = new Team("Test3", "3");
        Team team4 = new Team("Test4", "4");
        team1.setId(4L);
        team2.setId(5L);
        team3.setId(6L);
        team4.setId(7L);
        Spiel testSpiel1 = new Spiel(testSpieltag, 1, LocalDateTime.now(), team1, team2, 3, 1, false);
        testSpiel1.setId(7L);

        Spiel testSpiel2 = new Spiel(testSpieltag, 2, LocalDateTime.now(), team3, team4, 2, 2, false);
        testSpiel2.setId(8L);
        List<Spiel> spiele = List.of(testSpiel1, testSpiel2);

        List<SpielDto> spielDtos = new ArrayList<>();
        final ModelMapper myMapper = MapperUtil.getModelMapperForSpiel();
        for (Spiel spiel : spiele) {
            System.out.println(spiel.toString());
            spielDtos.add(myMapper.map(spiel, SpielDto.class));
        }

        for (SpielDto spielDto : spielDtos) {
            Assertions.assertNotNull(spielDto.getId());
            Assertions.assertTrue(spielDto.getSpielNumber() != 0);
            Assertions.assertNotNull(spielDto.getAnpfiffdate());
            Assertions.assertTrue(spielDto.getHeimTore() != 0);
            Assertions.assertTrue(spielDto.getGastTore() != 0);
            Assertions.assertNotNull(spielDto.getSpieltagId());
            Assertions.assertNotNull(spielDto.getSpieltagNumber());
            Assertions.assertNotNull(spielDto.getHeimTeamId());
            Assertions.assertNotNull(spielDto.getHeimTeamAcronym());
            Assertions.assertNotNull(spielDto.getGastTeamId());
            Assertions.assertNotNull(spielDto.getGastTeamAcronym());
        }
    }


    @Test
    public void checkCompTeam() {
        log.info("\n validate spiel");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);

        Team team1 = new Team("Test1", "1");
        Team team2 = new Team("Test2", "2");
        Team team3 = new Team("Test3", "3");
        Team team4 = new Team("Test4", "4");
        team1.setId(4L);
        team2.setId(5L);
        team3.setId(6L);
        team4.setId(7L);
        CompetitionTeam compTeam1 = new CompetitionTeam(team1, testComp);
        compTeam1.setId(99L);
        CompetitionTeam compTeam2 = new CompetitionTeam(team1, testComp);
        compTeam2.setId(100L);
        List<CompetitionTeam> competitionTeams = List.of(compTeam1, compTeam2);

        List<CompetitionTeamDto> dtos = new ArrayList<>();
        final ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        for (CompetitionTeam compTeam : competitionTeams) {
            dtos.add(myMapper.map(compTeam, CompetitionTeamDto.class));
        }

        for (CompetitionTeamDto dto : dtos) {
            Assertions.assertNotNull(dto.getId());

            Assertions.assertNotNull(dto.getTeamId());
            Assertions.assertNotNull(dto.getTeamAcronym());

            Assertions.assertNotNull(dto.getCompId());
            Assertions.assertNotNull(dto.getCompName());
        }



    }
}
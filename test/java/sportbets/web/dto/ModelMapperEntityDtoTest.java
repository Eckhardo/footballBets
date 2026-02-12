package sportbets.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.common.Country;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.*;
import sportbets.web.dto.authorization.CommunityRoleDto;
import sportbets.web.dto.authorization.CompetitionRoleDto;
import sportbets.web.dto.authorization.TipperRoleDto;
import sportbets.web.dto.competition.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperEntityDtoTest {


    public static final String TEST_FAM = "TestFam";
    public static final String TEST_FAM_DESCR = "Test Fussball Bundesliga";
    public static final String TEST_COMP = "Saison 2025/26";
    public static final String TEST_COMP_DESCR = "TEST  Fussball Bundesliga Saison 2025/26";
    public static final String COMP_ROUND = "Hinrunde";
    public static final String TEAM_ACR_1 = "Kiel";
    public static final String TEAM_ACR_2 = "Braunschweig";
    private static final Logger log = LoggerFactory.getLogger(ModelMapperEntityDtoTest.class);
    private static final String TEAM_NAME = "Eintracht Braunschweig";
    private static final String TEAM_NAME_2 = "Holstein Kiel";
    final ModelMapper modelMapper = new ModelMapper();

    @Test
    void checkModelMapper() {
        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);

        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false, 18, 17);


        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.debug("Round:: {}", compRoundDto.toString());
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);
        log.debug("Comp:: {}", compDto.toString());

        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.debug("Family:: {}", famDto.toString());


    }


    @Test
    public void checkFamily() {
        log.debug("\n validate family");
        modelMapper.createTypeMap(CompetitionFamilyDto.class, CompetitionFamily.class);
        modelMapper.validate();
        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.debug("Family:: {}", famDto.toString());

    }

    @Test
    public void checkComp() {
        log.debug("\n validate competition");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);

        log.debug("Comp:: {}", compDto.toString());
    }

    @Test
    public void checkRound() {
        log.debug("\n validate comp round");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false, 18, 17);
        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.debug("Round:: {}", compRoundDto.toString());

    }

    @Test
    public void checkSpieltag() {
        log.debug("\n validate spieltag");


        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false, 18, 17);
        testRound.setId(7L);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        Spieltag testSpieltag2 = new Spieltag(2, LocalDateTime.now(), testRound);
        Spieltag testSpieltag3 = new Spieltag(4, LocalDateTime.now(), testRound);
        List<Spieltag> spieltage = List.of(testSpieltag, testSpieltag2, testSpieltag3);
        final ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        spieltage.forEach(spieltag -> {
            SpieltagDto spieltagDto = myMapper.map(spieltag, SpieltagDto.class);
            log.debug("Spieltag:: {}", spieltagDto.toString());
        });
    }

    @Test
    public void checkSpiel() {
        log.debug("\n validate spiel");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, COMP_ROUND, testComp, false, 18, 17);
        testRound.setId(7L);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        testSpieltag.setId(7L);
        Team team1 = new Team("Test1", "1",true );
        Team team2 = new Team("Test2", "2",true );
        Team team3 = new Team("Test3", "3", true);
        Team team4 = new Team("Test4", "4", true);
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
            assertNotNull(spielDto.getId());
            assertTrue(spielDto.getSpielNumber() != 0);
            assertNotNull(spielDto.getAnpfiffdate());
            assertTrue(spielDto.getHeimTore() != 0);
            assertTrue(spielDto.getGastTore() != 0);
            assertNotNull(spielDto.getSpieltagId());
            assertNotNull(spielDto.getSpieltagNumber());
            assertNotNull(spielDto.getHeimTeamId());
            assertNotNull(spielDto.getHeimTeamAcronym());
            assertNotNull(spielDto.getGastTeamId());
            assertNotNull(spielDto.getGastTeamAcronym());
        }
    }


    @Test
    public void checkCompTeam() {
        log.debug("\n validate spiel");

        CompetitionFamily testFamily = new CompetitionFamily(TEST_FAM, TEST_FAM_DESCR, true, true,  Country.GERMANY);
        testFamily.setId(10L);
        Competition testComp = new Competition(TEST_COMP, TEST_COMP_DESCR, 3, 1, testFamily);
        testComp.setId(5L);

        Team team1 = new Team("Test1", "1", true);
        Team team2 = new Team("Test2", "2", true);
        Team team3 = new Team("Test3", "3", true);
        Team team4 = new Team("Test4", "4", true);
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
            assertNotNull(dto.getId());

            assertNotNull(dto.getTeamId());
            assertNotNull(dto.getTeamAcronym());

            assertNotNull(dto.getCompId());
            assertNotNull(dto.getCompName());
        }
    }

    @Test
    public void checkTipperRole() {
        String COMP_NAME = "1. Bundesliga Saison 2025/26";
        String COMM_NAME = "Bulitipper";

        final ModelMapper myMapper = MapperUtil.getModelMapperForTipperRole();

        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true,  Country.GERMANY);
        Competition testComp = new Competition(COMP_NAME, "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testComp.setId(4L);

        CompetitionRole competitionRole = new CompetitionRole(COMP_NAME, "Meine Test Rolle", testComp);
        competitionRole.setId(5L);
        testComp.addCompetitionRole(competitionRole);


        Community testComm = new Community(COMM_NAME, "Beschreibung");
        testComm.setId(3L);
        CommunityRole communityRole = new CommunityRole(COMM_NAME, "", testComm);
        communityRole.setId(6L);
        testComm.addCommunityRole(communityRole);

        Tipper testTipper = new Tipper("Eckhard", "Kirschning", "Eckhardo", "root", "hint", "eki@gmx.de");
        testTipper.setId(7L);
        TipperRole compTipperRole = new TipperRole(competitionRole, testTipper);
        compTipperRole.setId(1L);
        TipperRole communityTipperRole = new TipperRole(communityRole, testTipper);
        communityTipperRole.setId(2L);

        TipperRoleDto myCompRole = myMapper.map(compTipperRole, TipperRoleDto.class);
        assertNotNull(myCompRole.getId());
        assertNotNull(myCompRole.getRoleId());
        assertNotNull(myCompRole.getRoleName());
        assertNotNull(myCompRole.getTipperId());
        assertNotNull(myCompRole.getTipperUserName());

        TipperRoleDto myCommRole = myMapper.map(communityTipperRole, TipperRoleDto.class);
        assertNotNull(myCommRole.getId());
        assertNotNull(myCommRole.getRoleId());
        assertNotNull(myCommRole.getRoleName());
        assertNotNull(myCommRole.getTipperId());
        assertNotNull(myCommRole.getTipperUserName());
    }

    @Test
    public void checkCompetitionRole() {
        String COMP_NAME = "1. Bundesliga Saison 2025/26";
        String COMM_NAME = "Bulitipper";

        final ModelMapper myCompRoleMapper = MapperUtil.getModelMapperForCompetitionRole();

        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true,  Country.GERMANY);
        Competition testComp = new Competition(COMP_NAME, "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testComp.setId(4L);

        CompetitionRole competitionRole = new CompetitionRole(COMP_NAME, "Meine Test Rolle", testComp);
        competitionRole.setId(5L);
        testComp.addCompetitionRole(competitionRole);

        CompetitionRoleDto testRoleDto = myCompRoleMapper.map(competitionRole, CompetitionRoleDto.class);
        assertNotNull(testRoleDto.getId());
        assertNotNull(testRoleDto.getName());
        assertNotNull(testRoleDto.getDescription());
        assertNotNull(testRoleDto.getCompetitionId());
        assertNotNull(testRoleDto.getCompetitionName());
        assertEquals(testComp.getName(), testRoleDto.getName());
        assertEquals(competitionRole.getCompetition().getName(), testRoleDto.getCompetitionName());
        assertEquals(competitionRole.getCompetition().getId(), testRoleDto.getCompetitionId());
    }

    @Test
    public void checkCommunityRole() {
        String COMM_NAME = "Bulitipper";

        final ModelMapper myCommRoleMapeer = MapperUtil.getModelMapperForCommunityRole();

        Community community = new Community(COMM_NAME, "Beschreibung");
        CommunityRole communityRole = new CommunityRole(COMM_NAME, "Meine Test Rolle", community);
        communityRole.setId(5L);
        community.addCommunityRole(communityRole);

        CommunityRoleDto testRoleDto = myCommRoleMapeer.map(communityRole, CommunityRoleDto.class);
        assertNotNull(testRoleDto.getId());
        assertNotNull(testRoleDto.getName());
        assertNotNull(testRoleDto.getDescription());
        assertEquals(community.getName(), testRoleDto.getName());
        assertEquals(communityRole.getCommunity().getName(), testRoleDto.getCommunityName());
        assertEquals(communityRole.getCommunity().getId(), testRoleDto.getCommunityId());
    }

}
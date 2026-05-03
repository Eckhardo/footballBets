package sportbets.testdata;

import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

public class TestConstants {

    public static final String COMM_TEST = "Test Community";
    public static final String COMM_TEST_2 = "Test Community 2";
    public static final String COMP_FAM_TEST = "TEST Liga";
    public static final String COMP_TEST = "TEST Liga Saison 2026";
    public static final  String COMP_TEST_2 = "TEST Liga Saison 2027";
    public  static final  String COMP_ROUND_TEST= "Test Comp Round";
    public static final  String TEAM_NAME_TEST1 = "TestName";

    public static final  String TEAM_NAME_TEST2 = "TestName2";
    public static final  String TEAM_NAME_TEST3 = "TestName3";

    public static final  CompetitionFamily BUNDESLIGA = new CompetitionFamily(COMP_FAM_TEST, "1. Deutsche Fussball Bundesliga", true, true, Country.GERMANY);
    public static final CompetitionFamily FIFA_WM = new CompetitionFamily(COMP_TEST, "Fussball Weltmeisterschaft", false, false, Country.GERMANY);
    public static final CompetitionFamilyDto TEST_FAMILY = new CompetitionFamilyDto(null, COMP_FAM_TEST, "description of testliga", true, true, Country.GERMANY);

    public static final  CompetitionDto TEST_COMP = new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, TEST_FAMILY.getName());
    public static final CompetitionDto TEST_COMP_2 = new CompetitionDto(null, COMP_TEST_2, "Description of Competition 2", 3, 1, null, TEST_FAMILY.getName());

    public static final CompetitionRoundDto TEST_COMP_ROUND = new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, TEST_COMP.getName(), 18, 17, 1);

    public static final CommunityDto TEST_COMMUNITY = new CommunityDto(null, COMM_TEST, "Description of Community");
    public static final CommunityDto TEST_COMMUNITY_2 = new CommunityDto(null, COMM_TEST_2, "Description of Community 2");

    public static final TeamDto TEAM_DTO_1 = new TeamDto(null, TEAM_NAME_TEST1, "ZAC", true);
    public static final TeamDto TEAM_DTO_2 = new TeamDto(null,TEAM_NAME_TEST2, "RAZ", true);

    public static final  Team TEAM_1 = new Team(TEAM_NAME_TEST1, "ZAC", true);
    public static final Team TEAM_2 = new Team(TEAM_NAME_TEST2, "RAZ", true);

}

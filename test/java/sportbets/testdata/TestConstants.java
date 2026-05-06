package sportbets.testdata;

import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.time.LocalDateTime;

public class TestConstants {

    public static final String COMM_TEST = "Test Community";
    public static final String COMM_TEST_2 = "Test Community 2";
    public static final String COMP_FAM_TEST = "TEST Liga";
    public static final String COMP_TEST = "TEST Liga Saison 2026";
    public static final String COMP_TEST_2 = "TEST Liga Saison 2027";
    public static final String COMP_ROUND_TEST = "Test Comp Round";
    public static final String TEAM_NAME_TEST1 = "TestName";

    public static final String TEAM_NAME_TEST2 = "TestName2";
    public static final String TEAM_NAME_TEST3 = "TestName3";

    public static final CompetitionFamily BUNDESLIGA = new CompetitionFamily(COMP_FAM_TEST, "1. Deutsche Fussball Bundesliga", true, true, Country.GERMANY);
    public static final CompetitionFamily FIFA_WM = new CompetitionFamily(COMP_TEST, "Fussball Weltmeisterschaft", false, false, Country.GERMANY);
    public static final CompetitionFamilyDto TEST_FAMILY = new CompetitionFamilyDto(null, COMP_FAM_TEST, "description of testliga", true, true, Country.GERMANY);

    public static final CompetitionDto TEST_COMP_DTO = new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, TEST_FAMILY.getName());
    public static final CompetitionDto TEST_COMP_DTO_2 = new CompetitionDto(null, COMP_TEST_2, "Description of Competition 2", 3, 1, null, TEST_FAMILY.getName());
    public static final Competition TEST_COMP_ENTITY = new Competition(COMP_TEST, "description of comp", 3, 1, BUNDESLIGA);

    public static final CompetitionRoundDto TEST_COMP_ROUND_DTO = new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, TEST_COMP_DTO.getName(), 18, 17, 1);
    public static final CompetitionRound TEST_COMP_ROUND_ENTITY = new CompetitionRound(1, "Hinrunde", TEST_COMP_ENTITY, true, 1, 1, 1);

    public static final CommunityDto TEST_COMMUNITY_DTO = new CommunityDto(null, COMM_TEST, "Description of Community");
    public static final CommunityDto TEST_COMMUNITY_DTO_2 = new CommunityDto(null, COMM_TEST_2, "Description of Community 2");
    public static final Community TEST_COMMUNITY_ENTITY = new Community( COMM_TEST, "Description of Community");

    public static final CommunityMembership TEST_COMM_MEMB_ENTITY=new CommunityMembership(TEST_COMMUNITY_ENTITY, TipperConstants.ECKHARD);
    public static final CommunityMembershipDto TEST_COMM_MEMB_DTO=new CommunityMembershipDto(null, TipperConstants.ECKHARD_DTO.getId(),TipperConstants.ECKHARD_DTO.getUsername(), TestConstants.TEST_COMP_DTO.getId(),TestConstants.TEST_COMP_DTO.getName());

    public static final TeamDto TEAM_DTO_1 = new TeamDto(null, TEAM_NAME_TEST1, "ZAC", true);
    public static final TeamDto TEAM_DTO_2 = new TeamDto(null, TEAM_NAME_TEST2, "RAZ", true);

    public static final Team TEAM_1 = new Team(TEAM_NAME_TEST1, "ZAC", true);
    public static final Team TEAM_2 = new Team(TEAM_NAME_TEST2, "RAZ", true);


    public static final Spieltag TEST_SPIELTAG_ENTITY = new Spieltag(1, LocalDateTime.now(), TEST_COMP_ROUND_ENTITY);
    public static final SpieltagDto TEST_SPIELTAG_DTO = new SpieltagDto(null,1, LocalDateTime.now(), TEST_COMP_ROUND_DTO.getId(), TEST_COMP_ROUND_DTO.getName());

    public static final Spiel TEST_SPIEL_ENTITY = new Spiel(TEST_SPIELTAG_ENTITY, 1, LocalDateTime.now(), TEAM_1, TEAM_2, 2, 1, true);
    public static final SpielDto TEST_SPIEL_DTO= new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), TEST_SPIELTAG_DTO.getId(), TEST_SPIELTAG_DTO.getSpieltagNumber(), TEAM_DTO_1.getId(), TEAM_DTO_1.getAcronym(), TEAM_DTO_2.getId(), TEAM_DTO_2.getAcronym());
    public static final SpielDto TEST_SPIEL_DTO_2= new SpielDto(null, 2, 2, 2, true, LocalDateTime.now(), TEST_SPIELTAG_DTO.getId(), TEST_SPIELTAG_DTO.getSpieltagNumber(), TEAM_DTO_2.getId(), TEAM_DTO_2.getAcronym(), TEAM_DTO_1.getId(), TEAM_DTO_1.getAcronym());

    public static final TippModus TEST_TIPP_MODUS_ENTITY=new TippModusPoint("myNamePoint", TippModusType.TIPPMODUS_POINT, 1, TEST_COMMUNITY_ENTITY, 4);
    public static final TippModusDto TEST_TIPP_MODUS_DTO = new TippModusPointDto(null, "myNamePoint", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, TEST_COMMUNITY_DTO.getId(),  TEST_COMMUNITY_DTO.getName(),4);

    public static final Tipp TEST_TIPP_ENTITY = new Tipp(TEST_SPIEL_ENTITY,TEST_COMM_MEMB_ENTITY,TEST_TIPP_MODUS_ENTITY,4,0,0,4);
}

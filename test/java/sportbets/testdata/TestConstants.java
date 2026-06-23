package sportbets.testdata;

import sportbets.persistence.builder.TipperConstants;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.time.LocalDateTime;

public class TestConstants {

    private static final String COMM_TEST = "Test Community";
    private static final String COMM_TEST_2 = "Test Community 2";
    private static final String COMP_FAM_TEST = "TEST Liga";
    private static final String COMP_TEST = "TEST Liga Saison 2026";
    private static final String COMP_TEST_2 = "TEST Liga Saison 2027";
    private static final String COMP_ROUND_TEST = "Test Comp Round";
    private static final String TEAM_NAME_TEST1 = "TestName";
    private static final String TEAM_NAME_TEST2 = "TestName2";
    private static final String TEAM_NAME_TEST3 = "TestName3";
    public static final Tipp TEST_TIPP_ENTITY = new Tipp(createValidSpiel(), createValidCommunityMembership(), createValidTippModusPoint(), 4, 0, 0, 4);

    // Returns a fresh object every time it is called
    public static CompetitionFamilyDto createValidFamilyDto() {
        return new CompetitionFamilyDto(null, COMP_FAM_TEST, "description of testliga", true, true, Country.GERMANY);
    }

    public static CompetitionFamily createValidFamily() {
        return new CompetitionFamily(COMP_FAM_TEST, "description of testliga", true, true, Country.GERMANY);

    }

    public static Competition createValidCompetition() {
        return new Competition(COMP_TEST, "description of comp", 3, 1, createValidFamily());
    }

    public static CompetitionDto createValidCompetitionDto() {
        return new CompetitionDto(null, COMP_TEST, "Description of Competition", 3, 1, null, COMP_FAM_TEST);
    }

    public static CompetitionDto createValidCompetitionDto2() {
        return new CompetitionDto(null, COMP_TEST_2, "Description of Competition 2", 3, 1, null, COMP_FAM_TEST);

    }

    public static CompetitionRoundDto createValidCompRoundDto() {
        return new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, createValidCompetitionDto().getName(), 18, 17, 1);
    }

    public static CompetitionRoundDto createValidCompRoundDto2() {
        return new CompetitionRoundDto(null, 2, "TEST_COMP_ROUND_2", false, null, createValidCompetitionDto().getName(), 18, 17, 1);
    }

    public static CompetitionRound createValidCompRound() {
        return new CompetitionRound(1, "Hinrunde", createValidCompetition(), true, 1, 1, 1);
    }

    public static SpieltagDto createValidSpieltagDto() {
        return new SpieltagDto(null, 1, LocalDateTime.now(), null, createValidCompRoundDto().getName());

    }

    public static Spieltag createValidSpieltag() {
        return new Spieltag(1, LocalDateTime.now(), createValidCompRound());
    }

    public static SpielDto createValidSpielDto() {
        return new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, createValidTeamDto().getAcronym(), null, createValidTeamDto2().getAcronym());


    }

    public static SpielDto createValidSpielDto2() {
        return new SpielDto(null, 2, 3, 2, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, createValidTeamDto2().getAcronym(), null, createValidTeamDto().getAcronym());


    }

    public static Spiel createValidSpiel() {
        return new Spiel(createValidSpieltag(), 1, LocalDateTime.now(), createValidTeam(), createValidTeam2(), 2, 1, true);

    }

    public static TeamDto createValidTeamDto() {
        return new TeamDto(null, TEAM_NAME_TEST1, "ZAC", true);

    }

    public static TeamDto createValidTeamDto2() {
        return new TeamDto(null, TEAM_NAME_TEST2, "RAZ", true);

    }

    public static TeamDto createValidTeamDto3() {
        return new TeamDto(null, TEAM_NAME_TEST3, "PRE", true);

    }

    public static Team createValidTeam() {
        return new Team(TEAM_NAME_TEST1, "ZAC", true);
    }

    public static Team createValidTeam2() {
        return new Team(TEAM_NAME_TEST2, "RAZ", true);
    }


    public static CommunityDto createValidCommunityDto() {
        return new CommunityDto(null, COMM_TEST, "Description of Community");

    }

    public static CommunityDto createValidCommunityDto2() {
        return new CommunityDto(null, COMM_TEST_2, "Description of Community 2");


    }

    public static Community createValidCommunity() {
        return new Community(COMM_TEST, "Description of Community");
    }

    public static CommunityMembershipDto createValidCommunityMembershipDto() {
        return new CommunityMembershipDto(null, null, TipperConstants.createValidTipperDto().getUsername(), null, createValidCompetitionDto().getName());

    }

    public static CommunityMembership createValidCommunityMembership() {
        return new CommunityMembership(createValidCommunity(), TipperConstants.createValidTipper());
    }

    public static TippModusPointDto createValidTippModusPointDto() {
        return new TippModusPointDto(null, "myNamePoint", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, null, createValidCommunityDto().getName(), 4);
    }

    public static TippModusPoint createValidTippModusPoint() {
        return new TippModusPoint("myNamePoint", TippModusType.TIPPMODUS_POINT, 1, createValidCommunity(), 4);
    }

    public static TippModusTotoDto createValidTippModusTotoDto() {
        return new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, null, createValidCommunityDto().getName());
    }

    public static TippModusToto createValidTippModusToto() {
        return new TippModusToto("TotoTipp", TippModusType.TIPPMODUS_TOTO, 2, createValidCommunity());

    }

    public static TippModusResultDto createValidTippModusResultDto() {
        return new TippModusResultDto(null, "ErgebnisTest", TippModusType.TIPPMODUS_RESULT.getDisplayName(), 1, null, createValidCommunity().getName(), 3, 1);
    }

    public static TippModusResult createValidTippModusResult() {
        return new TippModusResult("myNameResult", TippModusType.TIPPMODUS_RESULT, 1, createValidCommunity(), 3, 1);

    }
}
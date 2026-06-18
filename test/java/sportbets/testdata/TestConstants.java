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

    public static final TeamDto TEAM_DTO_1 = new TeamDto(null, TEAM_NAME_TEST1, "ZAC", true);
    public static final TeamDto TEAM_DTO_2 = new TeamDto(null, TEAM_NAME_TEST2, "RAZ", true);
    public static final SpielDto TEST_SPIEL_DTO = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, TEAM_DTO_1.getAcronym(), null, TEAM_DTO_2.getAcronym());
    public static final SpielDto TEST_SPIEL_DTO_2 = new SpielDto(null, 2, 2, 2, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, TEAM_DTO_2.getAcronym(), null, TEAM_DTO_1.getAcronym());
    public static final Team TEAM_1 = new Team(TEAM_NAME_TEST1, "ZAC", true);
    public static final Team TEAM_2 = new Team(TEAM_NAME_TEST2, "RAZ", true);
    public static final Spiel TEST_SPIEL_ENTITY = new Spiel(createValidSpieltag(), 1, LocalDateTime.now(), TEAM_1, TEAM_2, 2, 1, true);
    public static final TippModus TEST_TIPP_MODUS_ENTITY = new TippModusPoint("myNamePoint", TippModusType.TIPPMODUS_POINT, 1, createValidCommunity(), 4);
    public static final Tipp TEST_TIPP_ENTITY = new Tipp(TEST_SPIEL_ENTITY, createValidCommunityMembership(), TEST_TIPP_MODUS_ENTITY, 4, 0, 0, 4);
    public static final TippModusDto TEST_TIPP_MODUS_DTO = new TippModusPointDto(null, "myNamePoint", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, null, createValidCommunityDto().getName(), 4);

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
        return new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, TEAM_DTO_1.getAcronym(), null, TEAM_DTO_2.getAcronym());


    }

    public static SpielDto createValidSpielDto2() {
        return new SpielDto(null, 2, 2, 2, true, LocalDateTime.now(), null, createValidSpieltagDto().getSpieltagNumber(), null, TEAM_DTO_2.getAcronym(), null, TEAM_DTO_1.getAcronym());


    }

    public static Spiel createValidSpiel() {
        return new Spiel(createValidSpieltag(), 1, LocalDateTime.now(), TEAM_1, TEAM_2, 2, 1, true);

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
}
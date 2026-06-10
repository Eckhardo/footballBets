package sportbets.service.initTestData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.competition.enums.Country;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.service.community.CommunityMembershipService;
import sportbets.service.community.CommunityService;
import sportbets.service.community.TipperService;
import sportbets.service.competition.*;
import sportbets.service.tipps.TippConfigService;
import sportbets.service.tipps.TippModusService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.community.TipperDto;
import sportbets.web.dto.competition.*;
import sportbets.web.dto.tipps.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ControllerTestDataService {

    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CompTeamService compTeamService;

    @Autowired
    private SpieltagService spieltagService;

    @Autowired
    TipperService tipperService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMembershipService communityMembershipService;

    @Autowired
    private CompetitionMembershipService competitionMembershipService;
    @Autowired
    private TippModusService tippModusService;
    @Autowired
    private TippConfigService tippConfigService;
    @Autowired
    private SpielService matchService;

    private static final Logger log = LoggerFactory.getLogger(ControllerTestDataService.class);
    public static final TeamDto TEAM_DTO_1 = new TeamDto(null, "TEAM_NAME_TEST1", "ZAC", true);
    public static final TeamDto TEAM_DTO_2 = new TeamDto(null, "TEAM_NAME_TEST2", "RAZ", true);

    public static final CompetitionFamilyDto TEST_COMP_FAM_DTO = new CompetitionFamilyDto(null, "COMP_FAM_TEST", "description of testliga", true, true, Country.GERMANY);
    public static final CompetitionDto TEST_COMP_DTO = new CompetitionDto(null, "COMP_TEST", "Description of Competition", 3, 1, null, TEST_COMP_FAM_DTO.getName());


    public static final CompetitionRoundDto TEST_COMP_ROUND_DTO = new CompetitionRoundDto(null, 1, "TEST_COMP_ROUND", false, null, TEST_COMP_DTO.getName(), 18, 17, 1);

    public static final SpieltagDto TEST_MATCH_DAY_DTO = new SpieltagDto(null, 1, LocalDateTime.now(), null, TEST_COMP_ROUND_DTO.getName());
    public static final SpielDto TEST_SPIEL_DTO = new SpielDto(null, 1, 3, 1, true, LocalDateTime.now(), null, TEST_MATCH_DAY_DTO.getSpieltagNumber(), null, TEAM_DTO_1.getAcronym(), null, TEAM_DTO_2.getAcronym());
    public static final SpielDto TEST_SPIEL_DTO_2 = new SpielDto(null, 2, 2, 2, true, LocalDateTime.now(), null, TEST_MATCH_DAY_DTO.getSpieltagNumber(), null, TEAM_DTO_2.getAcronym(), null, TEAM_DTO_1.getAcronym());
    public static final SpielDto TEST_SPIEL_DTO_3 = new SpielDto(null, 3, 2, 4, true, LocalDateTime.now(), null, TEST_MATCH_DAY_DTO.getSpieltagNumber(), null, TEAM_DTO_2.getAcronym(), null, TEAM_DTO_1.getAcronym());

    public static CommunityDto TEST_COMM_DTO = new CommunityDto(null, "COMM_TEST", "Description of Community");
    public static TipperDto WERNER_DTO = new TipperDto(null, "Werner", "Wernersen", "Wernerdo", "banane", "frucht", "werner@gmx.de",null);
    public static CommunityMembershipDto TEST_COMM_MEMB_DTO = new CommunityMembershipDto(null, null, WERNER_DTO.getUsername(), null, TEST_COMM_DTO.getName());
    public static CompetitionMembershipDto TEST_COMP_MEM_DTO = new CompetitionMembershipDto(null, TEST_COMP_DTO.getName(), null, TEST_COMM_DTO.getName());

    public static TippConfigDto TIPP_CONFIG_DTO = new TippConfigDto(null, null, null, TEST_MATCH_DAY_DTO.getSpieltagNumber(), null);


    public Optional<Competition> initCompWithGames() {

        CompetitionFamily savedFam = familyService.save(TEST_COMP_FAM_DTO);

        TEST_COMP_DTO.setFamilyId(savedFam.getId());
        Competition savedComp = compService.save(TEST_COMP_DTO);
        TEST_COMP_ROUND_DTO.setCompId(savedComp.getId());
        CompetitionRound savedCompRound = compRoundService.save(TEST_COMP_ROUND_DTO);
        TEST_MATCH_DAY_DTO.setCompRoundId(savedCompRound.getId());
        Spieltag savedMatchday = spieltagService.save(TEST_MATCH_DAY_DTO);
        TeamDto savedTeam1 = teamService.save(TEAM_DTO_1);
        TeamDto savedTeam2 = teamService.save(TEAM_DTO_2);

        compTeamService.save(new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getName(), true));
        compTeamService.save(new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getName(), true));

        TEST_SPIEL_DTO.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO.setHeimTeamId(savedTeam1.getId());
        TEST_SPIEL_DTO.setGastTeamId(savedTeam2.getId());
        Spiel savedSpiel = matchService.save(TEST_SPIEL_DTO);
        TEST_SPIEL_DTO_2.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO_2.setHeimTeamId(savedTeam2.getId());
        TEST_SPIEL_DTO_2.setGastTeamId(savedTeam1.getId());
        Spiel savedSpiel2 = matchService.save(TEST_SPIEL_DTO_2);

        TEST_SPIEL_DTO_3.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO_3.setHeimTeamId(savedTeam2.getId());
        TEST_SPIEL_DTO_3.setGastTeamId(savedTeam1.getId());
        Spiel savedSpiel3 = matchService.save(TEST_SPIEL_DTO_3);



        Community savedCommunity = communityService.save(TEST_COMM_DTO);

        TEST_COMP_MEM_DTO.setCommId(savedComp.getId());
        TEST_COMP_MEM_DTO.setCompId(savedComp.getId());
        CompetitionMembership savedCompMemb = competitionMembershipService.save(TEST_COMP_MEM_DTO);


        TippModusTotoDto tippModusTotoDto = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName());
        TippModusTotoDto savedTippModusToto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);
        TippModusPointDto tippModusPointDto = new TippModusPointDto(null, "PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName(), 4);
        TippModusPointDto savedTippModusPoint = (TippModusPointDto) tippModusService.save(tippModusPointDto);


        Tipper savedTipper = tipperService.save(WERNER_DTO);
        TEST_COMM_MEMB_DTO.setTipperId(savedTipper.getId());
        TEST_COMM_MEMB_DTO.setCommId(savedCommunity.getId());
        CommunityMembership savedCommunityMembership = communityMembershipService.save(TEST_COMM_MEMB_DTO);
        log.debug("tippConfigDto: {}", TIPP_CONFIG_DTO);
        TIPP_CONFIG_DTO.setSpieltagId(savedMatchday.getId());
        TIPP_CONFIG_DTO.setCompMembId(savedCompMemb.getId());
        TIPP_CONFIG_DTO.setTippModusId(savedTippModusPoint.getId());
        TippConfigDto savedTippConfig = tippConfigService.save(TIPP_CONFIG_DTO);
        return Optional.of(savedComp);

    }


    public TippRecord initCompWithGamesWitFamAndComp(CompetitionFamilyDto fam, CompetitionDto comp) {

        CompetitionFamily savedFam = familyService.save(fam);

        comp.setFamilyId(savedFam.getId());
        Competition savedComp = compService.save(comp);
        TEST_COMP_ROUND_DTO.setCompId(savedComp.getId());
        CompetitionRound savedCompRound = compRoundService.save(TEST_COMP_ROUND_DTO);
        TEST_MATCH_DAY_DTO.setCompRoundId(savedCompRound.getId());
        Spieltag savedMatchday = spieltagService.save(TEST_MATCH_DAY_DTO);
        TeamDto savedTeam1 = teamService.save(TEAM_DTO_1);
        TeamDto savedTeam2 = teamService.save(TEAM_DTO_2);

        compTeamService.save(new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam1.getId(), savedTeam1.getName(), true));
        compTeamService.save(new CompetitionTeamDto(null, savedComp.getId(), savedComp.getName(), savedTeam2.getId(), savedTeam2.getName(), true));

        TEST_SPIEL_DTO.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO.setHeimTeamId(savedTeam1.getId());
        TEST_SPIEL_DTO.setGastTeamId(savedTeam2.getId());
        Spiel savedSpiel = matchService.save(TEST_SPIEL_DTO);
        TEST_SPIEL_DTO_2.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO_2.setHeimTeamId(savedTeam2.getId());
        TEST_SPIEL_DTO_2.setGastTeamId(savedTeam1.getId());
        Spiel savedSpiel2 = matchService.save(TEST_SPIEL_DTO_2);

        TEST_SPIEL_DTO_3.setSpieltagId(savedMatchday.getId());
        TEST_SPIEL_DTO_3.setHeimTeamId(savedTeam2.getId());
        TEST_SPIEL_DTO_3.setGastTeamId(savedTeam1.getId());
        Spiel savedSpiel3 = matchService.save(TEST_SPIEL_DTO_3);


        Community savedCommunity = communityService.save(TEST_COMM_DTO);

        TEST_COMP_MEM_DTO.setCommId(savedComp.getId());
        TEST_COMP_MEM_DTO.setCompId(savedComp.getId());
        CompetitionMembership savedCompMemb = competitionMembershipService.save(TEST_COMP_MEM_DTO);


        TippModusTotoDto tippModusTotoDto = new TippModusTotoDto(null, "TotoTest", TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName());
        TippModusDto savedTippModusToto = tippModusService.save(tippModusTotoDto);
        TippModusPointDto tippModusPointDto = new TippModusPointDto(null, "PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName(), 4);
        TippModusDto savedTippModusPoint = tippModusService.save(tippModusPointDto);


        Tipper savedTipper = tipperService.save(WERNER_DTO);
        TEST_COMM_MEMB_DTO.setTipperId(savedTipper.getId());
        TEST_COMM_MEMB_DTO.setCommId(savedCommunity.getId());
        CommunityMembership savedCommunityMembership = communityMembershipService.save(TEST_COMM_MEMB_DTO);
        log.debug("tippConfigDto: {}", TIPP_CONFIG_DTO);
        TIPP_CONFIG_DTO.setSpieltagId(savedMatchday.getId());
        TIPP_CONFIG_DTO.setCompMembId(savedCompMemb.getId());
        TIPP_CONFIG_DTO.setTippModusId(savedTippModusPoint.getId());
        TippConfigDto savedTippConfig = tippConfigService.save(TIPP_CONFIG_DTO);
        return new TippRecord(savedCommunityMembership, List.of(savedSpiel, savedSpiel2,savedSpiel3), List.of(savedTippModusToto, savedTippModusPoint));

    }

    public void deleteCompWithGames(String famName) {
        familyService.deleteByName(famName);
        teamService.deleteByName(TEAM_DTO_1.getName());
        teamService.deleteByName(TEAM_DTO_2.getName());
        tipperService.deleteByUserName(WERNER_DTO.getUsername());
        communityService.deleteByName(TEST_COMM_DTO.getName());

    }

}

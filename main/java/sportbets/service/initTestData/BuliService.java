package sportbets.service.initTestData;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.common.DateUtil;
import sportbets.persistence.builder.*;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.*;
import sportbets.persistence.repository.tipps.TippConfigRepository;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static sportbets.persistence.builder.CompetitionConstants.BUNDESLIGA_NAME_2025;

@Service
public class BuliService {

    private static final Logger log = LoggerFactory.getLogger(BuliService.class);
    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionTeamRepository compTeamRepo;
    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    private CompetitionRoundRepository compRoundRepo;

    @Autowired
    private SpieltagRepository spieltagRepo;
    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private TeamRepository teamRepository;


    @Autowired
    private TipperRepository tipperRepo;
    @Autowired
    private TipperRoleRepository tipperRoleRepo;
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CommunityRepository commRepo;

    @Autowired
    private CompetitionMembershipRepository compMembRepo;

    @Autowired
    private CommunityMembershipRepository commMembRepo;

    @Autowired
    CompTableRepository compTableRepo;

    @Autowired
    TippConfigRepository tippConfigRepository;


    Competition savedComp = null;

    CompetitionRound savedHinrunde = null;
    CompetitionRound savedRückrunde = null;
    Community savedCommunity =null;
    @Transactional
    public void execute() {
        CompetitionFamily fam = familyRepo.save(CompFamilyConstants.BUNDESLIGA);
        savedComp = compRepo.save(new Competition(BUNDESLIGA_NAME_2025, "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, fam));

        savedHinrunde = compRoundRepo.save(new CompetitionRound(1, "Hinrunde", savedComp, false, 18, 17, 1));
        savedRückrunde = compRoundRepo.save(new CompetitionRound(2, "Rueckrunde", savedComp, false, 18, 17, 18));

        log.debug("save comTeams");
        compRepo.save(savedComp);

        Community community = new Community("Bulitipper", "Die Dinos des Tippens");

        savedCommunity = commRepo.save(community);
        Tipper ebi = TipperConstants.ECKHARD;
        ebi.setDefaultCompetitionId(savedComp.getId());
        ebi.setDefaultCommunityId(savedCommunity.getId());

        tipperRepo.save(ebi);

        saveRoles( ebi);

        CompetitionMembership compMemb = new CompetitionMembership(savedCommunity, savedComp);
        compMembRepo.save(compMemb);
        CommunityMembership commMemb = new CommunityMembership(savedCommunity, ebi);
        commMembRepo.save(commMemb);

        saveCompTeams();


        SortedMap<Integer, LocalDateTime> sortedMap = retrieveSpieltage();
        List<LocalDateTime> hinDates = new ArrayList<>();
        List<LocalDateTime> rueckDates = new ArrayList<>();

        for (Map.Entry<Integer, LocalDateTime> entry : sortedMap.entrySet()) {

            if (entry.getKey() <= 17) {
                hinDates.add(entry.getValue());
            } else {
                rueckDates.add(entry.getValue());
            }
        }

        List<Spieltag> spieltagHin = spieltagRepo.saveAll(SpieltagConstants.getSpieltageHinrunde(savedHinrunde, hinDates));
        List<Spieltag> spieltagRueck = spieltagRepo.saveAll(SpieltagConstants.getSpieltageRueckrunde(savedRückrunde, rueckDates));

        TippModus buliModus = new TippModusPoint("PunkteTipp",
                TippModusType.TIPPMODUS_POINT, 2, community,
                4);

        TippModus buliModus2 = new TippModusToto("TotoTipp", TippModusType.TIPPMODUS_TOTO,
                2, community);
        for (Spieltag spTagHin : spieltagHin) {
            Spieltag mySp = spieltagRepo.findByNumberWithRoundId(spTagHin.getSpieltagNumber(), savedHinrunde.getId()).orElseThrow();
            TippConfig tippConfig = new TippConfig(mySp, compMemb, buliModus);
            tippConfigRepository.save(tippConfig);


        }
        for (Spieltag sptagRueck : spieltagRueck) {
            Spieltag mySp = spieltagRepo.findByNumberWithRoundId(sptagRueck.getSpieltagNumber(), savedRückrunde.getId()).orElseThrow();
            TippConfig tippConfig = new TippConfig(mySp, compMemb, buliModus2);
            tippConfigRepository.save(tippConfig);
        }
        log.debug("save spiele:");
        List<Spiel> savedSpiele = retrieveSpiele();
        log.debug("add spielformula ::" + savedSpiele.size());
    }

    private void saveRoles(  Tipper ebi) {
        CompetitionRole competitionRole = new CompetitionRole(savedComp.getName(), savedComp.getDescription(), savedComp);
        CommunityRole communityRole = new CommunityRole(savedCommunity.getName(), savedCommunity.getDescription(), savedCommunity);

        Role savedCompetitionRole = roleRepo.save(competitionRole);
        Role savedCommmunityRole = roleRepo.save(communityRole);
        TipperRole tipperCompRole = new TipperRole(savedCompetitionRole, ebi);
        TipperRole tipperCommRole = new TipperRole(savedCommmunityRole, ebi);
        tipperRoleRepo.save(tipperCommRole);
        tipperRoleRepo.save(tipperCompRole);
    }


    private void saveCompTeams() {

        List<Team> teams = TeamConstants.getBuliTeams();
        for (Team team : teams) {
            CompetitionTeam ct = new CompetitionTeam(team, savedComp);
            savedComp.addCompetitionTeam(ct);
            teamRepository.saveAndFlush(team);
        }

    }


    SortedMap<Integer, LocalDateTime> retrieveSpieltage() {
        String filePath = "src/test/java/sportbets/testdata/bl.json";

        SortedMap<Integer, LocalDateTime> spieltags = new TreeMap<>(
                Comparator.nullsFirst(Comparator.naturalOrder())
        );

        try (FileReader reader = new FileReader(filePath)) {

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            // read value one by one manually
            System.out.println((String) jsonObject.get("name"));

            // loops the array
            JsonArray msg = (JsonArray) jsonObject.get("matches");

            int i = 1;
            String lastSpieltag = null;
            for (Object o : msg) {
                JsonObject nestedObj = (JsonObject) o;
                String spieltag = (String) nestedObj.get("round");
                if (lastSpieltag == null || !lastSpieltag.equals(spieltag)) {

                    String anpfiffTag = (String) nestedObj.get("date");
                    String time = (String) nestedObj.get("time");
                    if (time == null) {
                        time = "15:30";
                    }
                    String anpfiffDate = " 20:30";
                    LocalDateTime dt = DateUtil.formatDate(anpfiffTag + " " + time);
                    lastSpieltag = spieltag;

                    spieltags.put(i, dt);

                    i++;
                }


            }


        } catch (IOException | JsonException e) {
            System.out.println("##" + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("size::" + spieltags.size());
        for (Map.Entry<Integer, LocalDateTime> entry : spieltags.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        return spieltags;
    }

    List<Spiel> retrieveSpiele() {
        String filePath = "src/test/java/sportbets/testdata/bl.json";
        Competition comp = compRepo.findByName(BUNDESLIGA_NAME_2025).orElseThrow();

        List<Spiel> spiele = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            // read value one by one manually
            System.out.println((String) jsonObject.get("name"));

            // loops the array
            JsonArray msg = (JsonArray) jsonObject.get("matches");

            int i = 1;
            int k = 1;
            String lastSpieltag = null;
            for (Object o : msg) {
                JsonObject nestedObj = (JsonObject) o;

                String anpfiffTag = (String) nestedObj.get("date");
                String time = (String) nestedObj.get("time");
                if (time == null) {
                    time = "15:30";
                }
                LocalDateTime dt = DateUtil.formatDate(anpfiffTag + " " + time);

                String heim = (String) nestedObj.get("team1");


                String auswärts = (String) nestedObj.get("team2");
                JsonObject scores = (JsonObject) nestedObj.get("score");
                JsonArray fts = (JsonArray) scores.get("ft");
                BigDecimal heimTor = null;
                BigDecimal gastTor = null;
                if (fts != null) {
                    int j = 1;
                    for (Object ft : fts) {

                        if (j == 1) {
                            heimTor = (BigDecimal) ft;
                            j++;
                        } else if (j == 2) {
                            gastTor = (BigDecimal) ft;
                            j = 1;
                        }

                    }
                }

                //    System.out.println(dt + " - " + heim + "-  " + auswärts + " " + (heimTor != null ? heimTor.intValue() : null) + " " + (gastTor != null ? gastTor.intValue() : null));
                boolean stattgefunden = heimTor != null && gastTor != null;
                Integer homeGoals = heimTor != null ? heimTor.intValue() : 0;
                Integer guestGoals = gastTor != null ? gastTor.intValue() : 0;
                Spieltag spieltag = spieltagRepo.findByNumber(k);

                Team heimTeam = teamRepository.findByName(heim).orElseThrow();
                Team gastTeam = teamRepository.findByName(auswärts).orElseThrow();
                Spiel spiel = new Spiel(spieltag, i, dt, heimTeam, gastTeam, homeGoals, guestGoals, stattgefunden);
                spiele.add(spiel);

                int heimPoints = SpielFormula.calculatePoints(comp,
                        spiel.getHeimTore(), spiel.getGastTore(), spiel
                                .isStattgefunden());
                SpielFormula heimFormel = new SpielFormula(spiel, heimTeam.getName(), heimTeam.getAcronym(),
                        true, spiel.getHeimTore(), spiel
                        .getGastTore(), heimPoints);

                //  spielFormulaRepo.save(heimFormel);
                int gastPoints = SpielFormula.calculatePoints(comp,
                        spiel.getGastTore(), spiel.getHeimTore(), spiel
                                .isStattgefunden());
                SpielFormula gastFormel = new SpielFormula(spiel, gastTeam.getName(), gastTeam.getAcronym(),
                        false, spiel.getGastTore(), spiel
                        .getHeimTore(), gastPoints);

                //  spiele.add(spiel);


                if (i % 9 == 0) {

                    log.info("" + k);
                    k++;
                }
                i++;
            }


        } catch (IOException | JsonException e) {
            System.out.println("##" + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("size::" + spiele.size());

        return spielRepo.saveAll(spiele);
    }
}


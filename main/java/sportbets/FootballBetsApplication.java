package sportbets;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sportbets.common.DateUtil;
import sportbets.persistence.builder.*;
import sportbets.persistence.entity.*;
import sportbets.persistence.repository.*;
import sportbets.service.TeamService;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class FootballBetsApplication {

    private static final Logger log = LoggerFactory.getLogger(FootballBetsApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(FootballBetsApplication.class, args);
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

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
    private TeamService teamService;


    @Bean
    public CommandLineRunner run() {
        return runner -> {

//            CompetitionFamily fam = familyRepo.save(CompFamilyConstants.BUNDESLIGA);
//            Competition comp = compRepo.save(CompetitionConstants.getBundesliga2025(fam));
//            fam.addCompetition(comp);
//
//            CompetitionRound hinRunde = compRoundRepo.save(CompRoundConstants.getHinrunde(comp));
//            CompetitionRound rueckRunde = compRoundRepo.save(CompRoundConstants.getRueckrunde(comp));
//            comp.addCompetitionRound(hinRunde);
//            comp.addCompetitionRound(rueckRunde);
//            Team bay = TeamConstants.BAY;
//            Team hsv = TeamConstants.HSV;
//            Team pauli = TeamConstants.PAULI;
//            Team werder = TeamConstants.WERDER;
//            Team bvb = TeamConstants.BVB;
//            Team koe = TeamConstants.KOELN;
//            Team wob = TeamConstants.WOLFSBURG;
//            Team lev = TeamConstants.LEVERKUSEN;
//            Team vfb = TeamConstants.STUTTGART;
//            Team freiburg = TeamConstants.FREIBURG;
//            Team frankfurt = TeamConstants.FRANKFURT;
//            Team hoffenheim = TeamConstants.HOFFENHEIM;
//            Team augsburg = TeamConstants.AUGSBURG;
//            Team union = TeamConstants.UNION;
//            Team mainz = TeamConstants.MAINZ;
//            Team heidenheim = TeamConstants.HEIDENHEIM;
//            Team leipzig = TeamConstants.LEIPZIG;
//            Team gladbach = TeamConstants.GLADBACH;
//
//            //  testGroup = new CompetitionGroup("Gruppe A", 1, testRound);
//
//            CompetitionTeam ct1 = new CompetitionTeam(bay, comp);
//            bay.addCompetitionTeam(ct1);
//            CompetitionTeam ct2 = new CompetitionTeam(hsv, comp);
//            hsv.addCompetitionTeam(ct2);
//            CompetitionTeam ct3 = new CompetitionTeam(pauli, comp);
//            pauli.addCompetitionTeam(ct3);
//            CompetitionTeam ct4 = new CompetitionTeam(werder, comp);
//            werder.addCompetitionTeam(ct4);
//            CompetitionTeam ct5 = new CompetitionTeam(bvb, comp);
//            bvb.addCompetitionTeam(ct5);
//            CompetitionTeam ct6 = new CompetitionTeam(koe, comp);
//            koe.addCompetitionTeam(ct6);
//            CompetitionTeam ct7 = new CompetitionTeam(wob, comp);
//            wob.addCompetitionTeam(ct7);
//            CompetitionTeam ct8 = new CompetitionTeam(lev, comp);
//            lev.addCompetitionTeam(ct8);
//            CompetitionTeam ct9 = new CompetitionTeam(vfb, comp);
//            vfb.addCompetitionTeam(ct9);
//            CompetitionTeam ct10 = new CompetitionTeam(freiburg, comp);
//            freiburg.addCompetitionTeam(ct10);
//            CompetitionTeam ct11 = new CompetitionTeam(frankfurt, comp);
//            frankfurt.addCompetitionTeam(ct11);
//            CompetitionTeam ct12 = new CompetitionTeam(hoffenheim, comp);
//            hoffenheim.addCompetitionTeam(ct12);
//            CompetitionTeam ct13 = new CompetitionTeam(augsburg, comp);
//            augsburg.addCompetitionTeam(ct13);
//            CompetitionTeam ct14 = new CompetitionTeam(union, comp);
//            union.addCompetitionTeam(ct14);
//            CompetitionTeam ct15 = new CompetitionTeam(mainz, comp);
//            mainz.addCompetitionTeam(ct15);
//            CompetitionTeam ct16 = new CompetitionTeam(heidenheim, comp);
//            heidenheim.addCompetitionTeam(ct16);
//            CompetitionTeam ct17 = new CompetitionTeam(leipzig, comp);
//            leipzig.addCompetitionTeam(ct17);
//            CompetitionTeam ct18 = new CompetitionTeam(gladbach, comp);
//            gladbach.addCompetitionTeam(ct18);
//
//            comp.addCompetitionTeam(ct1);
//            comp.addCompetitionTeam(ct2);
//            comp.addCompetitionTeam(ct3);
//            comp.addCompetitionTeam(ct4);
//
//            comp.addCompetitionTeam(ct5);
//            comp.addCompetitionTeam(ct6);
//            comp.addCompetitionTeam(ct7);
//            comp.addCompetitionTeam(ct8);
//
//            comp.addCompetitionTeam(ct9);
//            comp.addCompetitionTeam(ct10);
//            comp.addCompetitionTeam(ct11);
//            comp.addCompetitionTeam(ct12);
//
//            comp.addCompetitionTeam(ct13);
//            comp.addCompetitionTeam(ct14);
//            comp.addCompetitionTeam(ct15);
//            comp.addCompetitionTeam(ct16);
//            comp.addCompetitionTeam(ct17);
//            comp.addCompetitionTeam(ct18);
//
//
//            compTeamRepo.saveAllAndFlush(List.of(ct1, ct2, ct3, ct4, ct5, ct6, ct7, ct8, ct9, ct10, ct11, ct12, ct13, ct14, ct15, ct16, ct17, ct18));
//
//
//            SortedMap<Integer, LocalDateTime> spieltage = retrieveSpieltage();
//            List<LocalDateTime> hinDates = new ArrayList<>();
//            List<LocalDateTime> rueckDates = new ArrayList<>();
//
//            for (Map.Entry<Integer, LocalDateTime> entry : spieltage.entrySet()) {
//
//                if (entry.getKey() <= 17) {
//                    hinDates.add(entry.getValue());
//                } else {
//                    rueckDates.add(entry.getValue());
//                }
//            }
//            log.info("hin ::" + hinDates.size());
//            log.info("rueck ::" + rueckDates.size());
//
//            List<Spieltag> spieltagHin = spieltagRepo.saveAll(SpieltagConstants.getSpieltageHinrunde(hinRunde, hinDates));
//            List<Spieltag> spieltagRueck = spieltagRepo.saveAll(SpieltagConstants.getSpieltageRueckrunde(rueckRunde, rueckDates));
//
//            List<Spiel> spiele = retrieveSpiele();
//            for (Spiel spiel : spiele) {
//               log.info(" spiele:: {} {}", spiel.getSpielNumber(), spiel.getSpieltag().getSpieltagNumber());
//
//            }

       ///     List<Spiel> spieleHin = spielRepo.saveAll(SpielConstants.getSpieleHinrunde(spieltagHin, pauli, hsv));



       //     List<Spiel> spieleRueck = spielRepo.saveAll(SpielConstants.getSpieleRückrunde(spieltagRueck, werder, bay));


            System.out.println("Save all cascade");
        };
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
                    System.out.println(spieltag + " - " + anpfiffTag + time);
                    LocalDateTime dt = DateUtil.formatDate(anpfiffTag + " " + time);
                    System.out.println(spieltag + " - " + dt.toString().replace("T", " "));
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

        List<Spiel> spiele = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            // read value one by one manually
            System.out.println((String) jsonObject.get("name"));

            // loops the array
            JsonArray msg = (JsonArray) jsonObject.get("matches");

            int i = 1;
            int k =1;
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
                        ;

                    }
                }

            //    System.out.println(dt + " - " + heim + "-  " + auswärts + " " + (heimTor != null ? heimTor.intValue() : null) + " " + (gastTor != null ? gastTor.intValue() : null));
                boolean stattgefunden = true;
                if (heimTor == null || gastTor == null) {
                    stattgefunden = false;
                }
                Integer homeGoals = heimTor != null ? heimTor.intValue() : 0;
                Integer guestGoals = gastTor != null ? gastTor.intValue() : 0;

                Spieltag spieltag= spieltagRepo.findByNumber(k);

                Team heimTeam = teamService.findByName(heim).orElseThrow();
                Team gastTeam = teamService.findByName(auswärts).orElseThrow();
                Long ID = (long) i;
                spiele.add(new Spiel(spieltag, i, dt, heimTeam, gastTeam, homeGoals, guestGoals, stattgefunden));
//                (Spieltag spieltag, int spielNumber, LocalDateTime startDate,
//                        Team heimTeam, Team gastTeam, Integer heimTore, Integer gastTore,
//                        Boolean stattgefunden

                if (i % 9 == 0) {

                    log.info(""+ k);
                    k++;
                }
                i++;
            }
             spielRepo.saveAll(spiele);

        } catch (IOException | JsonException e) {
            System.out.println("##" + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("size::" + spiele.size());

        return spiele;
    }
}


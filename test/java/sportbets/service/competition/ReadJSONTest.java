package sportbets.service.competition;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sportbets.common.DateUtil;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class ReadJSONTest {

    private static final Logger log = LoggerFactory.getLogger(ReadJSONTest.class);
    @Autowired
    private TeamService teamService;

    @Autowired


    private SpielRepository spielRepo;

    @Autowired
    private SpieltagRepository spieltagRepo;
    String filePath = "src/test/java/sportbets/testdata/bl.json";

    @BeforeEach
    public void setup() {
        log.info("setup");

    }

    @AfterEach
    public void tearDown() {
        log.info("tearDown");
    }

    @Test
    void retrieveTeams() {

        Set<Team> teams = new HashSet<>();

        try (FileReader reader = new FileReader(filePath)) {

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            // read value one by one manually
            System.out.println((String) jsonObject.get("name"));

            // loops the array
            JsonArray msg = (JsonArray) jsonObject.get("matches");
            int i = 1;
            for (Object o : msg) {
                JsonObject nestedObj = (JsonObject) o;
                String heim = (String) nestedObj.get("team1");


                String auswärts = (String) nestedObj.get("team2");
                System.out.println(heim + " - " + auswärts);
                Team team = teamService.findByName(heim).orElseThrow();
                teams.add(team);
                System.out.println("##" + team);
                i++;

                if (i == 10) {
                    break;
                }

            }


        } catch (IOException | JsonException e) {
            System.out.println("##" + e.getMessage());
            throw new RuntimeException(e);
        }


        log.info("size::" + teams.size());
        // return teams;
    }

    @Test
    void retrieveSpiele() {

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


        System.out.println("size::" + spiele.size());


    }

    @Test
    void retrieveSpieltage() {

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
        List<LocalDateTime> hinDates= new ArrayList<>();
        List<LocalDateTime> rueckDates= new ArrayList<>();

        for (Map.Entry<Integer, LocalDateTime> entry : spieltags.entrySet()) {

            if(entry.getKey()<=17){
                hinDates.add(entry.getValue());
            }else{
                rueckDates.add(entry.getValue());
            }
        }
        log.info("hin ::" + hinDates.size());
        log.info("rueck ::" + rueckDates.size());
        // return teams;
    }
}

package sportbets.persistence.repository.competition;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.competition.Team;

import java.io.FileReader;
import java.io.IOException;


@DataJpaTest()
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamRepositoryTest {

    @Autowired
    CompetitionRepository compRepo;
    @Autowired
    private TeamRepository teamRepo;

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }

    @Test
    public void myTest() {
        System.out.println("");
    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenGroupsAreFound() {
        String filePath = "src/test/java/sportbets/testdata/bl.json";
        try (FileReader reader = new FileReader(filePath)) {

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            // read value one by one manually
            System.out.println((String) jsonObject.get("name"));

            // loops the array
            JsonArray msg = (JsonArray) jsonObject.get("matches");
//            JsonObject jsonObject2 = new JsonObject();
//            jsonObject2.put("data", msg);
//            System.out.println(jsonObject2.toJson());
            // Result: {"data":["element1","element2"]}
            int i = 1;
            for (Object o : msg) {
                JsonObject nestedObj = (JsonObject) o;
                String heim = (String) nestedObj.get("team1");


                String auswärts = (String) nestedObj.get("team1");
                System.out.println(heim + " - " + auswärts);
                Team team = teamRepo.findByName(heim).orElseThrow();
                System.out.println("##" +team);
                i++;

                if (i == 10) {
                    break;
                }

            }


        } catch (IOException | JsonException e) {
            System.out.println("##" + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}

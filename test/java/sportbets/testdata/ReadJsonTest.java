package sportbets.testdata;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.TeamRepository;

import java.io.FileReader;
import java.io.IOException;

@Component
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadJsonTest {
@Autowired
    private TeamRepository teamRepository;


    @Test
    public void makeIt() {
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


                String auswärts = (String) nestedObj.get("team2");
                System.out.println(heim + " - " + auswärts);
                i++;
                Team team = teamRepository.findByName(heim).orElseThrow();
                if (i == 10) {
                    break;
                }

            }


        } catch (IOException | JsonException e) {
            throw new RuntimeException(e);
        }

    }

}



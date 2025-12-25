package sportbets.persistence.repository.competition;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.competition.Team;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;


@DataJpaTest()
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamRepositoryTest {

    @Autowired
    CompetitionRepository compRepo;
    @Autowired
    private TeamRepository teamRepo;

    @Before
    public void setup() {
        teamRepo.save(new Team("TestTeam", "Team"));
    }

    @After
    public void tearDown() {


        //   familyRepo.deleteAll();
    }
    @Test
    public void saveTeam() {
        Team team = teamRepo.findByName("TestTeam").orElseThrow();
        assertNotNull(team);
    }

}

package sportbets.persistence.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.rowObject.CompRecord;
import sportbets.testData.CompetitionConstants;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest

@RunWith(SpringRunner.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionQueryTest {

    @Autowired
    private CompetitionRepository compRepo;

    @Test
    public void findByName()  {
        System.out.println("\n");
        System.out.println("FindByName");
        CompRecord comp = compRepo.findCompByNameAndFamily(CompetitionConstants.BUNDESLIGA_NAME_2025, 1L);
        assertNotNull(comp);
        System.out.println("Found competition : " + comp);

    }

    @Test
    public void findByNameJoinFetchRounds() {
        System.out.println("\n");
        System.out.println("findByNameJoinFetchRounds");
        Competition comp = compRepo.findByNameJoinFetchRounds(CompetitionConstants.BUNDESLIGA_NAME_2025);
        assertNotNull(comp);
        System.out.println("Found competition name: " + comp.getName());
        comp.getCompetitionRounds().forEach(compRound -> {

            System.out.println("Found competitionRound name: " + compRound.getName());
            compRound.getSpieltage().forEach(spieltag -> System.out.println("Found spieltag name: " + spieltag.getSpieltagNumber()));
        });
    }

    @Test
    public void findByNameJoinFetchRoundsAndSpieltage() {
        System.out.println("\n");
        System.out.println("findByNameJoinFetchRoundsAndSpieltage");
        Competition comp = compRepo.findByNameJoinFetchRoundsAndSpieltage(CompetitionConstants.BUNDESLIGA_NAME_2025);
        assertNotNull(comp);
        System.out.println("Found competition name: " + comp.getName());
        comp.getCompetitionRounds().forEach(compRound -> {

            System.out.println("Found competitionRound name: " + compRound.getName());
            compRound.getSpieltage().forEach(spieltag -> System.out.println("Found spieltag name: " + spieltag.getSpieltagNumber()));
        });
    }
}

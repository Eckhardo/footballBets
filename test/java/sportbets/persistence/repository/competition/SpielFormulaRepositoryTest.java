package sportbets.persistence.repository.competition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpielFormulaRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SpielFormulaRepositoryTest.class);
    private static final String COMP_NAME = "1. Bundesliga Saison 2025";
    @Autowired
    CompetitionRepository compRepo;

    @Autowired
    SpielFormulaRepository spielFormulaRepository;

    @Test
    public void givenComp_whenFindSpielFormulaForHeimCalled_then18RowsAreFetched() {

        Competition foundComp = compRepo.findByName(COMP_NAME).orElse(null);
        assertNotNull(foundComp);

        List<TeamPositionSummaryRow> rows = spielFormulaRepository.findTableForLigaModus(foundComp.getId(), true, 1L, 10L);
        assertNotNull(rows);
        assertThat(rows.size()).isGreaterThan(17);
        rows.sort(Collections.reverseOrder());
        rows.forEach(System.out::println);

    }

    @Test
    public void givenComp_whenFindSpielFormulaForGastCalled_then18RowsAreFetched() {

        Competition foundComp = compRepo.findByName(COMP_NAME).orElse(null);
        assertNotNull(foundComp);

        List<TeamPositionSummaryRow> rows = spielFormulaRepository.findTableForLigaModus(foundComp.getId(), false, 1L, 10L);
        assertNotNull(rows);
        assertThat(rows.size()).isGreaterThan(17);
        rows.sort(Collections.reverseOrder());


        rows.forEach(System.out::println);

    }

}

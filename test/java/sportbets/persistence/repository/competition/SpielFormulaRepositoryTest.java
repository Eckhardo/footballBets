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
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.Comparator;
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
    CompetitionRoundRepository compRoundRepo;

    @Autowired
    CompetitionRepository compRepo;


    @Autowired
    CompTableRepository compTableRepository;

    @Test
    public void givenComp_whenFindTableCalled_then18RowsAreFetched() {

        Competition foundComp = compRepo.findByName(COMP_NAME).orElse(null);
        assertNotNull(foundComp);
        TableSearchCriteria searchCriteria = new TableSearchCriteria(foundComp.getId(),1,17,null);

        List<TeamPositionSummaryRow> rows = compTableRepository.findTableForLigaModus(searchCriteria.getCompId(),searchCriteria.getStartSpieltag(),searchCriteria.getEndSpieltag());
        assertNotNull(rows);
        assertThat(rows.size()).isGreaterThan(17);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));

    }

    @Test
    public void givenComp_whenFindSpielFormulaForHeimCalled_then18RowsAreFetched() {

        Competition foundComp = compRepo.findByName(COMP_NAME).orElse(null);
        assertNotNull(foundComp);
        TableSearchCriteria searchCriteria = new TableSearchCriteria(foundComp.getId(),1,17,true);

        List<TeamPositionSummaryRow> rows = compTableRepository.findTableHeimOrGastForLigaModus(searchCriteria.getCompId(),searchCriteria.getStartSpieltag(),searchCriteria.getEndSpieltag(),searchCriteria.getHeimOrGast());

        assertNotNull(rows);
        assertThat(rows.size()).isGreaterThan(17);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));

    }

    @Test
    public void givenComp_whenFindTableForRoundsForLigaModusCalled_thenRowsAreFetched() {

        Competition foundComp = compRepo.findByName(COMP_NAME).orElse(null);
        assertNotNull(foundComp);
        Competition foundCompWithRounds = compRepo.findByIdJoinFetchRounds(foundComp.getId());
        assertNotNull(foundCompWithRounds);
        CompetitionRound firstRound= foundCompWithRounds.getCompetitionRounds().stream().findFirst().orElseThrow();
        log.info("firstRound  {}", firstRound);
        TableSearchCriteria searchCriteria = new TableSearchCriteria(foundComp.getId(),2,22,true);

        List<TeamPositionSummaryRow> rows = compTableRepository.findTableForLigaModus(searchCriteria.getCompId(),searchCriteria.getStartSpieltag(),searchCriteria.getEndSpieltag());

        assertNotNull(rows);
        assertThat(rows.size()).isGreaterThan(17);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }


}

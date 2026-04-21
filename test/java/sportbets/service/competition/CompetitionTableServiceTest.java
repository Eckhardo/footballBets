package sportbets.service.competition;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.repository.competition.CompTableRepository;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

/**
 * Needs Bundesliga data
 */
public class CompetitionTableServiceTest {


    private static final Logger log = LoggerFactory.getLogger(CompetitionTableServiceTest.class);


    private static final String TEST_COMP = "1. Bundesliga Saison 2025";
    @Autowired
    private CompService compService; // Real service being tested

    @Autowired
    private CompTableRepository compTableRepository;

    @Autowired
    private CompTableService compTableService;

    Competition myComp;
    CompetitionRound myRound;

    @BeforeEach
    public void setup() {

       Competition comp = compService.findByName(TEST_COMP).orElseThrow();
       myComp = compService.findByIdJoinFetchRounds(comp.getId());
        assertNotNull(myComp);
        myRound = myComp.getCompetitionRounds().stream().findFirst().orElseThrow();
        log.info("myRound  {}", myRound);

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void retrieveTableData() {
        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 1, 18,null);

        List<TeamPositionSummaryRow> rows = compTableService.findTableForLigaModus(searchCriteria);
        assertThat(18).isEqualTo(rows.size());
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }

    @Test
    public void retrieveTableDataHeim() {
        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 1, 18,true);

        List<TeamPositionSummaryRow> rows = compTableService.findTableHeimOrGastForLigaModus(searchCriteria);
        assertThat(18).isEqualTo(rows.size());
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }



    @Test
    public void retrieveTableForRound() {

        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 18, 34,true);

        List<TeamPositionSummaryRow> rows = compTableService.findTableHeimOrGastForLigaModus(searchCriteria);
        assertThat(18).isEqualTo(rows.size());
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));

    }
}

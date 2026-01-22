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
import sportbets.persistence.repository.competition.CompTableRepository;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;


import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CompetitionTableServiceTest {


    private static final Logger log = LoggerFactory.getLogger(CompetitionTableServiceTest.class);


    private static final String COMP_BL_2025 = "1. Bundesliga Saison 2025/26";
    private static final String TEST_COMP = "1. Bundesliga Saison 2025";
    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested

    @Autowired
    private CompTableRepository compTableRepository;

    @Autowired
    private CompTableService compTableService;

    Competition myComp;

    @BeforeEach
    public void setup() {

        myComp = compService.findByName(TEST_COMP).orElseThrow();
        assertNotNull(myComp);

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void retrieveTableData() {
        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 1, 10,null);

        List<TeamPositionSummaryRow> rows = compTableService.findTableForLigaModus(searchCriteria);
        assertThat(rows.size()).isEqualTo(18);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }

    @Test
    public void retrieveTableDataHeim() {
        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 1, 10,true);

        List<TeamPositionSummaryRow> rows = compTableService.findTableHeimOrGastForLigaModus(searchCriteria);
        assertThat(rows.size()).isEqualTo(18);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));
    }



    @Test
    public void retrieveTableDataHeim3() {

        TableSearchCriteria searchCriteria = new TableSearchCriteria(myComp.getId(), 1, 10,true);

        List<TeamPositionSummaryRow> rows = compTableService.findTableHeimOrGastForLigaModus2(searchCriteria.getCompId(),searchCriteria.getHeimOrGast(),searchCriteria.getStartSpieltag(),searchCriteria.getEndSpieltag() );
        assertThat(rows.size()).isEqualTo(18);
        rows.sort(Comparator.comparing(TeamPositionSummaryRow::getPoints).reversed());
        rows.forEach(row -> System.out.println(row.getTeamName() + " " + row.getPoints()));

    }
}

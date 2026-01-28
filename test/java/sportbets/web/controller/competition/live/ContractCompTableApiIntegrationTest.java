package sportbets.web.controller.competition.live;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import sportbets.FootballBetsApplication;
import sportbets.config.TestProfileLiveTest;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.web.dto.competition.search.TableSearchCriteria;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FootballBetsApplication.class, TestProfileLiveTest.class})
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractCompTableApiIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(ContractCompTableApiIntegrationTest.class);

    private static final String TEST_COMP = "1. Bundesliga Saison 2025";
    @Autowired
    WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    @Autowired
    CompetitionFamilyRepository competitionFamilyRepository;
    @Autowired
    CompetitionRepository repository;

    @Test
    @Order(1)
    void givenPreloadedData_whenSearchCompTable_thenResponseContainsCompleteCompTable() {
        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Long id = entity.getId();
        TableSearchCriteria criteria=new TableSearchCriteria(id,1,10,null);
        webClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/compTable/search")
                        .queryParam("compId",criteria.getCompId())
                        .queryParam("startSpieltag", criteria.getStartSpieltag())
                        .queryParam("endSpieltag", criteria.getEndSpieltag())
                        .queryParam("isHeimOrGast",criteria.getHeimOrGast())
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TeamPositionSummaryRow.class)
                .hasSize(18);

    }

    @Test
    @Order(2)
    void givenPreloadedData_whenSearchHeimCompTable_thenResponseContainsHeimCompTable() {
        Competition entity = repository.findByName(TEST_COMP).orElseThrow(() -> new EntityNotFoundException(TEST_COMP));
        Long id = entity.getId();
        TableSearchCriteria criteria=new TableSearchCriteria(id,1,10,true);
        webClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/compTable/search")
                        .queryParam("compId",criteria.getCompId())
                        .queryParam("startSpieltag", criteria.getStartSpieltag())
                        .queryParam("endSpieltag", criteria.getEndSpieltag())
                        .queryParam("isHeimOrGast",criteria.getHeimOrGast())
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TeamPositionSummaryRow.class)
                .hasSize(18);

    }


}

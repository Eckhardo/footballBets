package sportbets.service.competition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.builder.CompetitionConstants;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.repository.competition.SpielRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class SpielFormulaLigaModusServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SpielFormulaLigaModusServiceTest.class);

    private static final String TEST_COMP = CompetitionConstants.BUNDESLIGA_NAME_2025;
    private static final String TEST_COMP_ROUND = "Hinrunde";


    @Autowired
    private CompFamilyService familyService; // Real service being tested
    @Autowired
    private CompService compService; // Real service being tested
    @Autowired
    private CompRoundService compRoundService;

    @Autowired
    private SpieltagService spieltagService;
    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private SpielService spielService;

    Competition savedComp = null;

    CompetitionRound savedRound = null;

    Spiel savedSpiel = null;

    @BeforeEach
    public void setup() {
        log.info("setup");
        savedComp = compService.findByNameJoinFetchRounds(TEST_COMP).orElseThrow();
        assertNotNull(savedComp);
        Set<CompetitionRound> rounds = savedComp.getCompetitionRounds();
        assertNotNull(rounds);
        assertThat(rounds.size()).isEqualTo(2);
        savedRound = rounds.stream().filter((round) -> round.getName().equals(TEST_COMP_ROUND)).findFirst().orElseThrow();

        Spieltag spieltag = spieltagService.findByNumberAndRound(11, savedRound.getId()).orElseThrow();
        assertNotNull(spieltag);
        log.info("spieltag evaluated");
        List<Spiel> spiele = spielService.getAllForMatchday(spieltag.getId());
        assertThat(spiele.isEmpty()).isFalse();
        savedSpiel =spiele.get(0);
        assertNotNull(savedSpiel);
        log.info("spiel evaluated {}", savedSpiel);

        ;


    }

    @Test
    public void checkIt() {
        log.info("checkIt");
        savedSpiel.setStattgefunden(true);
        savedSpiel.setHeimTore(1);
        savedSpiel.setGastTore(1);

        SpielFormula heim = savedSpiel.getSpielFormulaForHeim().orElseThrow();
        assertNotNull(heim);
        int heimPoints = SpielFormula.calculatePoints(savedComp,
                savedSpiel.getHeimTore(), savedSpiel.getGastTore(), savedSpiel
                        .isStattgefunden());

        heim.setPoints(heimPoints);
        heim.setHeimTore(savedSpiel.getHeimTore());
        heim.setGastTore(savedSpiel.getGastTore());
        heim.calculateTrend(heim.getHeimTore(),
                heim.getGastTore(), savedSpiel.isStattgefunden());

        log.info("heim formula:: {}", heim);


        SpielFormula gast = savedSpiel.getSpielFormulaForGast().orElseThrow();
        assertNotNull(gast);
        int gastPoints = SpielFormula.calculatePoints(savedComp,
                savedSpiel.getGastTore(), savedSpiel.getHeimTore(), savedSpiel
                        .isStattgefunden());

        gast.setPoints(gastPoints);

        gast.setHeimTore(savedSpiel.getGastTore());
        gast.setGastTore(savedSpiel.getHeimTore());
        gast.calculateTrend(gast.getHeimTore(),
                gast.getGastTore(), savedSpiel.isStattgefunden());

        log.info("gast formula:: {}", gast);


        Spiel spiel = spielRepository.save(savedSpiel);
        log.info("spiel was saved:: {}", spiel);

        SpielFormula theHeim = spiel.getSpielFormulaForHeim().orElseThrow();
        SpielFormula theGast = spiel.getSpielFormulaForGast().orElseThrow();
        log.info("spielformula heim:: {}", theHeim);
        log.info("spielformula gast:: {}", theGast);


    }
}

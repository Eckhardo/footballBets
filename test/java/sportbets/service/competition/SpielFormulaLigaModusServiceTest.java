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
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.repository.competition.SpielRepository;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SpielFormulaLigaModusServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SpielFormulaLigaModusServiceTest.class);

    private static final String TEST_COMP_FAM = "1. Bundesliga";
    private static final String TEST_COMP = "Saison 2025";
    private static final String TEST_COMP_ROUND = "Hinrunde";
    private static final int TEST_MATCH_DAY = 11;


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

    Competition savedComp = null;

    CompetitionRound savedRound = null;

    Spiel savedSpiel = null;

    @BeforeEach
    public void setup() {
        log.info("setup");
        savedComp = compService.findByNameJoinFetchRounds(TEST_COMP).orElseThrow();
        assertNotNull(savedComp);
        log.info("competition found");
        Set<CompetitionRound> rounds = savedComp.getCompetitionRounds();
        log.info("competitionRounds found");
        assertNotNull(rounds);
        assertThat(rounds.size()).isEqualTo(2);
        log.info("competitionRounds evaluated");
        savedRound = rounds.stream().filter((round) -> round.getName().equals(TEST_COMP_ROUND)).findFirst().orElseThrow();

        Spieltag spieltag = spieltagService.findByNumberAndRound(11, savedRound.getId()).orElseThrow();
        assertNotNull(spieltag);
        log.info("spieltag evaluated");
        Set<Spiel> spiele = spieltag.getSpiele();
        savedSpiel = spiele.stream().filter((spiel -> spiel.getId().equals(91L))).findFirst().orElseThrow();
        assertNotNull(savedSpiel);
        log.info("spiel evaluated");
        // Mainz(15)-Hoffenheim(12) 1:1 SpielId=91, spielNumber=91

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


      Spiel spiel=  spielRepository.save(savedSpiel);
        log.info("spiel was saved:: {}", spiel);

        SpielFormula theHeim=spiel.getSpielFormulaForHeim().orElseThrow();
        SpielFormula theGast=spiel.getSpielFormulaForGast().orElseThrow();
        log.info("spielformula heim:: {}", theHeim);
        log.info("spielformula gast:: {}", theGast);


    }
}

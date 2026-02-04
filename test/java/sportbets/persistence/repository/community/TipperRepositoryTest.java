package sportbets.persistence.repository.community;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sportbets.common.Country;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest()
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TipperRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(TipperRepositoryTest.class);
    TipperRole testTipperRole;
    @Autowired
    TipperRoleRepository tRoleRepo;
    @Autowired
    RoleRepository roleRepo;
    private Tipper testTipper;
    @Autowired
    private TipperRepository tipperRepo;
    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;

    CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true,  Country.GERMANY);


    @Before
    public void setUp() {
        Competition testComp = new Competition("TestLiga: Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily, 18, 17);
        testFamily.addCompetition(testComp);
        familyRepo.save(testFamily);
        Competition savedComp = compRepo.findByName(testComp.getName()).orElseThrow();

        testTipper = new Tipper("Eckhard", "Kirschning", "TestTipper", "root", "hint", "eki@gmx.de");
         tipperRepo.save(testTipper);
        CompetitionRole testRole = new CompetitionRole("1. Bundesliga Saison 2025/26", "Meine Test Rolle", savedComp);
        testTipperRole = new TipperRole(testRole, testTipper);
        roleRepo.save(testRole);
        tRoleRepo.save(testTipperRole);
    }

    @After
    public void tearDown() {
        log.info("Deleting test tipper");
     //   tipperRepo.deleteByUsername(testTipper.getUsername());
     //   familyRepo.deleteByName(testFamily.getName());

    }

    @Test
    public void findByUsername() {
        log.debug("findByUsername");
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);
        log.debug("tipper {}", tipper);
        tipperRepo.deleteByUsername(testTipper.getUsername());
        log.debug("deleted tipper ");

    }

    @Test
    public void deleteByUsername() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);

        tipperRepo.deleteByUsername(testTipper.getUsername());
        log.debug("deleted tipper ");

    }
}
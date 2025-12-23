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
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest()
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TipperRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(TipperRepositoryTest.class);
    private CompetitionFamily testFamily;
    private Competition testComp;

    private Tipper testTipper;
    private CompetitionRole testRole;
    TipperRole testTipperRole;
    @Autowired
    private TipperRepository tipperRepo;

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    TipperRoleRepository tRoleRepo;
    @Autowired
    RoleRepository roleRepo;

    @Before
    public void setUp() {
        testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true);
        testComp = new Competition("TestLiga: Saison 2025/26", "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        familyRepo.save(testFamily);
        Competition savedComp = compRepo.findByName(testComp.getName()).orElseThrow();

        testTipper = new Tipper("Eckhard", "Kirschning", "Eckhardo", "root", "hint", "eki@gmx.de", savedComp.getId());
        testTipper = tipperRepo.save(testTipper);
        testRole = new CompetitionRole("TestRolle", "Meine Test Rolle", savedComp);
        testTipperRole = new TipperRole(testRole, testTipper);
        roleRepo.save(testRole);
        tRoleRepo.save(testTipperRole);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void findByUsername() {
        log.info("findByUsername");
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);
        log.info("tipper {}", tipper);

        List<TipperRole> tipperRoles1 = tRoleRepo.getAllForTipper(tipper.getId());
        for (TipperRole role : tipperRoles1) {
            assertNotNull(role);
            log.info("tipper role={}", role.getRole());
            assertEquals(role.getRolle().getName(), testTipperRole.getRolle().getName());
        }
        List<Role> roles= roleRepo.getAllCompRolesForTipper(tipper.getId());
        for (Role role : roles) {
            assertNotNull(role);
            log.info("role={}", role);
            assertEquals(role.getName(), testRole.getName());
        }
    }

    @Test
    public void deleteByUsername() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);

        tipperRepo.deleteByUsername(testTipper.getUsername());

    }
}
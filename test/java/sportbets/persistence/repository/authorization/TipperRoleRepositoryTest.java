package sportbets.persistence.repository.authorization;

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
import sportbets.common.Country;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.SpielRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest()
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TipperRoleRepositoryTest {
    public static final String TESTUSER = "Testuser";
    private static final Logger log = LoggerFactory.getLogger(TipperRoleRepositoryTest.class);
    private static final String COMP_NAME = "TEST Saison 2025/26";
    private static final String COMM_NAME = "TEST_COMM";
    TipperRole compTipperRole;
    TipperRole communityTipperRole;
    @Autowired
    TipperRoleRepository tRoleRepo;
    @Autowired
    RoleRepository roleRepo;
    private Tipper testTipper;
    @Autowired
    private TipperRepository tipperRepo;
    @Autowired
    private CommunityRepository commRepo;
    @Autowired
    private SpielRepository spielRepo;
    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionRepository compRepo;

    @Before
    public void setUp() {
        CompetitionFamily testFamily = new CompetitionFamily("TestLiga", "1. Deutsche Fussball Bundesliga", true, true, Country.GERMANY);
        Competition testComp = new Competition(COMP_NAME, "2. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testFamily.addCompetition(testComp);
        CompetitionRole competitionRole = new CompetitionRole(COMP_NAME, "Meine Test Rolle", testComp);
        testComp.addCompetitionRole(competitionRole);
        familyRepo.save(testFamily);

        Competition savedComp = compRepo.findByName(testComp.getName()).orElseThrow();


        Community testComm = new Community(COMM_NAME, "Beschreibung");
        CommunityRole communityRole = new CommunityRole(COMM_NAME, "", testComm);
        testComm.addCommunityRole(communityRole);
        Community savedCommunity = commRepo.save(testComm);


        testTipper = new Tipper("Eckhard", "Kirschning", TESTUSER, "root", "hint", "eki@gmx.de");
        Tipper savedTipper = tipperRepo.save(testTipper);
        compTipperRole = new TipperRole(competitionRole, savedTipper);
        communityTipperRole = new TipperRole(communityRole, savedTipper);
        roleRepo.saveAll(List.of(communityRole, competitionRole));
        tRoleRepo.saveAll(List.of(communityTipperRole, compTipperRole));
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getAllTipperRolesForTipper() {
        log.debug("findByUsername");
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);

        List<TipperRole> tipperRoles1 = tRoleRepo.getAllForTipper(tipper.getId());
        for (TipperRole role : tipperRoles1) {
            assertNotNull(role);
            log.debug("TIPPER ROLE ={}", role.getRole());
            //   assertEquals(role.getRolle().getName(), compTipperRole.getRolle().getName());
        }

    }

    @Test
    public void getAllCompetitionRolesForTipper() {
        log.debug("findByUsername");
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);
        List<CompetitionRole> roles = roleRepo.getAllCompRoles();
        assertThat(roles.size()).isGreaterThan(0);
        for (CompetitionRole role : roles) {
            // assertEquals(role.getName(), competitionRole.getName());
            assertInstanceOf(CompetitionRole.class, role);
        }
    }

    @Test
    public void getAllCommunityRolesForTipper() {
        log.debug("findByUsername");
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);
        List<CommunityRole> roles = roleRepo.getAllCommunityRoles();
        assertThat(roles.size()).isGreaterThan(0);
        for (CommunityRole role : roles) {
            // assertEquals(role.getName(), competitionRole.getName());
            assertInstanceOf(CommunityRole.class, role);
        }
    }

    @Test
    public void getAllTippers() {
        List<Tipper> tippers = tipperRepo.findAll();

        assertThat(tippers.size()).isGreaterThan(0);

    }

    @Test
    public void getAllRolesForTipper() {
        Tipper tipper = tipperRepo.findByUsername(testTipper.getUsername()).orElseThrow();
        assertNotNull(tipper);

        List<Role> roles = roleRepo.findRolesByTipperId(tipper.getId());
        assertThat(roles.size()).isGreaterThan(0);

    }
}

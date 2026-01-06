package sportbets.service.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.community.Community;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommunityRoleServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityRoleServiceTest.class);
    private static final String TEST_COMM = "Test Community";
    Community savedCommunity = null;

    @Autowired
    private CommunityService communityService; // Real service being tested
    @Autowired
    private CommunityRoleService communityRoleService;



    @BeforeEach
    public void setup() {

        CommunityDto compDto = new CommunityDto(null, TEST_COMM, "Description of Community");

        savedCommunity = communityService.save(compDto);
        assertNotNull(savedCommunity);


    }
    @AfterEach
    public void tearDown() {

    }

    @Test
    public void saveCommunityRole() {
        log.debug("saveCompRole");
        CommunityRole communityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();

        assertEquals(savedCommunity.getName(), communityRole.getName());
        assertEquals(savedCommunity.getId(), communityRole.getCommunity().getId());
        assertEquals(savedCommunity.getName(), communityRole.getCommunity().getName());
        Community myComm = communityService.findByIdTest(savedCommunity.getId()).orElseThrow();
        assertThat(myComm.getCommunityRoles()).isNotNull();
    }


    @Test
    public void findAllCommunityRoles() {
        log.debug("findCommunityRoles");

        List<CommunityRole> roles = communityRoleService.getAllCommunityRoles();
        assertThat(roles).isNotNull();
        CommunityRole savedRole= roles.stream().filter( (r) -> r.getName().equals(TEST_COMM)).findFirst().get();

        assertEquals(savedCommunity.getId(), savedRole.getCommunity().getId());
        assertEquals(savedCommunity.getName(), savedRole.getCommunity().getName());
    }

}

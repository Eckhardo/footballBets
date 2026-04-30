package sportbets.service.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.community.Community;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.testdata.TestConstants.COMM_TEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class CommunityRoleServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityRoleServiceTest.class);

    Community savedCommunity = null;

    @Autowired
    private CommunityService communityService; // Real service being tested
    @Autowired
    private CommunityRoleService communityRoleService;
    CommunityDto communityDto =  new CommunityDto(null, COMM_TEST, "Description of Community");

    @BeforeEach
    public void setup() {

        savedCommunity = communityService.save(communityDto);
        assertNotNull(savedCommunity);


    }

    @AfterEach
    public void tearDown() {
        communityService.deleteById(savedCommunity.getId());
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
        CommunityRole savedRole = roles.stream().filter((r) -> r.getName().equals(communityDto.getName())).findFirst().get();

        assertEquals(savedCommunity.getId(), savedRole.getCommunity().getId());
        assertEquals(savedCommunity.getName(), savedRole.getCommunity().getName());
    }

}

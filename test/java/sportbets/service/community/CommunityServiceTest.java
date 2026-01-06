package sportbets.service.community;

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
import sportbets.persistence.entity.community.Community;
import sportbets.service.authorization.CommunityRoleService;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommunityServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityServiceTest.class);
    private static final String TEST_COMM = "Test Community";
    private static final String TEST_COMM_2 = "My Test Community 2";
    @Autowired
    CommunityService communityService;
    @Autowired
    private CommunityRoleService communityRoleService;


    @BeforeEach
    public void setup() {
        log.debug("setup");
    }
    @AfterEach
    public void tearDown() {
        log.debug("Tear down");
    }

    @Test
    void whenValidComm_thenCommShouldBeSaved() {

        CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
        Community saved = communityService.save(communityDto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(TEST_COMM);
        List<CommunityRole> roles = communityRoleService.getAllCommunityRoles();
        CommunityRole savedRole= roles.stream().filter( (r) -> r.getName().equals(TEST_COMM)).findFirst().get();


        assertEquals(saved.getId(), savedRole.getCommunity().getId());
        assertEquals(saved.getName(), savedRole.getCommunity().getName());


    }

    @Test
    void whenValidComm_thenCommShouldBeUpdated() {

        CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
        Community saved = communityService.save(communityDto);


        communityDto.setName(TEST_COMM_2);
        communityDto.setId(saved.getId());
        Community updatedComp = communityService.update(saved.getId(), communityDto).orElseThrow();

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo(TEST_COMM_2);
        List<CommunityRole> roles = communityRoleService.getAllCommunityRoles();
        assertThat(roles).isNotNull();
        CommunityRole savedRole= roles.stream().filter( (r) -> r.getName().equals(TEST_COMM_2)).findFirst().get();

        assertEquals(updatedComp.getId(),savedRole.getCommunity().getId());

        assertEquals(updatedComp.getName(), savedRole.getCommunity().getName());
        assertEquals(updatedComp.getName(), savedRole.getName());
    }
}

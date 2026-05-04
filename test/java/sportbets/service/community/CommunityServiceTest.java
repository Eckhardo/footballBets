package sportbets.service.community;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.community.Community;
import sportbets.service.authorization.CommunityRoleService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommunityServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityServiceTest.class);

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
        communityService.deleteByName(TestConstants.TEST_COMMUNITY_DTO.getName());
    }

    @Test
    @Order(1)
    void whenValidComm_thenCommShouldBeSaved() {
        CommunityDto communityDto = TestConstants.TEST_COMMUNITY_DTO;


        Community saved = communityService.save(communityDto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(communityDto.getName());
        List<CommunityRole> roles = communityRoleService.getAllCommunityRoles();
        CommunityRole savedRole = roles.stream().filter((r) -> r.getName().equals(communityDto.getName())).findFirst().get();


        assertEquals(saved.getId(), savedRole.getCommunity().getId());
        assertEquals(saved.getName(), savedRole.getCommunity().getName());


    }

    @Test
    @Order(2)
    void whenValidComm_thenCommShouldBeUpdated() {
        CommunityDto communityDto = TestConstants.TEST_COMMUNITY_DTO;

        Community saved = communityService.save(communityDto);


        communityDto.setName("TEST_COMM_2");
        communityDto.setId(saved.getId());
        Community updatedComp = communityService.update(saved.getId(), communityDto).orElseThrow();

        assertThat(updatedComp.getId()).isNotNull();
        assertThat(updatedComp.getName()).isEqualTo("TEST_COMM_2");
        List<CommunityRole> roles = communityRoleService.getAllCommunityRoles();
        assertThat(roles).isNotNull();
        CommunityRole savedRole = roles.stream().filter((r) -> r.getName().equals("TEST_COMM_2")).findFirst().get();

        assertEquals(updatedComp.getId(), savedRole.getCommunity().getId());

        assertEquals(updatedComp.getName(), savedRole.getCommunity().getName());
        assertEquals(updatedComp.getName(), savedRole.getName());
    }
}

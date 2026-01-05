package sportbets.service.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.service.authorization.CommunityRoleService;
import sportbets.service.authorization.CompetitionRoleService;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.competition.CompetitionMembershipDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CompetitionMembershipServiceTest {


    private static final Logger log = LoggerFactory.getLogger(CompetitionMembershipServiceTest.class);
    @Autowired
    CompetitionMembershipService membershipService;
    @Autowired
    private CompetitionRoleService compRoleService;
    @Autowired
    private CompFamilyService familyService; // Real service being tested


    @Autowired
    CommunityService communityService;
    @Autowired
    private CommunityRoleService communityRoleService;

    @Autowired
    CompService compService;


    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMM = "Test Community";
    private static final String TEST_COMM_2 = "My Test Community 2";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    private static final String TEST_COMP_2 = "TestLiga 2: Saison 2025";
    final CompetitionFamilyDto competitionFamily = new CompetitionFamilyDto(null, TEST_COMP_FAM, "description of testliga", true, true);

    Competition savedComp = null;
    Competition savedComp2 = null;
    Community savedCommunity = null;
    Community savedCommunity2 =null;

    @BeforeEach
    public void setup() {
        CompetitionFamily savedFam = familyService.save(competitionFamily).orElseThrow();

        CompetitionDto compDto = new CompetitionDto(null, TEST_COMP, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        CompetitionDto compDto2 = new CompetitionDto(null, TEST_COMP_2, "Description of Competition", 3, 1, savedFam.getId(), TEST_COMP_FAM);
        savedComp = compService.save(compDto);
        savedComp2 = compService.save(compDto2);

        CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
        CommunityDto communityDto2 = new CommunityDto(null, TEST_COMM_2, "Description of Community2");
        savedCommunity = communityService.save(communityDto);
        savedCommunity2 = communityService.save(communityDto2);
    }

    @AfterEach
    public void tearDown() {
        familyService.deleteByName(TEST_COMP_FAM);
        communityService.deleteAll();

    }
    @Test
    public void whenCompMembIsCreated_thenSuccess(){

        CompetitionMembershipDto competitionMembershipDto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(),savedCommunity.getName());
        CompetitionMembership savedCommunityMembership = membershipService.save(competitionMembershipDto);
        assertNotNull(savedCommunityMembership);
        assertEquals(savedCommunity.getId(), savedCommunityMembership.getCommunity().getId());
        assertEquals(savedCommunity.getName(), savedCommunityMembership.getCommunity().getName());
        assertEquals(savedComp.getId(), savedCommunityMembership.getCompetition().getId());
        assertEquals(savedComp.getName(), savedCommunityMembership.getCompetition().getName());

    }
    @Test
    public void updateCompMembership() {
        log.debug("updateCompMembership");

        assertNotNull(savedComp);
        assertNotNull(savedCommunity);

        CompetitionMembershipDto dto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(),savedCommunity.getName());
        CompetitionMembership savedCompMemb = membershipService.save(dto);
        assertNotNull(savedCompMemb);
        assertEquals(savedCommunity.getId(), savedCompMemb.getCommunity().getId());
        assertEquals(savedCommunity.getName(), savedCompMemb.getCommunity().getName());
        assertEquals(savedComp.getId(), savedCompMemb.getCompetition().getId());
        assertEquals(savedComp.getName(), savedCompMemb.getCompetition().getName());

        // update comp

        dto.setId(savedCompMemb.getId());
        dto.setCompId(savedComp2.getId());
        dto.setCompName(savedComp2.getName());
        CompetitionMembership updated = membershipService.update(dto.getId(), dto).orElseThrow();
        assertEquals(savedCompMemb.getId(), updated.getId());
        assertNotNull(updated);
        assertEquals(savedComp2.getId(), updated.getCompetition().getId());
        assertEquals(savedComp2.getName(), updated.getCompetition().getName());
        assertEquals(savedCommunity.getId(), updated.getCommunity().getId());
        assertEquals(savedCommunity.getName(), updated.getCommunity().getName());

        // update community

        dto.setCommId(savedCommunity2.getId());
        dto.setCommName(savedCommunity2.getName());
        CompetitionMembership updatedComp = membershipService.update(dto.getId(), dto).orElseThrow();
        assertNotNull(updatedComp);
        assertEquals(savedCompMemb.getId(), updatedComp.getId());
        assertEquals(savedComp2.getId(), updatedComp.getCompetition().getId());
        assertEquals(savedComp2.getName(), updatedComp.getCompetition().getName());
        assertEquals(savedCommunity2.getId(), updatedComp.getCommunity().getId());
        assertEquals(savedCommunity2.getName(), updatedComp.getCommunity().getName());
    }
    @Test
    public void deleteCompMembership() {
        log.debug("deleteCompMembership");
        assertNotNull(savedComp);
        assertNotNull(savedCommunity);

        CompetitionMembershipDto dto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(),savedCommunity.getName());
        CompetitionMembership savedCompMemb = membershipService.save(dto);
        assertNotNull(savedCompMemb);

        membershipService.deleteById(savedCompMemb.getId());
        Optional<CompetitionMembership> deleted = membershipService.findById(savedCompMemb.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    public void whenDeleteCommunity_thenRoleIsDeletedAndMembershipIsDeleted() {
        log.debug("deleteCommunity");
        assertNotNull(savedComp);
        assertNotNull(savedCommunity);
        CommunityRole savedCommunityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();
        assertNotNull(savedCommunityRole);


        CommunityRole communityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();
        assertNotNull(communityRole);
        CompetitionMembershipDto dto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(),savedCommunity.getName());
        CompetitionMembership savedCompMemb = membershipService.save(dto);
        assertNotNull(savedCompMemb);

        communityService.deleteById(savedCommunity.getId());
        log.debug("community deleted");
        Optional<Community> deletedComm = communityService.findById(savedCommunity.getId());
        assertTrue(deletedComm.isEmpty());
        Optional<CommunityRole> deletedRole = communityRoleService.findByCommunityName(savedCommunity.getName());
        assertTrue(deletedRole.isEmpty());
        Optional<CompetitionMembership> deleted = membershipService.findById(savedCompMemb.getId());
        assertTrue(deleted.isEmpty());

    }

    @Test
    public void whenDeleteCompetition_thenRoleIsDeletedAndMembershipIsDeleted() {
        log.debug("deletCompetition");
        assertNotNull(savedComp);
        assertNotNull(savedCommunity);
        CompetitionRole competitionRole = compRoleService.findByCompName(savedComp.getName()).orElseThrow();
        assertNotNull(competitionRole);


        CommunityRole communityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();
        assertNotNull(communityRole);
        CompetitionMembershipDto dto = new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedCommunity.getId(),savedCommunity.getName());
        CompetitionMembership savedCompMemb = membershipService.save(dto);
        assertNotNull(savedCompMemb);

        compService.deleteById(savedComp.getId());
        log.debug("comp deleted");
        Optional<Competition> deletedComp = compService.findById(savedComp.getId());
        assertTrue(deletedComp.isEmpty());
        Optional<CompetitionRole> deletedRole = compRoleService.findByCompName(savedComp.getName());
        assertTrue(deletedRole.isEmpty());
        Optional<CompetitionMembership> deleted = membershipService.findById(savedCompMemb.getId());
        assertTrue(deleted.isEmpty());

    }
}

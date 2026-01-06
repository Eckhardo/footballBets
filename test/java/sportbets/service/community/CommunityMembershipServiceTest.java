package sportbets.service.community;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.service.authorization.CommunityRoleService;
import sportbets.service.authorization.TipperRoleService;
import sportbets.web.dto.authorization.TipperRoleDto;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.community.TipperDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")

public class CommunityMembershipServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CommunityMembershipServiceTest.class);
    private static final String TEST_COMM = "Test Community";
    private static final String TEST_COMM_2 = "My Test Community 2";
    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_USERNAME_2 = "TEST_USER 2";

    @Autowired
    CommunityMembershipService membershipService;
    @Autowired
    private CommunityRoleService communityRoleService;


    @Autowired
    CommunityService communityService;

    @Autowired
    TipperService tipperService;
    @Autowired
    private TipperRoleService tipperRoleService;

    TipperDto testTipper = new TipperDto(null, "Eckhard", "Kirschning", TEST_USERNAME, "root", "hint", "eki@gmx.de");

    TipperDto testTipper2 = new TipperDto(null, "Werner", "Wernersen", TEST_USERNAME_2, "root", "hint", "werner@gmx.de");

    CommunityDto communityDto = new CommunityDto(null, TEST_COMM, "Description of Community");
    CommunityDto communityDto2 = new CommunityDto(null, TEST_COMM_2, "Description of Community2");

    Tipper savedTipper = null;
    Tipper savedTipper2 = null;
    Community savedCommunity = null;
    Community savedCommunity2 = null;

    @BeforeEach
    public void setup() {
        savedTipper = tipperService.save(testTipper);
        savedTipper2 = tipperService.save(testTipper2);
        savedCommunity = communityService.save(communityDto);
        savedCommunity2 = communityService.save(communityDto2);
    }

    @AfterEach
    public void tearDown() {
        tipperService.deleteById(savedTipper.getId());
        tipperService.deleteById(savedTipper2.getId());
        communityService.deleteById(savedCommunity.getId());
        communityService.deleteById(savedCommunity2.getId());

    }


    @Test
    public void saveCommunityMembership() {
        log.debug("saveCommunityMembership");

        assertNotNull(savedTipper);
        assertNotNull(savedCommunity);
        CommunityMembershipDto dto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());

        CommunityMembership savedCommunityMembership = membershipService.save(dto);
        assertNotNull(savedCommunityMembership);
        assertEquals(savedTipper.getId(), savedCommunityMembership.getTipper().getId());
        membershipService.deleteById(savedCommunityMembership.getId());
    }


    @Test
    public void updateCommunityMembership() {
        log.debug("updateCommunityMembership");

        assertNotNull(savedTipper);
        assertNotNull(savedCommunity);

        CommunityMembershipDto dto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());

        CommunityMembership savedCommunityMembership = membershipService.save(dto);
        assertNotNull(savedCommunityMembership);
        assertEquals(savedTipper.getId(), savedCommunityMembership.getTipper().getId());
        assertEquals(savedTipper.getUsername(), savedCommunityMembership.getTipper().getUsername());
        assertEquals(savedCommunity.getId(), savedCommunityMembership.getCommunity().getId());
        assertEquals(savedCommunity.getName(), savedCommunityMembership.getCommunity().getName());

        // update tipper

        dto.setId(savedCommunityMembership.getId());
        dto.setTipperId(savedTipper2.getId());
        dto.setTipperName(savedTipper2.getUsername());
        CommunityMembership updated = membershipService.update(dto.getId(), dto).orElseThrow();
        assertEquals(savedCommunityMembership.getId(), updated.getId());
        assertNotNull(updated);
        assertEquals(savedTipper2.getId(), updated.getTipper().getId());
        assertEquals(savedTipper2.getUsername(), updated.getTipper().getUsername());
        assertEquals(savedCommunity.getId(), updated.getCommunity().getId());
        assertEquals(savedCommunity.getName(), updated.getCommunity().getName());

        // update community

        dto.setCommId(savedCommunity2.getId());
        dto.setCommName(savedCommunity2.getName());
        CommunityMembership updatedComm = membershipService.update(dto.getId(), dto).orElseThrow();
        assertNotNull(updatedComm);
        assertEquals(savedCommunityMembership.getId(), updatedComm.getId());
        assertEquals(savedTipper2.getId(), updatedComm.getTipper().getId());
        assertEquals(savedTipper2.getUsername(), updatedComm.getTipper().getUsername());
        assertEquals(savedCommunity2.getId(), updatedComm.getCommunity().getId());
        assertEquals(savedCommunity2.getName(), updatedComm.getCommunity().getName());
        membershipService.deleteById(savedCommunityMembership.getId());
    }


    @Test
    public void deleteCommunityMembership() {
        log.debug("deleteCommunityMembership");
        assertNotNull(savedTipper);
        assertNotNull(savedCommunity);

        CommunityMembershipDto dto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());

        CommunityMembership savedCommunityMembership = membershipService.save(dto);
        assertNotNull(savedCommunityMembership);
        membershipService.deleteById(savedCommunityMembership.getId());
        Optional<CommunityMembership> deleted = membershipService.findById(savedCommunityMembership.getId());
        assertTrue(deleted.isEmpty());

    }


    @Test
    public void whenDeleteCommunity_thenRoleIsDeletedAndMembershipIsDeleted() {
        log.debug("deleteCommunity");
        assertNotNull(savedTipper);
        assertNotNull(savedCommunity);
        CommunityRole savedCommunityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();
        assertNotNull(savedCommunityRole);


        CommunityRole communityRole = communityRoleService.findByCommunityName(communityDto.getName()).orElseThrow();
        assertNotNull(communityRole);
        CommunityMembershipDto dto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());

        CommunityMembership savedCommunityMembership = membershipService.save(dto);
        assertNotNull(savedCommunityMembership);
        communityService.deleteById(savedCommunity.getId());
        log.debug("community deleted");
        Optional<Community> deletedComm = communityService.findById(savedCommunity.getId());
        assertTrue(deletedComm.isEmpty());
        Optional<CommunityRole> deletedRole = communityRoleService.findByCommunityName(communityDto.getName());
        assertTrue(deletedRole.isEmpty());
        Optional<CommunityMembership> deleted = membershipService.findById(savedCommunityMembership.getId());
        assertTrue(deleted.isEmpty());

    }

    @Test
    public void whenDeletedTipper_thenTipperRoleIsDeletedAndMembershipIsDeleted() {
        log.debug("deleteTipper");

        assertNotNull(savedTipper);
        assertNotNull(savedCommunity);
        CommunityRole savedCommunityRole = communityRoleService.findByCommunityName(savedCommunity.getName()).orElseThrow();

        TipperRoleDto tipperRoleDto = new TipperRoleDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunityRole.getId(), savedCommunityRole.getName());


        log.debug("\n");
        TipperRole savedTipperRole = tipperRoleService.save(tipperRoleDto).orElseThrow(() -> new EntityNotFoundException("savedTipperRole not found"));
        assertNotNull(savedTipperRole);

        CommunityMembershipDto dto = new CommunityMembershipDto(null, savedTipper.getId(), savedTipper.getUsername(), savedCommunity.getId(), savedCommunity.getName());

        CommunityMembership savedCommunityMembership = membershipService.save(dto);
        assertNotNull(savedCommunityMembership);
        tipperService.deleteById(savedTipper.getId());
        log.debug("tipper deleted");
        Optional<Tipper> deletedTipper = tipperService.findById(savedTipper.getId());
        assertTrue(deletedTipper.isEmpty());
        Optional<CommunityMembership> deleted = membershipService.findById(savedCommunityMembership.getId());
        assertTrue(deleted.isEmpty());
        Optional<TipperRole> deletedTipperRole = tipperRoleService.findById(savedTipperRole.getId());
        assertTrue(deletedTipperRole.isEmpty());

    }
}

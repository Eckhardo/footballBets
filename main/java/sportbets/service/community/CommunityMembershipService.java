package sportbets.service.community;

import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.web.dto.community.CommunityMembershipDto;

import java.util.List;
import java.util.Optional;


public interface CommunityMembershipService {
    Optional<CommunityMembership> findById(Long id);

    CommunityMembership save(CommunityMembershipDto communityMembershipDto);

    Optional<CommunityMembership> update(Long id, CommunityMembershipDto commDto);

    void deleteById(Long id);
    List<CommunityMembership> getAll();
    List<CommunityMembership> findCommunities(Long tipperId);
    Optional<CommunityMembership> findByCommIdAndTipperId(Long commId, Long tipperId);
}

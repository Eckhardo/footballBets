package sportbets.service.community;

import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Competition;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;
import java.util.Optional;

public interface CommunityService {

    Optional<Community> findById(Long id);

    Optional<Community> findByName(String name);

    Community save(CommunityDto communityDto);

    Optional<Community> update(Long id, CommunityDto commDto);

    void deleteById(Long id);

    void deleteByName(String name);

    void deleteAll();

    List<Community> getAll();
    Optional<Community> findByIdTest(Long id);

}

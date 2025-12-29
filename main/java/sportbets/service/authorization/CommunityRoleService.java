package sportbets.service.authorization;

import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.web.dto.authorization.CommunityRoleDto;

import java.util.List;
import java.util.Optional;

public interface CommunityRoleService {

    Optional<CommunityRole> findById(Long id);


    CommunityRole save(CommunityRoleDto dto);


    Optional<CommunityRole> findByCommunityName(String name);
    List<CommunityRole> getAllCommunityRoles();

    void deleteByName(String name);

    void deleteById(Long id);

}

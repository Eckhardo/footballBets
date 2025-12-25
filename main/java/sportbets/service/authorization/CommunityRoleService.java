package sportbets.service.authorization;

import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.web.dto.authorization.CommunityRoleDto;

import java.util.List;
import java.util.Optional;

public interface CommunityRoleService {

    Optional<CommunityRoleDto> findById(Long id);


    Optional<CommunityRoleDto> save(CommunityRoleDto dto);

    Optional<CommunityRoleDto> update(Long id, CommunityRoleDto dto);
    List<CommunityRoleDto> getAllCommunityRoles();

    void  deleteByName(String name);
    void deleteById(Long id);

}

package sportbets.service.authorization;

import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.web.dto.authorization.CompetitionRoleDto;

import java.util.List;
import java.util.Optional;

public interface CompetitionRoleService {
    Optional<CompetitionRole> findById(Long id);

    CompetitionRole save(CompetitionRoleDto dto);


    List<CompetitionRole> getAllCompRoles();

    void deleteById(Long id);

    void deleteByName(String name);
}

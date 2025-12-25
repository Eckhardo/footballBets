package sportbets.service.authorization;

import sportbets.web.dto.authorization.CompetitionRoleDto;

import java.util.List;
import java.util.Optional;

public interface CompetitionRoleService {
    Optional<CompetitionRoleDto> findById(Long id);

    CompetitionRoleDto save(CompetitionRoleDto dto);


    List<CompetitionRoleDto> getAllCompRoles();

    void deleteById(Long id);

    void deleteByName(String name);
}

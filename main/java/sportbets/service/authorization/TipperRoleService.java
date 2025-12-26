package sportbets.service.authorization;

import sportbets.web.dto.authorization.TipperRoleDto;

import java.util.List;
import java.util.Optional;

public interface TipperRoleService {
    List<TipperRoleDto> getAllForTipper(Long tipperId);

    Optional<TipperRoleDto> save(TipperRoleDto dto);


    void deleteAll();

    void deleteById(Long id);
}

package sportbets.service.authorization;

import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.web.dto.authorization.TipperRoleDto;

import java.util.List;
import java.util.Optional;

public interface TipperRoleService {
    List<TipperRole> getAllForTipper(Long tipperId);

    Optional<TipperRole> save(TipperRoleDto dto);


    void deleteAll();

    void deleteById(Long id);
}

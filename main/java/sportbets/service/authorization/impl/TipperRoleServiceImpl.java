package sportbets.service.authorization.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.service.authorization.TipperRoleService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.authorization.TipperRoleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class TipperRoleServiceImpl implements TipperRoleService {

    private static final Logger log = LoggerFactory.getLogger(TipperRoleServiceImpl.class);
    private final TipperRoleRepository tipperRoleRepo;

    private final TipperRepository tipperRepository;
    private final RoleRepository roleRepository;

    public TipperRoleServiceImpl(TipperRoleRepository tipperRoleRepo, TipperRepository tipperRepository, RoleRepository roleRepository) {
        this.tipperRoleRepo = tipperRoleRepo;
        this.tipperRepository = tipperRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public Optional<TipperRole> save(TipperRoleDto dto) {
        log.info("\n");
        log.info("TipperRoleServiceImpl.save {}", dto);
        Tipper tipper = tipperRepository.findById(dto.getTipperId()).orElseThrow(() -> new EntityNotFoundException("Tipper not found"));
        Role compRole = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new EntityNotFoundException("Role not found"));

        TipperRole tipperRole = new TipperRole(compRole, tipper);
        TipperRole model = tipperRoleRepo.save(tipperRole);
        log.info("model {}", model);

        return Optional.of(model);
    }

    /**
     * @param tipperId
     * @return
     */
    @Override
    public List<TipperRole> getAllForTipper(Long tipperId) {
        List<TipperRole> tipperRoles = tipperRoleRepo.getAllForTipper(tipperId);

        log.info("return all tipperRole dtos::");
        return tipperRoles;
    }

    /**
     *
     */
    @Override
    public void deleteAll() {
        tipperRoleRepo.deleteAll();
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        tipperRoleRepo.deleteById(id);
    }
}

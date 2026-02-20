package sportbets.service.authorization.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.service.authorization.CommunityRoleService;
import sportbets.web.dto.authorization.CommunityRoleDto;

import java.util.List;
import java.util.Optional;
@Service
public class CommunityRoleServiceImpl implements CommunityRoleService {

    private static final Logger log = LoggerFactory.getLogger(CommunityRoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final CommunityRepository compRepo;
    private final ModelMapper modelMapper;

    public CommunityRoleServiceImpl(RoleRepository roleRepository, CommunityRepository compRepo, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.compRepo = compRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunityRole> findById(Long id) {
        Role model = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        log.debug("\n");
        if (model instanceof CommunityRole) {
            return Optional.of((CommunityRole) model);
        } else {
            return Optional.empty();
        }
    }


    @Override
    @Transactional
    public CommunityRole save(CommunityRoleDto dto) {
        log.debug(" to be save:: {}", dto);
        Optional<CommunityRole> savedRole = roleRepository.findByCommunityName(dto.getName());
        if (savedRole.isPresent()) {
            throw new EntityExistsException("CompetitionRole  already exist with given name:" + savedRole.get().getName());
        }
        Community community = compRepo.findById(dto.getCommunityId()).orElseThrow(() -> new EntityNotFoundException("Community not found"));

        CommunityRole model = modelMapper.map(dto, CommunityRole.class);
        model.setCommunity(community);
        log.debug("model be save:: {}", model);
        CommunityRole createdModel = roleRepository.save(model);
        log.debug("saved entity:: {}", createdModel);

        return createdModel;

    }


    @Override
    public Optional<CommunityRole> findByCommunityName(String name) {
        return roleRepository.findByCommunityName(name);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CommunityRole> getAllCommunityRoles() {
        return roleRepository.getAllCommunityRoles();
    }


    @Override
    @Transactional
    public void deleteByName(String name) {
        roleRepository.deleteByName(name);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}

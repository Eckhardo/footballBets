package sportbets.service.authorization.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.authorization.CompetitionRoleService;
import sportbets.web.dto.authorization.CompetitionRoleDto;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionRoleServiceImpl implements CompetitionRoleService {

    private static final Logger log = LoggerFactory.getLogger(CompetitionRoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final CompetitionRepository compRepo;
    private final ModelMapper modelMapper;

    public CompetitionRoleServiceImpl(RoleRepository roleRepository, CompetitionRepository compRepo, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.compRepo = compRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public Optional<CompetitionRole> findById(Long id) {
        log.debug("CompetitionRoleServiceImpl.findById");
        Role model = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        log.debug("\n");
        if (model instanceof CompetitionRole) {

            return Optional.of((CompetitionRole) model);
        } else {
            return Optional.empty();
        }
    }


    @Override
    @Transactional
    public CompetitionRole save(CompetitionRoleDto dto) {
        log.debug("FmDto to be save:: {}", dto);
        Optional<CompetitionRole> savedRole = roleRepository.findByCompName(dto.getName());
        if (savedRole.isPresent()) {
            throw new EntityExistsException("CompetitionRole  already exist with given name:" + savedRole.get().getName());
        }
        Competition comp = compRepo.findById(dto.getCompetitionId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));

        CompetitionRole model = modelMapper.map(dto, CompetitionRole.class);
        model.setCompetition(comp);
        log.debug("model be save:: {}", model);
        CompetitionRole createdModel = roleRepository.save(model);
        log.debug("saved entity:: {}", createdModel);

        return createdModel;

    }


    @Override
    @Transactional(readOnly = true)
    public List<CompetitionRole> getAllCompRoles() {


        return roleRepository.getAllCompRoles();
    }


    @Override
    public Optional<CompetitionRole> findByCompName(String name) {
      return roleRepository.findByCompName(name);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteByName(String name) {
        roleRepository.deleteByName(name);
    }
}

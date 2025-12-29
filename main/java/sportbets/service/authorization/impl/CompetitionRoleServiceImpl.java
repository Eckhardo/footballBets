package sportbets.service.authorization.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.authorization.CompetitionRoleService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.authorization.CompetitionRoleDto;

import java.util.ArrayList;
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

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<CompetitionRole> findById(Long id) {
        log.info("\n");
        log.info("CompetitionRoleServiceImpl.findById");
        Role model = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tipper not found"));
        log.info("\n");
        if (model instanceof CompetitionRole) {

            return Optional.of((CompetitionRole) model);
        } else if (model instanceof CommunityRole) {

            throw new RuntimeException("Gheört hier nicht hn: eigene Mehtode für CompRole UND CommRole");

        } else {
            return Optional.empty();
        }
    }

    /**
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public CompetitionRole save(CompetitionRoleDto dto) {
        log.info("FmDto to be save:: {}", dto);
        Optional<Role> savedRole = roleRepository.findByName(dto.getName());
        if (savedRole.isPresent()) {
            throw new EntityExistsException("CompetitionRole  already exist with given name:" + savedRole.get().getName());
        }
        Competition comp = compRepo.findById(dto.getCompetitionId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));

        CompetitionRole model = modelMapper.map(dto, CompetitionRole.class);
        model.setCompetition(comp);
        log.info("model be save:: {}", model);
        CompetitionRole createdModel = roleRepository.save(model);
        log.info("saved entity:: {}", createdModel);

        return createdModel;

    }


    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompetitionRole> getAllCompRoles() {
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRole();
        List<CompetitionRoleDto> competitionRoleDtos = new ArrayList<>();
        log.info("\n");
        List<CompetitionRole> compRoles = roleRepository.getAllCompRoles();
        log.info("\n");

        return compRoles;
    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * @param name
     */
    @Override
    @Transactional
    public void deleteByName(String name) {
        roleRepository.deleteByName(name);
    }
}

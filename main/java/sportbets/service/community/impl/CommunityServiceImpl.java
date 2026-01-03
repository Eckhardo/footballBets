package sportbets.service.community.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {

    private static final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);
    private final CommunityRepository communityRepo;
    private final RoleRepository roleRepo;
    private final ModelMapper modelMapper;

    public CommunityServiceImpl(CommunityRepository communityRepo, RoleRepository roleRepo, ModelMapper modelMapper) {
        this.communityRepo = communityRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Community> findById(Long id) {
        return communityRepo.findById(id);
    }


    @Override
    public Optional<Community> findByName(String name) {
        return communityRepo.findByName(name);
    }


    @Override
    @Transactional
    public Community save(CommunityDto communityDto) {
        log.info("Community to be save:: {}", communityDto);
        Optional<Community> savedCommunity = communityRepo.findByName(communityDto.getName());
        if (savedCommunity.isPresent()) {
            throw new EntityExistsException("Community already exist with given name:" + communityDto.getName());
        }

        Community model = modelMapper.map(communityDto, Community.class);
        CommunityRole communityRole = new CommunityRole(model.getName(), model.getDescription(), model);
        model.addCommunityRole(communityRole);
        log.info("model be save:: {}", model);
        Community createdModel = communityRepo.save(model);
        log.info("saved entity:: {}", createdModel);
        return createdModel;
    }


    @Override
    @Transactional
    public Optional<Community> update(Long id, CommunityDto commDto) {
        log.info("update:: {}", commDto);
        Optional<Community> updateModel = communityRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("Community  DOES NOT exist with given id:" + id);
        }


        Optional<CommunityRole> oldCommunityRole = updateModel.get().getCommunityRoleByName(updateModel.get().getName());
        if (oldCommunityRole.isPresent()) {


           oldCommunityRole.get().setName(commDto.getName());
           oldCommunityRole.get().setDescription(commDto.getDescription());
        }
        updateModel.get().setName(commDto.getName());
        updateModel.get().setDescription(commDto.getDescription());

        log.info("updated Community  with {}", updateModel.get());
        return Optional.of(communityRepo.save(updateModel.get()));
    }




    @Override
    @Transactional
    public void deleteById(Long id) {
        communityRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        communityRepo.deleteByName(name);
    }

    @Override
    public void deleteAll() {
        communityRepo.deleteAll();
    }

    @Override
    public List<Community> getAll() {
        return communityRepo.findAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Community> findByIdTest(Long id) {
        return communityRepo.findById(id);
    }
}

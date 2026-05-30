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
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {

    private static final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);
    private final CommunityRepository communityRepo;
    private final ModelMapper modelMapper;

    public CommunityServiceImpl(CommunityRepository communityRepo,  ModelMapper modelMapper) {
        this.communityRepo = communityRepo;

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
        log.debug("Community to be save:: {}", communityDto);
        Optional<Community> savedCommunity = communityRepo.findByName(communityDto.getName());
        if (savedCommunity.isPresent()) {
            throw new EntityExistsException("Community already exist with given name:" + communityDto.getName());
        }

        Community entity = modelMapper.map(communityDto, Community.class);
        CommunityRole communityRole = new CommunityRole(entity.getName(), entity.getDescription(), entity);
        entity.addCommunityRole(communityRole);
        return communityRepo.save(entity);

    }


    @Override
    @Transactional
    public Optional<Community> update(Long id, CommunityDto commDto) {
        log.debug("update:: {}", commDto);
        Community updateModel = communityRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Community  DOES NOT exist with given id:" + id) );

        Optional<CommunityRole> oldCommunityRole = updateModel.getCommunityRoleByName(updateModel.getName());
        if (oldCommunityRole.isPresent()) {
            oldCommunityRole.get().setName(commDto.getName());
            oldCommunityRole.get().setDescription(commDto.getDescription());
        }
        updateModel.setName(commDto.getName());
        updateModel.setDescription(commDto.getDescription());


        return Optional.of(communityRepo.save(updateModel));
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

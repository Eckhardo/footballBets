package sportbets.service.community.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    public CommunityServiceImpl(CommunityRepository communityRepo, ModelMapper modelMapper) {
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
    public Community save(CommunityDto communityDto) {
        log.info("Community to be save:: {}", communityDto);
        Optional<Community> savedCommunity = communityRepo.findByName(communityDto.getName());
        if (savedCommunity.isPresent()) {
            throw new EntityExistsException("Community already exist with given username:" + communityDto.getName());
        }
        Community model = modelMapper.map(communityDto, Community.class);
        log.info("model be save:: {}", model);
        Community createdModel = communityRepo.save(model);
        log.info("saved entity:: {}", createdModel);
        return createdModel;
    }


    @Override
    public Optional<Community> update(Long id, CommunityDto commDto) {
        log.info("update:: {}", commDto);
        Optional<Community> updateModel = communityRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("Community  DOES NOT exist with given id:" + id);
        }
        Community model = modelMapper.map(commDto, Community.class);

        Community updated = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", updated);
        return Optional.of(communityRepo.save(updated));
    }

    private Community updateFields(Community base, Community model) {
        base.setName(model.getName());
        base.setDescription(model.getDescription());
        return base;
    }


    @Override
    public void deleteById(Long id) {
        communityRepo.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        communityRepo.deleteByName(name);
    }


    @Override
    public List<Community> getAll() {
        return communityRepo.findAll();
    }
}

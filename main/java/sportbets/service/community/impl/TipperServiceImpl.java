package sportbets.service.community.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.service.community.TipperService;
import sportbets.web.dto.community.TipperDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class TipperServiceImpl implements TipperService {
    private static final Logger log = LoggerFactory.getLogger(TipperServiceImpl.class);
    private final TipperRepository tipperRepo;
    private final ModelMapper modelMapper;

    TipperServiceImpl(TipperRepository tipperRepo, ModelMapper modelMapper) {
        this.tipperRepo = tipperRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Tipper> findById(Long id) {
        return tipperRepo.findById(id);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Optional<Tipper> save(TipperDto dto) {
        log.info("Tipper to be save:: {}", dto);
        Optional<Tipper> savedTipper = tipperRepo.findByUsername(dto.getUsername());
        if (savedTipper.isPresent()) {
            throw new EntityExistsException("Tipper already exist with given username:" + savedTipper.get().getUsername());
        }
        Tipper model = modelMapper.map(dto, Tipper.class);
        log.info("model be save:: {}", model);
        Tipper createdModel = tipperRepo.save(model);
        log.info("saved entity:: {}", createdModel);
        return Optional.of(createdModel);

    }

    /**
     * @param dtos
     * @return
     */
    @Override
    @Transactional
    public List<Tipper> saveAll(List<TipperDto> dtos) {

        List<Tipper> savedTippers = new ArrayList<>();
        for (TipperDto tipperDto : dtos) {
            Optional<Tipper> entity = tipperRepo.findByUsername(tipperDto.getUsername());

            if (entity.isPresent()) {
                log.error("Tipper already exists");
                throw new EntityExistsException("CompTeam  already exist with given username:" + tipperDto.getUsername());
            }
            Tipper model = modelMapper.map(tipperDto, Tipper.class);

            savedTippers.add(tipperRepo.save(model));
        }
        return savedTippers;
    }

    /**
     * @param id
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Optional<Tipper> update(Long id, TipperDto dto) {
        log.info("updateFamily:: {}", dto);
        Optional<Tipper> updateModel = tipperRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("Tipper  DOES NOT exist with given id:" + id);
        }
        Tipper model = modelMapper.map(dto, Tipper.class);

        Tipper updated = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", updated);
        return Optional.of(tipperRepo.save(updated));
    }

    private Tipper updateFields(Tipper base, Tipper model) {
        base.setUsername(model.getUsername());
        base.setEmail(model.getEmail());
        base.setFirstname(model.getFirstname());
        base.setLastname(model.getLastname());
        base.setPasswort(base.getPasswort());
        base.setPasswortHint(base.getPasswortHint());
        base.setDefaultCompetitionId(model.getDefaultCompetitionId());
        return base;

    }

    /**
     * @param ids
     */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        for (Long id : ids) {
            tipperRepo.deleteById(id);
        }
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        tipperRepo.deleteById(id);
    }

    /**
     * @param userName
     */
    @Override
    @Transactional
    public void deleteByUserName(String userName) {
        tipperRepo.deleteByUsername(userName);
    }

    /**
     * @param compId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Tipper> getAllFormComp(Long compId) {
        return List.of();
    }
}

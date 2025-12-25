package sportbets.service.community.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.service.community.TipperService;
import sportbets.web.dto.community.TipperDto;

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
    public Optional<TipperDto> findById(Long id) {
        return Optional.empty();
    }

    /**
     * @param dto 
     * @return
     */
    @Override
    @Transactional
    public Optional<TipperDto> save(TipperDto dto) {
        log.info("FmDto to be save:: {}", dto);
        Optional<Tipper> savedTipper = tipperRepo.findByUsername(dto.getUsername());
        if (savedTipper.isPresent()) {
            throw new EntityExistsException("Tipper already exist with given username:" + savedTipper.get().getUsername());
        }
        Tipper model = modelMapper.map(dto, Tipper.class);
        log.info("model be save:: {}", model);
        Tipper createdModel = tipperRepo.save(model);
        log.info("saved entity:: {}", createdModel);
        TipperDto tipperDto = modelMapper.map(createdModel, TipperDto.class);
        log.info("dto to return:: {}", tipperDto);
        return Optional.of(tipperDto);
    }

    /**
     * @param dtos 
     * @return
     */
    @Override
    @Transactional
    public List<TipperDto> saveAll(List<TipperDto> dtos) {
        return List.of();
    }

    /**
     * @param id 
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Optional<TipperDto> update(Long id, TipperDto dto) {
        return Optional.empty();
    }

    /**
     * @param ids 
     */
    @Override
    public void deleteAll(List<Long> ids) {

    }

    /**
     * @param id 
     */
    @Override
    public void deleteById(Long id) {

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
    public List<TipperDto> getAllFormComp(Long compId) {
        return List.of();
    }
}

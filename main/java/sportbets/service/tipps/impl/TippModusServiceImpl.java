package sportbets.service.tipps.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.service.tipps.TippModusService;
import sportbets.web.dto.MapperUtilTipps;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TippModusServiceImpl implements TippModusService {


    private static final Logger log = LoggerFactory.getLogger(TippModusServiceImpl.class);
    private final TippModusRepository repo;
    private final CommunityRepository commRepo;
    private final ModelMapper modelMapper;

    public TippModusServiceImpl(TippModusRepository repo, CommunityRepository commRepo, ModelMapper modelMapper) {
        this.repo = repo;
        this.commRepo = commRepo;
        this.modelMapper = modelMapper;

    }

    @Override
    public Optional<TippModusDto> findById(Long id) {
        TippModus entity = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("TippModus not found"));

        return Optional.of(convertToDto(entity));
    }


    @Override
    public TippModusDto save(TippModusDto dto) {
        log.info("save tippModus");
        Optional<TippModus> tippModus = repo.findByName(dto.getCommId(), dto.getName());
        if(tippModus.isPresent()) {
            throw new EntityExistsException("TippModus  already exists with name " + dto.getName());
        }
        Community community = commRepo.findById(dto.getCommId()).orElseThrow(() -> new EntityNotFoundException("Community not found"));
        final TippModus entity = convertToEntity(dto);

        entity.setType(TippModusType.fromString(dto.getType()));
        entity.setCommunity(community);
        TippModus saved = repo.save(entity);
        return convertToDto(saved);

    }


    @Override
    public Optional<TippModusDto> update(Long id, TippModusDto dto) {
        log.info("update tippModus: {}", dto);

        Community community = commRepo.findById(dto.getCommId()).orElseThrow(() -> new EntityNotFoundException("Community not found"));
        TippModus tippModus = repo.findById(id).orElseThrow(() -> new RuntimeException("TippModus not found"));
        final TippModus entity = convertToEntity(dto);
        log.info("converted dto to entity:{}", entity);
        entity.setCommunity(community);
        entity.setType(TippModusType.fromString(dto.getType()));
      //  entity.setId(id);

        TippModus updated = repo.save(entity);
        log.info("updated entity:{}", updated);
        return Optional.of(convertToDto(updated));

    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<TippModusDto> getAllForCommunity(Long id) {
        List<TippModus> entities = repo.findAllForCommunity(id);
        List<TippModusDto> dtos = new ArrayList<>();
        for (TippModus entity : entities) {
            dtos.add(convertToDto(entity));
        }
        return dtos;
    }


    @Override
    public List<TippModusTotoDto> findTotoTypesForCommunity(Long id) {
        List<TippModusToto> entities = repo.findTippModusToto(id);
        List<TippModusTotoDto> dtos = new ArrayList<>();
        MapperUtilTipps mapperUtilTipps = new MapperUtilTipps();
        ModelMapper myMapper = mapperUtilTipps.modelMapperForTotoTipp();
        for (TippModusToto entity : entities) {
            dtos.add(myMapper.map(entity, TippModusTotoDto.class));
        }
        return dtos;
    }

    @Override
    public List<TippModusResultDto> findResultTypesForCommunity(Long id) {
        List<TippModusResult> entities = repo.findTippModusResult(id);
        List<TippModusResultDto> dtos = new ArrayList<>();
        MapperUtilTipps mapperUtilTipps = new MapperUtilTipps();
        ModelMapper myMapper = mapperUtilTipps.modelMapperForResultTipp();
        for (TippModusResult entity : entities) {
            dtos.add(myMapper.map(entity, TippModusResultDto.class));
        }
        return dtos;
    }

    @Override
    public List<TippModusPointDto> findPointTypesForCommunity(Long id) {
        List<TippModusPoint> entities = repo.findTippModusPoint(id);
        List<TippModusPointDto> dtos = new ArrayList<>();
        MapperUtilTipps mapperUtilTipps = new MapperUtilTipps();
        ModelMapper myMapper = mapperUtilTipps.modelMapperForPointTipp();
        for (TippModusPoint entity : entities) {
            dtos.add(myMapper.map(entity, TippModusPointDto.class));
        }
        return dtos;
    }

    private TippModusDto convertToDto(TippModus entity) {
MapperUtilTipps myMapperUtilTipps = new MapperUtilTipps();
        if (entity instanceof TippModusToto) {
            ModelMapper myMapper = myMapperUtilTipps.modelMapperForTotoTipp();
            return myMapper.map(entity, TippModusTotoDto.class);
        } else if (entity instanceof TippModusPoint) {
            ModelMapper myMapper = myMapperUtilTipps.modelMapperForPointTipp();
            return myMapper.map(entity, TippModusPointDto.class);
        } else if (entity instanceof TippModusResult) {
            ModelMapper myMapper = myMapperUtilTipps.modelMapperForResultTipp();
            return myMapper.map(entity, TippModusResultDto.class);
        } else    throw new RuntimeException("Unknown tippModus type " + entity.getClass());

    }

    private TippModus convertToEntity(TippModusDto dto) {
        TippModus entity;
        if (dto instanceof TippModusTotoDto totoDto) {
            entity = modelMapper.map(totoDto, TippModusToto.class);
        } else if (dto instanceof TippModusPointDto pointDto) {
            entity = modelMapper.map(pointDto, TippModusPoint.class);
        } else if (dto instanceof TippModusResultDto resultDto) {
            entity = modelMapper.map(resultDto, TippModusResult.class);
        } else {
            throw new RuntimeException("Unknown tippModus type " + dto.getClass());
        }
        return entity;
    }
}
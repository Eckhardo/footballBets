package sportbets.service.tipps.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.common.TippModusType;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
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
        TippModus entity = repo.findById(id).orElseThrow(() -> new RuntimeException("TippModus not found"));

        return Optional.of(getTippModusDto(entity));
    }


    @Override
    public TippModusDto save(TippModusDto dto) {
        log.info("save tippModus");

        Community community = commRepo.findById(dto.getCommId()).orElseThrow(() -> new RuntimeException("Community not found"));
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

        entity.setType(TippModusType.fromString(dto.getType()));
        entity.setCommunity(community);
        TippModus saved = repo.save(entity);
        return getTippModusDto(saved);

    }

    @Override
    public Optional<TippModusDto> update(Long id, TippModusDto dto) {
        return Optional.empty();
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
            dtos.add(getTippModusDto(entity));
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

    private TippModusDto getTippModusDto(TippModus entity) {
        MapperUtilTipps mapperUtilTipps = new MapperUtilTipps();
        if (entity instanceof TippModusToto) {
            ModelMapper myMapper = mapperUtilTipps.modelMapperForTotoTipp();
            return myMapper.map(entity, TippModusTotoDto.class);
        } else if (entity instanceof TippModusPoint) {
            ModelMapper myMapper = mapperUtilTipps.modelMapperForPointTipp();
            return myMapper.map(entity, TippModusPointDto.class);
        } else if (entity instanceof TippModusResult) {
            ModelMapper myMapper = mapperUtilTipps.modelMapperForResultTipp();
            return myMapper.map(entity, TippModusResultDto.class);
        } else throw new RuntimeException("TippModus not found");


    }
}
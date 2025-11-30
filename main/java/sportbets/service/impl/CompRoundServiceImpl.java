package sportbets.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.repository.CompetitionRepository;
import sportbets.persistence.repository.CompetitionRoundRepository;
import sportbets.service.CompRoundService;
import sportbets.web.dto.CompetitionRoundDto;
import sportbets.web.dto.MapperUtil;

import java.util.List;
import java.util.Optional;

@Service
public class CompRoundServiceImpl implements CompRoundService {

    private static final Logger log = LoggerFactory.getLogger(CompRoundServiceImpl.class);
    private final CompetitionRoundRepository roundRepository;
    private final CompetitionRepository compRepository;

    private final ModelMapper modelMapper;

    public CompRoundServiceImpl(CompetitionRoundRepository roundRepository, CompetitionRepository compRepository, ModelMapper modelMapper) {
        this.roundRepository = roundRepository;
        this.compRepository = compRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<CompetitionRoundDto> findById(Long id) {
        Optional<CompetitionRound> model =  roundRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto compRoundDto = modelMapper.map(model, CompetitionRoundDto.class);
        return Optional.of(compRoundDto);
    }

    @Override
    public Optional<CompetitionRoundDto> save(CompetitionRoundDto compRoundDto) {
        Optional<Competition> comp =  compRepository.findById(compRoundDto.getCompId());

        if(comp.isPresent()) {
            CompetitionRound model = modelMapper.map(compRoundDto, CompetitionRound.class);
            model.setCompetition(comp.get());
            CompetitionRound createdModel = roundRepository.save(model);
            ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();
            CompetitionRoundDto createdDto = myModelMapper.map(createdModel, CompetitionRoundDto.class);
            return Optional.of(createdDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<CompetitionRoundDto> updateRound(Long id, CompetitionRoundDto dto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();


        log.info("updateRound:: {}", dto);
        Optional<CompetitionRound > updateModel= roundRepository.findById(id);
        if (updateModel.isPresent()) {
            Optional<CompetitionRound> updated = updateModel.map(base -> updateFields(base, dto))
                    .map(roundRepository::save);

            CompetitionRoundDto compRoundDto = myModelMapper.map(updateModel, CompetitionRoundDto.class);
            log.info("CompetitionRound updated  RETURN dto {}", compRoundDto);
            return Optional.ofNullable(compRoundDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        roundRepository.deleteById(id);
    }

    @Override
    public List<CompetitionRound> getAll() {
        return roundRepository.findAll();
    }


    private CompetitionRound updateFields(CompetitionRound base, CompetitionRoundDto updatedRound) {
        base.setName(updatedRound.getName());
        base.setRoundNumber(updatedRound.getRoundNumber());
        base.setHasGroups(updatedRound.getHasGroups());

        return base;
    }
}

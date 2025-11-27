package sportbets.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.CompetitionRound;
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

    @Autowired
    private ModelMapper modelMapper;

    public CompRoundServiceImpl(CompetitionRoundRepository compRepository) {
        this.roundRepository = compRepository;
    }

    @Override
    public Optional<CompetitionRoundDto> findById(Long id) {
        Optional<CompetitionRound> model =  roundRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        log.info("CompetitionRound found with {}", model);
        CompetitionRoundDto compRoundDto = modelMapper.map(model, CompetitionRoundDto.class);
        return Optional.of(compRoundDto);
    }

    @Override
    public Optional<CompetitionRoundDto> save(CompetitionRoundDto comp) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();

        CompetitionRound model =  myModelMapper.map(comp, CompetitionRound.class);

        log.info("CompetitionRound saved with {}", model);
        CompetitionRound createdModel= roundRepository.save(model);
        CompetitionRoundDto compRoundDto = modelMapper.map(createdModel, CompetitionRoundDto.class);
        log.info("CompetitionRound RETURN do {}", compRoundDto);
        return Optional.of(compRoundDto);
    }

    @Override
    public Optional<CompetitionRoundDto> updateRound(Long id, CompetitionRoundDto dto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();

        CompetitionRound model =  myModelMapper.map(dto, CompetitionRound.class);
        log.info("CompetitionRound updated  with {}", model);
        Optional<CompetitionRound > updateModel= roundRepository.findById(id)
                .map(base -> updateFields(base, model))
                .map(roundRepository::save);
        CompetitionRoundDto compRoundDto = modelMapper.map(updateModel, CompetitionRoundDto.class);
        log.info("CompetitionRound updated  RETURN dto {}", compRoundDto);
        return Optional.of(compRoundDto);
    }

    @Override
    public void deleteById(Long id) {
        roundRepository.deleteById(id);
    }

    @Override
    public List<CompetitionRound> getAll() {
        return roundRepository.findAll();
    }


    private CompetitionRound updateFields(CompetitionRound base, CompetitionRound updatedRound) {
        base.setName(updatedRound.getName());
        base.setRoundNumber(updatedRound.getRoundNumber());
        base.setHasGroups(updatedRound.isHasGroups());

        return base;
    }
}

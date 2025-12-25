package sportbets.service.competition.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionRoundRepository;
import sportbets.service.competition.CompRoundService;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.MapperUtil;

import java.util.ArrayList;
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
    @Transactional
    public Optional<CompetitionRoundDto> findById(Long id) {
        Optional<CompetitionRound> model = roundRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto compRoundDto = modelMapper.map(model, CompetitionRoundDto.class);
        return Optional.of(compRoundDto);
    }

    @Override
    @Transactional
    public Optional<CompetitionRoundDto> save(CompetitionRoundDto compRoundDto) {
        Competition comp = compRepository.findById(compRoundDto.getCompId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));


        CompetitionRound model = modelMapper.map(compRoundDto, CompetitionRound.class);
        model.setCompetition(comp);
        CompetitionRound createdModel = roundRepository.save(model);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();
        CompetitionRoundDto createdDto = myModelMapper.map(createdModel, CompetitionRoundDto.class);
        return Optional.of(createdDto);

    }

    @Override
    @Transactional
    public Optional<CompetitionRoundDto> updateRound(Long id, CompetitionRoundDto dto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();


        log.info("updateRound:: {}", dto);
        Optional<CompetitionRound> updateModel = roundRepository.findById(id);
        if (updateModel.isPresent()) {
            Optional<CompetitionRound> updated = updateModel.map(base -> updateFields(base, dto))
                    .map(roundRepository::save);
            log.info("CompetitionRound updated entity {}", updated.get());
            CompetitionRoundDto compRoundDto = myModelMapper.map(updated, CompetitionRoundDto.class);
            log.info("CompetitionRound updated  RETURN dto {}", compRoundDto);
            return Optional.ofNullable(compRoundDto);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roundRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        roundRepository.deleteByName(name);
    }



    @Override
    @Transactional
    public List<CompetitionRoundDto> getAll() {
        List<CompetitionRound> rounds = roundRepository.findAll();
        List<CompetitionRoundDto> roundDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetition();
        rounds.forEach(round -> roundDtos.add(myMapper.map(round, CompetitionRoundDto.class)));
        return roundDtos;
    }


    private CompetitionRound updateFields(CompetitionRound base, CompetitionRoundDto updatedRound) {
        base.setName(updatedRound.getName());
        base.setRoundNumber(updatedRound.getRoundNumber());
        base.setHasGroups(updatedRound.getHasGroups());

        return base;
    }
}

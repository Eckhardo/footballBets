package sportbets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.repository.competition.CompetitionRoundRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.service.SpieltagService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.SpieltagDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpieltagServiceImpl implements SpieltagService {

    private static final Logger log = LoggerFactory.getLogger(SpieltagServiceImpl.class);
    private final CompetitionRoundRepository roundRepository;
    private final SpieltagRepository spieltagRepository;

    private final ModelMapper modelMapper;

    public SpieltagServiceImpl(CompetitionRoundRepository roundRepository, SpieltagRepository spieltagRepository, ModelMapper modelMapper) {
        this.roundRepository = roundRepository;
        this.spieltagRepository = spieltagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<SpieltagDto> getAll() {
        log.info("get all matchdays::");
        List<Spieltag> matchDays = spieltagRepository.findAll();
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        final ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        matchDays.forEach(spieltag -> spieltagDtos.add(myMapper.map(spieltag, SpieltagDto.class)));
        log.info("return all matchdays::");
        return spieltagDtos;
    }


    @Override
    public Optional<SpieltagDto> findById(Long id) {
        Optional<Spieltag> model = spieltagRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetitionRound();
        SpieltagDto matchDayDto = modelMapper.map(model, SpieltagDto.class);
        return Optional.of(matchDayDto);
    }

    @Override
    @Transactional
    public Optional<SpieltagDto> save(SpieltagDto spieltagDto) {
        log.info("save spieltag:: {}", spieltagDto);
        CompetitionRound round = roundRepository.findByIdWithParents(spieltagDto.getCompRoundId()).orElseThrow(() -> new EntityNotFoundException("CompRound not found"));


        Spieltag model = modelMapper.map(spieltagDto, Spieltag.class);
        model.setCompetitionRound(round);
        Spieltag createdModel = spieltagRepository.save(model);
        log.info("saved spieltag:: {}", createdModel);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetitionRound();
        SpieltagDto createdDto = myModelMapper.map(createdModel, SpieltagDto.class);
        log.info("returned spieltagDto:: {}", createdModel);
        return Optional.of(createdDto);

    }

    @Override
    @Transactional
    public Optional<SpieltagDto> updateMatchDay(Long id, SpieltagDto spieltagDto) {


        log.info("updateMatchday:: {}", spieltagDto);
        Optional<Spieltag> updateModel = spieltagRepository.findById(id);
        if (updateModel.isPresent()) {

            Optional<Spieltag> updated = updateModel.map(base -> updateFields(base, spieltagDto))
                    .map(spieltagRepository::save);
            log.info("updated Matchday:: {}", updated);
            ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetitionRound();
            SpieltagDto matchDayDto = myModelMapper.map(updated, SpieltagDto.class);
            log.info("Matchday updated  RETURN dto {}", matchDayDto);
            return Optional.ofNullable(matchDayDto);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        spieltagRepository.deleteById(id);
    }

    @Override
    public List<SpieltagDto> getAllForRound(Long roundId) {
        List<Spieltag> matchDays = spieltagRepository.findAllByRoundId(roundId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        matchDays.forEach(spieltag -> spieltagDtos.add(myMapper.map(spieltag, SpieltagDto.class)));
        return spieltagDtos;
    }


    private Spieltag updateFields(Spieltag base, SpieltagDto updatedMatchDay) {
        base.setSpieltagNumber(updatedMatchDay.getSpieltagNumber());
        base.setStartDate(updatedMatchDay.getStartDate());

        return base;
    }
}

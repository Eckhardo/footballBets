package sportbets.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Spieltag;
import sportbets.persistence.repository.CompetitionRoundRepository;
import sportbets.persistence.repository.SpieltagRepository;
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

    private ModelMapper modelMapper;

    public SpieltagServiceImpl(CompetitionRoundRepository roundRepository, SpieltagRepository spieltagRepository, ModelMapper modelMapper) {
        this.roundRepository = roundRepository;
        this.spieltagRepository = spieltagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<SpieltagDto> findById(Long id) {
        Optional<Spieltag> model =  spieltagRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompetitionRound();
        SpieltagDto matchDayDto = modelMapper.map(model, SpieltagDto.class);
        return Optional.of(matchDayDto);
    }

    @Override
    public Optional<SpieltagDto> save(SpieltagDto spieltagDto) {
        log.info("save spieltag:: {}", spieltagDto);
        Optional<CompetitionRound> round =  roundRepository.findByIdWithParents(spieltagDto.getCompRoundId());

        if(round.isPresent()) {
            log.info("round present:: {}", round.get().getRoundNumber());
            Spieltag model = modelMapper.map(spieltagDto, Spieltag.class);
            model.setCompetitionRound(round.get());
            log.info("save matchday:: {}", model.getSpieltagNumber());
            Spieltag createdModel = spieltagRepository.save(model);
            log.info("saved matchday:: {}", model.getSpieltagNumber());
            ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetitionRound();
            SpieltagDto createdDto = myModelMapper.map(createdModel, SpieltagDto.class);
            log.info("return matchday dto:: {}", createdDto);
            return Optional.of(createdDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<SpieltagDto> updateMatchDay(Long id, SpieltagDto spieltagDto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompetition();


        log.info("updateRound:: {}", spieltagDto);
        Optional<Spieltag > updateModel= spieltagRepository.findById(id);
        if (updateModel.isPresent()) {
            Optional<Spieltag> updated = updateModel.map(base -> updateFields(base, spieltagDto))
                    .map(spieltagRepository::save);

            SpieltagDto matchDayDto = myModelMapper.map(updateModel, SpieltagDto.class);
            log.info("CompetitionRound updated  RETURN dto {}", matchDayDto);
            return Optional.ofNullable(matchDayDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        spieltagRepository.deleteById(id);
    }

    @Override
    public List<SpieltagDto> getAll(Long roundId) {
        List<Spieltag> matchDays = spieltagRepository.findAllByRoundId(roundId);
        List<SpieltagDto> spieltagDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetitionRound();
        matchDays.forEach(spieltag -> {
            spieltagDtos.add(myMapper.map(spieltag, SpieltagDto.class));
        });
        return spieltagDtos;
    }


    private Spieltag updateFields(Spieltag base, SpieltagDto updatedMatchDay) {
        base.setSpieltagNumber(updatedMatchDay.getSpieltagNumber());
        base.setStartDate(updatedMatchDay.getStartDate());

        return base;
    }
}

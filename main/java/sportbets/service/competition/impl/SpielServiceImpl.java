package sportbets.service.competition.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.competition.SpielService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.SpielDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpielServiceImpl implements SpielService {
    private static final Logger log = LoggerFactory.getLogger(SpielServiceImpl.class);
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;
    private final TeamRepository teamRepo;
    private final ModelMapper modelMapper;

    public SpielServiceImpl(SpielRepository spielRepo, ModelMapper modelMapper, SpieltagRepository spieltagRepo, TeamRepository teamRepo) {
        this.spielRepo = spielRepo;
        this.modelMapper = modelMapper;
        this.spieltagRepo = spieltagRepo;
        this.teamRepo = teamRepo;
    }

    @Override
    @Transactional
    public List<SpielDto> getAllForMatchday(Long id) {
        log.info("getAllForMatchday:: {}", id);
        List<Spiel> spiele = spielRepo.findAllForMatchday(id);
        log.info("Spiele:: {}", spiele);
        List<SpielDto> spielDtos = new ArrayList<>();
        final ModelMapper myMapper = MapperUtil.getModelMapperForSpiel();
        for (Spiel spiel : spiele) {
            spielDtos.add(myMapper.map(spiel, SpielDto.class));
        }
        return spielDtos;
    }

    @Override
    public Optional<SpielDto> findById(Long id) {
        Optional<Spiel> model = spielRepo.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto matchDto = modelMapper.map(model, SpielDto.class);
        return Optional.of(matchDto);
    }

    @Override
    @Transactional
    public Optional<SpielDto> save(SpielDto spielDto) {
        log.info("save spiel:: {}", spielDto);
        Spiel model = modelMapper.map(spielDto, Spiel.class);

        log.info("save spiel  entity:: {}", model);
        Spiel createdModel = spielRepo.save(model);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto createdDto = myModelMapper.map(createdModel, SpielDto.class);
        log.info("return spiel  dto:: {}", createdDto);
        return Optional.of(createdDto);


    }

    @Override
    @Transactional
    public Optional<SpielDto> updateSpiel(Long id, SpielDto spielDto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamRepo.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));


        log.info("update Match dto:: {}", spielDto);
        Optional<Spiel> updateModel = spielRepo.findById(id);
        if (updateModel.isPresent()) {
            updateModel.get().setHeimTeam(heimTeam);
            updateModel.get().setGastTeam(gastTeam);
            updateModel.get().setSpieltag(spieltag);
            Optional<Spiel> updated = updateModel.map(base -> updateFields(base, spielDto))
                    .map(spielRepo::save);
            log.info("update Match entity:: {}", updated);
            SpielDto matchDto = myModelMapper.map(updated, SpielDto.class);
            log.info("Spiel updated  RETURN dto {}", matchDto);
            return Optional.ofNullable(matchDto);
        }
        return Optional.empty();
    }

    private Spiel updateFields(Spiel base, SpielDto updatedMatch) {
        base.setAnpfiffdate(updatedMatch.getAnpfiffdate());
        base.setSpielNumber(updatedMatch.getSpielNumber());
        base.setHeimTore(updatedMatch.getHeimTore());
        base.setStattgefunden(updatedMatch.isStattgefunden());
        base.setGastTore(updatedMatch.getGastTore());
        log.info("Sthis will be persistedo {}", base);
        return base;
    }

    @Override
    public void deleteById(Long id) {
        spielRepo.deleteById(id);
    }


    @Override
    public List<SpielDto> getAll() {
        List<Spiel> spiele = spielRepo.findAll();
        List<SpielDto> spielDtos = new ArrayList<>();
        final ModelMapper myMapper = MapperUtil.getModelMapperForSpiel();
        for (Spiel spiel : spiele) {
            spielDtos.add(myMapper.map(spiel, SpielDto.class));
        }
        return spielDtos;
    }
}

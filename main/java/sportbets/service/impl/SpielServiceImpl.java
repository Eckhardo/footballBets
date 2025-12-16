package sportbets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Spiel;
import sportbets.persistence.entity.Spieltag;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.SpielRepository;
import sportbets.persistence.repository.SpieltagRepository;
import sportbets.persistence.repository.TeamRepository;
import sportbets.service.SpielService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.SpielDto;
import sportbets.web.dto.SpieltagDto;

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
    public Optional<SpielDto> findById(Long id) {
        Optional<Spiel> model =  spielRepo.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForSpiel();
        SpielDto matchDto = modelMapper.map(model, SpielDto.class);
        return Optional.of(matchDto);
    }

    @Override
    @Transactional
    public Optional<SpielDto> save(SpielDto spielDto) {
        log.info("save spiel:: {}", spielDto);
        Optional<Spieltag> spieltag =  spieltagRepo.findById(spielDto.getSpieltagId());
        Optional<Team> heimTeam =  teamRepo.findById(spielDto.getHeimTeamId());
        Optional<Team> gastTeam =  teamRepo.findById(spielDto.getGastTeamId());
        if(spieltag.isPresent() && heimTeam.isPresent() && gastTeam.isPresent()) {
            Spiel model = modelMapper.map(spielDto, Spiel.class);

            log.info("save spiel  entity:: {}", model);
            Spiel createdModel = spielRepo.save(model);
            ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();
            SpielDto createdDto = myModelMapper.map(createdModel, SpielDto.class);
            log.info("return spiel  dto:: {}", createdDto);
            return Optional.of(createdDto);
        }
        else{
            throw new EntityNotFoundException("spieltag or heim- or gastteam not found");
        }

    }

    @Override
    @Transactional
    public Optional<SpielDto> updateSpiel(Long id, SpielDto dto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForSpiel();


        log.info("update Match dto:: {}", dto);
        Optional<Spiel > updateModel= spielRepo.findById(id);
        if (updateModel.isPresent()) {
            Optional<Spiel> updated = updateModel.map(base -> updateFields(base, dto))
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
        base.setGastTore(updatedMatch.getGastTore());
        Spieltag sp=new Spieltag();
        sp.setId(updatedMatch.getSpieltagId());
        base.setSpieltag(sp);
        Team heimTeam=new Team();
        heimTeam.setId(updatedMatch.getHeimTeamId());
        base.setHeimTeam(heimTeam);
        Team gastTeam=new Team();
        gastTeam.setId(updatedMatch.getGastTeamId());
        base.setGastTeam(gastTeam);
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
            spielDtos.add(myMapper.map(spiele, SpielDto.class));
        }
        return spielDtos;
    }
}

package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
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
import sportbets.web.dto.competition.SpielDto;

import java.util.List;
import java.util.Optional;

@Service
public class SpielServiceImpl implements SpielService {
    private static final Logger log = LoggerFactory.getLogger(SpielServiceImpl.class);
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;
    private final TeamRepository teamRepo;
    private final ModelMapper modelMapper;

    public SpielServiceImpl(SpielRepository spielRepo, SpieltagRepository spieltagRepo, TeamRepository teamRepo, ModelMapper modelMapper) {
        this.spielRepo = spielRepo;
        this.spieltagRepo = spieltagRepo;
        this.teamRepo = teamRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<Spiel> getAllForMatchday(Long id) {

        return spielRepo.findAllForMatchday(id);

    }

    @Override
    public Optional<Spiel> findById(Long id) {
        return spielRepo.findById(id);

    }

    @Override
    @Transactional
    public Spiel save(SpielDto spielDto) {
        log.info("save Spiel :: {}", spielDto);
        Optional<Spiel> optionalSpiel = spielRepo.findByNumberWithSpieltagId(spielDto.getSpielNumber(), spielDto.getSpieltagId());
        if (optionalSpiel.isPresent()) {
            throw new EntityExistsException("Spiel  already exist with given id:" + spielDto.getId());
        }
        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamRepo.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);
        return spielRepo.save(model);


    }

    @Override
    @Transactional
    public Optional<Spiel> updateSpiel(Long id, SpielDto spielDto) {

        log.info("update Match dto:: {}", spielDto);
        Optional<Spiel> updateModel = spielRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("spiel  already exist with given id:" + spielDto.getId());
        }
        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamRepo.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        Spiel updated = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", updated);
        return Optional.of(spielRepo.save(updated));

    }

    private Spiel updateFields(Spiel base, Spiel updatedMatch) {
        base.setAnpfiffdate(updatedMatch.getAnpfiffdate());
        base.setSpielNumber(updatedMatch.getSpielNumber());
        base.setHeimTore(updatedMatch.getHeimTore());
        base.setStattgefunden(updatedMatch.isStattgefunden());
        base.setGastTore(updatedMatch.getGastTore());
        base.setSpieltag(updatedMatch.getSpieltag());
        base.setHeimTeam(updatedMatch.getHeimTeam());
        base.setGastTeam(updatedMatch.getGastTeam());
        return base;
    }

    @Override
    public void deleteById(Long id) {
        spielRepo.deleteById(id);
    }


    @Override
    public List<Spiel> getAll() {
        return spielRepo.findAll();

    }
}

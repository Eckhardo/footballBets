package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
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
import sportbets.service.competition.SpieltagService;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.List;
import java.util.Optional;

@Service
public class SpieltagServiceImpl implements SpieltagService {

    private static final Logger log = LoggerFactory.getLogger(SpieltagServiceImpl.class);
    private final SpieltagRepository spieltagRepository;
    private final CompetitionRoundRepository compRoundRepo;
    private final ModelMapper modelMapper;
    public SpieltagServiceImpl(SpieltagRepository spieltagRepository, CompetitionRoundRepository compRoundRepo, ModelMapper modelMapper) {

        this.spieltagRepository = spieltagRepository;

        this.compRoundRepo = compRoundRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<Spieltag> getAll() {

        return spieltagRepository.findAll();

    }


    @Override
    public Optional<Spieltag> findById(Long id) {
        return spieltagRepository.findById(id);
    }

    @Override
    @Transactional
    public Spieltag save(SpieltagDto spieltagDto) {
        log.info("save :: {}", spieltagDto);
        Optional<Spieltag> optionalSpieltag = spieltagRepository.findByNumberWithRoundId(spieltagDto.getSpieltagNumber(), spieltagDto.getCompRoundId());
        if (optionalSpieltag.isPresent()) {
            throw new EntityExistsException("Spieltag  already exist with given id:" + spieltagDto.getId());
        }
        CompetitionRound competitionRound = compRoundRepo.findById(spieltagDto.getCompRoundId()).orElseThrow(() -> new EntityNotFoundException("spieltag not found "));
        Spieltag model = modelMapper.map(spieltagDto, Spieltag.class);
        model.setCompetitionRound(competitionRound);

        return spieltagRepository.save(model);


    }

    @Override
    @Transactional
    public Optional<Spieltag> updateMatchDay(Long id, SpieltagDto spieltagDto) {
        log.info("updateMatchday:: {}", spieltagDto);
        CompetitionRound competitionRound = compRoundRepo.findById(spieltagDto.getCompRoundId()).orElseThrow(() -> new EntityNotFoundException("spieltag not found "));
        Spieltag model = modelMapper.map(spieltagDto, Spieltag.class);
        model.setCompetitionRound(competitionRound);


        Optional<Spieltag> updateModel = spieltagRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("spiel  does not exits given id:" + spieltagDto.getId());
        }


        Spieltag updatedModel = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", model);
        return Optional.of(spieltagRepository.save(updatedModel));

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        spieltagRepository.deleteById(id);
    }

    @Override
    public List<Spieltag> getAllForRound(Long roundId) {
        return spieltagRepository.findAllByRoundId(roundId);

    }


    private Spieltag updateFields(Spieltag base, Spieltag updatedMatchDay) {
        base.setSpieltagNumber(updatedMatchDay.getSpieltagNumber());
        base.setStartDate(updatedMatchDay.getStartDate());

        return base;
    }
}

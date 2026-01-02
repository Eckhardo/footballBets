package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
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

import java.util.Optional;

@Service
public class CompRoundServiceImpl implements CompRoundService {

    private static final Logger log = LoggerFactory.getLogger(CompRoundServiceImpl.class);
    private final CompetitionRoundRepository roundRepository;
    private final CompetitionRepository compRepo;
    private final ModelMapper modelMapper;

    public CompRoundServiceImpl(CompetitionRoundRepository roundRepository, CompetitionRepository compRepo, ModelMapper modelMapper) {
        this.roundRepository = roundRepository;

        this.compRepo = compRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Optional<CompetitionRound> findById(Long id) {
        return roundRepository.findById(id);
    }

    @Override
    @Transactional
    public CompetitionRound save(CompetitionRoundDto compRoundDto) {
        log.debug("Saving competition round");
        Optional<CompetitionRound> round = roundRepository.findByName(compRoundDto.getName());
        if (round.isPresent()) {
            throw new EntityExistsException("Comp Round already exist with given name:" + compRoundDto.getName());
        }
        Competition comp = compRepo.findById(compRoundDto.getCompId()).orElseThrow(() -> new EntityNotFoundException("comp not found "));
        CompetitionRound model = modelMapper.map(compRoundDto, CompetitionRound.class);
        model.setCompetition(comp);
        log.debug("Saving competition round {}", model.getName());
        return roundRepository.save(model);
    }

    @Override
    @Transactional
    public Optional<CompetitionRound> updateRound(Long id, CompetitionRoundDto compRoundDto) {


        log.info("updateCompRound  with {}", compRoundDto);
        Optional<CompetitionRound> updateModel = roundRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("spiel  does not exits given id:" + compRoundDto.getId());
        }
        Competition comp = compRepo.findById(compRoundDto.getCompId()).orElseThrow(() -> new EntityNotFoundException("comp not found "));
        CompetitionRound model = modelMapper.map(compRoundDto, CompetitionRound.class);
        model.setCompetition(comp);

        CompetitionRound updatedCompRound = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", updatedCompRound);
        return Optional.of(roundRepository.save(updatedCompRound));
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


    private CompetitionRound updateFields(CompetitionRound base, CompetitionRound updatedRound) {
        base.setName(updatedRound.getName());
        base.setRoundNumber(updatedRound.getRoundNumber());
        base.setHasGroups(updatedRound.isHasGroups());

        return base;
    }
}

package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
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

import java.util.List;
import java.util.Optional;

@Service
public class CompRoundServiceImpl implements CompRoundService {

    private static final Logger log = LoggerFactory.getLogger(CompRoundServiceImpl.class);
    private final CompetitionRoundRepository roundRepository;
    private final CompetitionRepository compRepository;

    public CompRoundServiceImpl(CompetitionRoundRepository roundRepository, CompetitionRepository compRepository, ModelMapper modelMapper) {
        this.roundRepository = roundRepository;
        this.compRepository = compRepository;
    }

    @Override
    @Transactional
    public Optional<CompetitionRound> findById(Long id) {
        return roundRepository.findById(id);
    }

    @Override
    @Transactional
    public CompetitionRound save(CompetitionRound compRound) {
         Optional<CompetitionRound> competition = roundRepository.findByName(compRound.getName());
        if (competition.isPresent()) {
            throw new EntityExistsException("Comp Round already exist with given name:" + compRound.getName());
        }

        return roundRepository.save(compRound);
    }

    @Override
    @Transactional
    public Optional<CompetitionRound> updateRound(Long id, CompetitionRound compRound) {


        log.info("updateCompRound  with {}", compRound);
        Optional<CompetitionRound> updateModel = roundRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("Round does not exist with given id:" + compRound.getId());
        }

        CompetitionRound updatedCompRound = updateFields(updateModel.get(), compRound);
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


    @Override
    @Transactional
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

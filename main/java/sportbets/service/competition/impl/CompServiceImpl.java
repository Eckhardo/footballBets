package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.competition.CompService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompServiceImpl implements CompService {

    private static final Logger log = LoggerFactory.getLogger(CompServiceImpl.class);
    private final CompetitionRepository compRepository;
    private final CompetitionFamilyRepository famRepository;


    private final ModelMapper modelMapper;

    public CompServiceImpl(CompetitionRepository compRepository, CompetitionFamilyRepository famRepository, ModelMapper modelMapper) {
        this.compRepository = compRepository;
        this.famRepository = famRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Optional<Competition> findById(Long id) {
        return  compRepository.findById(id);

    }

    /**
     * @param name
     * @return
     */
    @Override
    public Optional<Competition> findByName(String name) {
        return  compRepository.findByName(name);
    }

    @Override
    @Transactional
    public Competition save(Competition comp) {

        Optional<Competition> competition = compRepository.findByName(comp.getName());
        if (competition.isPresent()) {
            throw new EntityExistsException("Comp  already exist with given name:" + comp.getName());
        }
        return compRepository.save(comp);

    }

    @Override
    @Transactional
    public Competition updateComp(Long id, Competition comp) {


        log.info("updateComp  with {}", comp);
        Optional<Competition> updateModel = compRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("Comp  already exist with given name:" + comp.getName());
        }

        Competition updatedComp = updateFields(updateModel.get(), comp);
        log.info("updated Comp  with {}", updatedComp);
        return compRepository.save(updatedComp);

    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        compRepository.deleteById(id);
    }

    @Override
    public List<Competition> getAll() {

       return compRepository.findAll();

    }

    @Override
    public Optional<Competition> findByNameJoinFetchRounds(String name) {
        log.info("CompService:findByNameJoinFetchRounds::" + name);
        return compRepository.findByNameJoinFetchRounds(name);
    }

    @Override
    public List<TeamDto> findTeamsForComp(Long compId) {
        List<Team> teams = compRepository.findTeamsForComp(compId);
        List<TeamDto> teamDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetition();
        teams.forEach(team -> {
            teamDtos.add(myMapper.map(team, TeamDto.class));
        });
        return teamDtos;
    }

    @Override
    public Competition findByIdJoinFetchRounds(Long id) {
        log.info("CompService:findById::" + id);
        return compRepository.findByIdJoinFetchRounds(id);
    }

    @Override
    @Transactional
    public List<CompetitionRoundDto> getAllFormComp(Long compId) {
        Competition comp = compRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException("Comp not found"));
        List<CompetitionRound> rounds = compRepository.findAllForComp(compId);
        List<CompetitionRoundDto> roundDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompetition();
        rounds.forEach(round -> {
            roundDtos.add(myMapper.map(round, CompetitionRoundDto.class));
        });
        return roundDtos;
    }

    private Competition updateFields(Competition base, Competition updatedComp) {
        base.setName(updatedComp.getName());
        base.setDescription(updatedComp.getDescription());
        base.setWinMultiplicator(updatedComp.getWinMultiplicator());
        base.setRemisMultiplicator(updatedComp.getRemisMultiplicator());
        return base;
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        compRepository.deleteByName(name);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Optional<Competition> findByIdTest(Long id) {
        log.info("\n");
        Optional<Competition> model = compRepository.findById(id);
        if (model.isPresent()) {
            Set<CompetitionRound> rounds = model.get().getCompetitionRounds();
            if (!rounds.isEmpty()) {
                for (CompetitionRound round : rounds) {
                    log.info("round of current comp found with {}", round);
                }
            }
        }
        Set<CompetitionRole> roles = model.get().getCompetitionRoles();
        if (!roles.isEmpty()) {
            for (CompetitionRole role : roles) {
                log.info("role of current comp found with {}", role);
            }
        }
        log.info("\n");
        return model;
    }
}

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
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompServiceImpl implements CompService {

    private static final Logger log = LoggerFactory.getLogger(CompServiceImpl.class);
    private final CompetitionRepository compRepository;
    private final CompetitionFamilyRepository compFamilyRepo;
    final ModelMapper modelMapper;


    public CompServiceImpl(CompetitionRepository compRepository, CompetitionFamilyRepository compFamilyRepo, ModelMapper modelMapper) {
        this.compRepository = compRepository;
        this.compFamilyRepo = compFamilyRepo;

        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Optional<Competition> findById(Long id) {
        return compRepository.findById(id);

    }

    /**
     * @param name
     * @return
     */
    @Override
    public Optional<Competition> findByName(String name) {
        return compRepository.findByName(name);
    }

    @Override
    @Transactional
    public Competition save(CompetitionDto compDto) {

        Optional<Competition> competition = compRepository.findByName(compDto.getName());
        if (competition.isPresent()) {
            throw new EntityExistsException("Competition already exists with given name:" + compDto.getName());
        }
        Competition model = modelMapper.map(compDto, Competition.class);
        CompetitionFamily fam = compFamilyRepo.findById(compDto.getFamilyId()).orElseThrow(() -> new EntityNotFoundException("compFam not found "));
        model.setCompetitionFamily(fam);
        CompetitionRole compRole= new CompetitionRole(model.getName(), model.getDescription() ,model);
        model.addCompetitionRole(compRole);
        return compRepository.save(model);

    }

    @Override
    @Transactional
    public Optional<Competition> updateComp(Long id, CompetitionDto compDto) {


        log.debug("updateComp  with {}", compDto);
        Optional<Competition> updateModel = compRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("spiel  does not exits given name:" + compDto.getName());
        }


        Optional<CompetitionRole> oldRole = updateModel.get().getCompetitionRoleByName(updateModel.get().getName());
        if (oldRole.isPresent()) {
            log.debug("old {}",oldRole.get().getName());

            oldRole.get().setName(compDto.getName());
            oldRole.get().setDescription(compDto.getDescription());
        }
        updateModel.get().setName(compDto.getName());
        updateModel.get().setDescription(compDto.getDescription());
        updateModel.get().setWinMultiplicator(compDto.getWinMultiplicator());
        updateModel.get().setRemisMultiplicator(compDto.getRemisMultiplicator());
        log.debug("updated Comp  with {}", updateModel);
        return  Optional.of(compRepository.save(updateModel.get()));

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
        log.debug("CompService:findByNameJoinFetchRounds::{}", name);
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
        log.debug("CompService:findById::{}", id);
        return compRepository.findByIdJoinFetchRounds(id);
    }

    @Override
    @Transactional
    public List<CompetitionRound> getAllFormComp(Long compId) {
        return compRepository.findAllForComp(compId);

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
        log.debug("\n");
        Optional<Competition> model = compRepository.findById(id);
        if (model.isPresent()) {
            Set<CompetitionRound> rounds = model.get().getCompetitionRounds();
            if (!rounds.isEmpty()) {
                for (CompetitionRound round : rounds) {
                    log.debug("round of current comp found with {}", round);
                }
            }
        }

        Set<CompetitionRole> roles = model.get().getCompetitionRoles();
        if (!roles.isEmpty()) {
            for (CompetitionRole role : roles) {
                log.debug("role of current comp found with {}", role);
            }
        }
        log.debug("\n");
        return model;
    }
}

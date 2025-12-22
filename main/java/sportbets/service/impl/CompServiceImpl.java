package sportbets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.CompetitionRepository;
import sportbets.service.CompService;
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionRoundDto;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.TeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompServiceImpl implements CompService {

    private static final Logger log = LoggerFactory.getLogger(CompServiceImpl.class);
    private final CompetitionRepository compRepository;


    private final ModelMapper modelMapper;

    public CompServiceImpl(CompetitionRepository compRepository, ModelMapper modelMapper) {
        this.compRepository = compRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Optional<CompetitionDto> findById(Long id) {
        Optional<Competition> model = compRepository.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForFamily();
        log.info("Competition found with {}", model);
        CompetitionDto compDto = modelMapper.map(model, CompetitionDto.class);
        return Optional.of(compDto);
    }

    @Override
    @Transactional
    public Optional<CompetitionDto> save(CompetitionDto comp) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();

        Competition model = myModelMapper.map(comp, Competition.class);

        log.info("Competition saved with {}", model);
        Competition createdModel = compRepository.save(model);
        CompetitionDto compDto = modelMapper.map(createdModel, CompetitionDto.class);
        log.info("Competition RETURN do {}", compDto);
        return Optional.of(compDto);
    }

    @Override
    @Transactional
    public Optional<CompetitionDto> updateComp(Long id, CompetitionDto updatedCompDto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();


        log.info("updateComp  with {}", updatedCompDto);
        Optional<Competition> updateModel = compRepository.findById(id);

        if (updateModel.isPresent()) {
            Optional<Competition> updated = updateModel.map(base -> updateFields(base, updatedCompDto))
                    .map(compRepository::save);
            CompetitionDto compDto = modelMapper.map(updated, CompetitionDto.class);
            log.info("Competition updated  RETURN dto {}", compDto);
            return Optional.ofNullable(compDto);

        }

        return Optional.empty();
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        compRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<CompetitionDto> getAll() {

        List<Competition> comps = compRepository.findAll();
        List<CompetitionDto> competitionDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        comps.forEach(comp -> {
            competitionDtos.add(myMapper.map(comp, CompetitionDto.class));
        });
        return competitionDtos;
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

    private Competition updateFields(Competition base, CompetitionDto updatedComp) {
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
}

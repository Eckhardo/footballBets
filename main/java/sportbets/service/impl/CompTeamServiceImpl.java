package sportbets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionTeam;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.CompetitionRepository;
import sportbets.persistence.repository.CompetitionTeamRepository;
import sportbets.persistence.repository.TeamRepository;
import sportbets.service.CompTeamService;
import sportbets.web.dto.CompetitionTeamDto;
import sportbets.web.dto.MapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompTeamServiceImpl implements CompTeamService {

    private static final Logger log = LoggerFactory.getLogger(CompTeamServiceImpl.class);
    private final CompetitionTeamRepository compTeamRepo;
    private final CompetitionRepository compRepo;
    private final TeamRepository teamRepo;
    private final ModelMapper modelMapper;

    public CompTeamServiceImpl(CompetitionTeamRepository compTeamRepo, CompetitionRepository compRepo, TeamRepository teamRepo, ModelMapper modelMapper) {
        this.compTeamRepo = compTeamRepo;
        this.compRepo = compRepo;
        this.teamRepo = teamRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionTeamDto> findById(Long id) {
        Optional<CompetitionTeam> model = compTeamRepo.findById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapperForCompTeam();
        CompetitionTeamDto competitionTeamDto = modelMapper.map(model, CompetitionTeamDto.class);
        return Optional.of(competitionTeamDto);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionTeamDto> save(CompetitionTeamDto dto) {
        Optional<Team> team = teamRepo.findById(dto.getTeamId());
        Optional<Competition> comp = compRepo.findById(dto.getCompId());
        if(team.isPresent() && comp.isPresent()) {

            CompetitionTeam model = new CompetitionTeam(team.get(),comp.get());

            log.info("compTeam model to be saved:: {}", model);
            CompetitionTeam createdModel = compTeamRepo.save(model);

            log.info("compTeam saved model:: {}", createdModel);
            ModelMapper myModelMapper = MapperUtil.getModelMapperForCompTeam();
            CompetitionTeamDto createdDto = myModelMapper.map(createdModel, CompetitionTeamDto.class);
            log.info("compTeam dto to return:: {}", dto);
            return Optional.of(createdDto);
        }
        else {
            throw  new EntityNotFoundException("entities for tem and/or comp not found");
        }


    }

    /**
     * @param dtos
     * @return
     */
    @Override
    @Transactional
    public List<CompetitionTeamDto> saveAll(List<CompetitionTeamDto> dtos) {
        List<CompetitionTeam> competitionTeams = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();

        for (CompetitionTeamDto competitionTeamDto : dtos) {
            competitionTeams.add(myMapper.map(competitionTeamDto, CompetitionTeam.class));
        }

        List<CompetitionTeam> savedModels = compTeamRepo.saveAll(competitionTeams);
        List<CompetitionTeamDto> theDtos = new ArrayList<>();
        for (CompetitionTeam savedModel : savedModels) {
            theDtos.add(modelMapper.map(savedModel, CompetitionTeamDto.class));
        }
        return theDtos;
    }

    /**
     * @param id
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionTeamDto> update(Long id, CompetitionTeamDto dto) {
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompTeam();
        Optional<Team> team = teamRepo.findById(dto.getTeamId());


        log.info("update Match dto:: {}", dto);
        Optional<CompetitionTeam> updateModel = compTeamRepo.findById(id);
        if (updateModel.isPresent() &&  team.isPresent()) {
            Optional<CompetitionTeam> updated = updateModel.map(base -> updateFields(base, dto,team.get()))
                    .map(compTeamRepo::save);
            log.info("update Match entity:: {}", updated);
            CompetitionTeamDto matchDto = myModelMapper.map(updated, CompetitionTeamDto.class);
            log.info("Spiel updated  RETURN dto {}", matchDto);
            return Optional.ofNullable(matchDto);
        }
        return Optional.empty();
    }

    /**
     * @param dtos
     * @return
     */
    @Override
    @Transactional
    public void deleteAll(List<CompetitionTeamDto> dtos) {
        List<CompetitionTeam> competitionTeams = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();

        for (CompetitionTeamDto competitionTeamDto : dtos) {
            competitionTeams.add(myMapper.map(competitionTeamDto, CompetitionTeam.class));
        }

        compTeamRepo.deleteAll(competitionTeams);

    }

    private CompetitionTeam updateFields(CompetitionTeam base, CompetitionTeamDto dto,Team team) {
        base.setTeam(team);

        Competition competition = new Competition();
        competition.setId(dto.getCompId());
        base.setCompetition(competition);

        return base;
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        compTeamRepo.deleteById(id);
    }

    /**
     * @param compId
     * @return
     */
    @Override
    public List<CompetitionTeamDto> getAllFormComp(Long compId) {


        List<CompetitionTeam> savedModels = compTeamRepo.getAllFormComp(compId);
        List<CompetitionTeamDto> theDtos = new ArrayList<>();
        for (CompetitionTeam savedModel : savedModels) {
            theDtos.add(modelMapper.map(savedModel, CompetitionTeamDto.class));
        }
        return theDtos;
    }
}

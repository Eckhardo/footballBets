package sportbets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionTeamRepository;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.CompTeamService;
import sportbets.web.dto.competition.CompetitionTeamDto;
import sportbets.web.dto.competition.MapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompTeamServiceImpl implements CompTeamService {

    private static final Logger log = LoggerFactory.getLogger(CompTeamServiceImpl.class);
    private final CompetitionTeamRepository compTeamRepo;
    private final CompetitionRepository compRepo;
    private final TeamRepository teamRepo;

    public CompTeamServiceImpl(CompetitionTeamRepository compTeamRepo, CompetitionRepository compRepo, TeamRepository teamRepo) {
        this.compTeamRepo = compTeamRepo;
        this.compRepo = compRepo;
        this.teamRepo = teamRepo;

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
        Team team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        Competition comp = compRepo.findById(dto.getCompId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));


        CompetitionTeam model = new CompetitionTeam(team, comp);

        log.info("compTeam model to be saved:: {}", model);
        CompetitionTeam createdModel = compTeamRepo.save(model);

        log.info("compTeam saved model:: {}", createdModel);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForCompTeam();
        CompetitionTeamDto createdDto = myModelMapper.map(createdModel, CompetitionTeamDto.class);
        log.info("compTeam dto to return:: {}", dto);
        return Optional.of(createdDto);

    }


    /**
     * @param dtos
     * @return
     */
    @Override
    @Transactional
    public List<CompetitionTeamDto> saveAll(List<CompetitionTeamDto> dtos) {

        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();
        List<CompetitionTeamDto> theDtos = new ArrayList<>();
        for (CompetitionTeamDto dto : dtos) {
            Team team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new EntityNotFoundException("Team not found"));
            Competition comp = compRepo.findById(dto.getCompId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));
            CompetitionTeam model = new CompetitionTeam(team, comp);
            CompetitionTeam createdModel = compTeamRepo.save(model);
            CompetitionTeamDto createdDto = myMapper.map(createdModel, CompetitionTeamDto.class);
            theDtos.add(createdDto);

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
        Team team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        Competition comp = compRepo.findById(dto.getCompId()).orElseThrow(() -> new EntityNotFoundException("Comp not found"));
        ;

        log.info("update Match dto:: {}", dto);
        Optional<CompetitionTeam> updateModel = compTeamRepo.findById(id);
        if (updateModel.isPresent()) {

            Optional<CompetitionTeam> updated = updateModel.map(base -> updateFields(base, dto, team, comp))
                    .map(compTeamRepo::save);
            CompetitionTeamDto matchDto = myModelMapper.map(updated, CompetitionTeamDto.class);
            log.info("CompTeam updated  RETURN dto {}", matchDto);
            return Optional.ofNullable(matchDto);
        } else {
            throw new EntityNotFoundException("entity compTeam not found");
        }

    }

    /**
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        for (Long id : ids) {
            compTeamRepo.deleteById(id);
        }
    }

    private CompetitionTeam updateFields(CompetitionTeam base, CompetitionTeamDto dto, Team team, Competition comp) {
        base.setTeam(team);
        base.setCompetition(comp);

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
    @Transactional
    public List<CompetitionTeamDto> getAllFormComp(Long compId) {

        ModelMapper myMapper = MapperUtil.getModelMapperForCompTeam();

        List<CompetitionTeam> savedModels = compTeamRepo.getAllFormComp(compId);
        List<CompetitionTeamDto> theDtos = new ArrayList<>();
        for (CompetitionTeam savedModel : savedModels) {
            theDtos.add(myMapper.map(savedModel, CompetitionTeamDto.class));
        }
        return theDtos;
    }
}

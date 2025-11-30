package sportbets.service.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.service.CompFamilyService;
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionFamilyDto;
import sportbets.web.dto.MapperUtil;

import java.util.*;

@Service
public class CompFamilyServiceImpl implements CompFamilyService {

    private static final Logger log = LoggerFactory.getLogger(CompFamilyServiceImpl.class);
    private final CompetitionFamilyRepository compFamilyRepository;
    private final ModelMapper modelMapper;
    ;

    public CompFamilyServiceImpl(CompetitionFamilyRepository compFamilyRepository, ModelMapper modelMapper) {
        this.compFamilyRepository = compFamilyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CompetitionFamilyDto> getAll() {

        List<CompetitionFamily> fams = compFamilyRepository.findAll();
        List<CompetitionFamilyDto> famDtos = new ArrayList<>();
        fams.forEach(fam -> {
            famDtos.add(modelMapper.map(fam, CompetitionFamilyDto.class));
        });
        return famDtos;
    }


    @Override
    public Optional<CompetitionFamilyDto> findById(Long id) {
        Optional<CompetitionFamily> model = compFamilyRepository.findById(id);
        CompetitionFamilyDto famDto = modelMapper.map(model, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    public Optional<CompetitionFamilyDto> save(CompetitionFamilyDto compFam) {
        log.info("FmDto to be save:: {}", compFam);
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if (savedCompFamily.isPresent()) {
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        CompetitionFamily model = modelMapper.map(compFam, CompetitionFamily.class);
        log.info("model be save:: {}", model);
        CompetitionFamily createdModel = compFamilyRepository.save(model);
        log.info("saved entity:: {}", createdModel);
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        log.info("dto to return:: {}", famDto);
        return Optional.of(famDto);
    }

    public Optional<CompetitionFamilyDto> saveHierarchyTest(CompetitionFamilyDto compFam) {

        log.info("Dto to be save:: {}", compFam.toString());
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if (savedCompFamily.isPresent()) {
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        CompetitionFamily model = modelMapper.map(compFam, CompetitionFamily.class);
        ModelMapper myModelMapper = MapperUtil.getModelMapperForFamily();
        if(!compFam.getCompetitions().isEmpty()){

            for(CompetitionDto compDto : compFam.getCompetitions()){
                log.info("iterate comp dtos:: {}", compDto.toString());
                Competition compModel=  myModelMapper.map(compDto, Competition.class);
                log.info("CompModel to save:: {}", compModel.toString());
                model.addCompetition(compModel);
            }
        }
        log.info("Model to save:: {}", model.toString());
        CompetitionFamily createdModel = compFamilyRepository.save(model);
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        Set<CompetitionDto> compDtos = new HashSet<>();

        for(Competition comp : createdModel.getCompetitions()){
            compDtos.add(myModelMapper.map(comp, CompetitionDto.class));
        }
        famDto.setCompetitions(compDtos);
        return Optional.of(famDto);
    }
    @Override
    public Optional<CompetitionFamilyDto> updateFamily(Long id, CompetitionFamilyDto compFam) {
        log.info("updateFamily:: {}",compFam);
        Optional<CompetitionFamily> updateModel = compFamilyRepository.findById(id);

        if (updateModel.isPresent()) {
            Optional<CompetitionFamily> updatedModel = updateModel.map(base -> updateFields(base, compFam))
                    .map(compFamilyRepository::save);
            CompetitionFamilyDto famDto = modelMapper.map(updatedModel, CompetitionFamilyDto.class);
            return Optional.of(famDto);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public List<Team> findTeams(Long id) {
        return compFamilyRepository.findTeamsForCompFamily(id);
    }


    private CompetitionFamily updateFields(CompetitionFamily base, CompetitionFamilyDto updatedFam) {
        base.setName(updatedFam.getName());
        base.setDescription(updatedFam.getDescription());
        base.setHasClubs(updatedFam.isHasClubs());
        base.setHasLigaModus(updatedFam.isHasLigaModus());
        return base;
    }

    @Override
    public void deleteByName(String name) {
        compFamilyRepository.deleteByName(name);
    }

    @Override
    public void deleteById(Long id) {
        compFamilyRepository.deleteById(id);
    }

    @Override
    public ModelMapper getModelMapperForFamily() {
        return modelMapper;
    }
}

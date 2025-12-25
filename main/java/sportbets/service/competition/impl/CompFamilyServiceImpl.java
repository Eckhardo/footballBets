package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.service.competition.CompFamilyService;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    /**
     * @param familyId
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionFamily> findByIdTest(Long familyId) {
        Optional<CompetitionFamily> model = compFamilyRepository.findById(familyId);
        if (model.isPresent()) {
            Set<Competition> comps= model.get().getCompetitions();
            assert !comps.isEmpty();
            for (Competition comp : comps) {
                log.info("Competition of current family found with {}", comp);
            }
        }
        return model;

    }

    @Override
    public Optional<CompetitionFamilyDto> findById(Long id) {
        Optional<CompetitionFamily> model = compFamilyRepository.findById(id);
        CompetitionFamilyDto famDto = modelMapper.map(model, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    @Transactional
    public Optional<CompetitionFamilyDto> save(CompetitionFamilyDto compFam) {
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if (savedCompFamily.isPresent()) {
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        CompetitionFamily model = modelMapper.map(compFam, CompetitionFamily.class);
        CompetitionFamily createdModel = compFamilyRepository.save(model);
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    @Transactional
    public Optional<CompetitionFamilyDto> updateFamily(Long id, CompetitionFamilyDto compFam) {
        log.info("updateFamily:: {}",compFam);
        Optional<CompetitionFamily> updateModel = compFamilyRepository.findById(id);

        if (updateModel.isPresent()) {
            Optional<CompetitionFamily> updated = updateModel.map(base -> updateFields(base, compFam))
                    .map(compFamilyRepository::save);
            CompetitionFamilyDto famDto = modelMapper.map(updated, CompetitionFamilyDto.class);
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
    @Transactional
    public void deleteByName(String name) {
        compFamilyRepository.deleteByName(name);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        compFamilyRepository.deleteById(id);
    }

    @Override
    public ModelMapper getModelMapperForFamily() {
        return modelMapper;
    }
}

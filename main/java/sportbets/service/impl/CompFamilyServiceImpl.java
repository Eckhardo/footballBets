package sportbets.service.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.service.CompFamilyService;
import sportbets.web.dto.CompetitionFamilyDto;

import java.util.List;
import java.util.Optional;

@Service
public class CompFamilyServiceImpl implements CompFamilyService {

    private final CompetitionFamilyRepository compFamilyRepository;
    private final ModelMapper modelMapper;;

    public CompFamilyServiceImpl(CompetitionFamilyRepository compFamilyRepository, ModelMapper modelMapper) {
        this.compFamilyRepository = compFamilyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CompetitionFamily> getAll() {
        return compFamilyRepository.findAll();
    }

    @Override
    public Optional<CompetitionFamilyDto> findById(Long id) {
        Optional<CompetitionFamily> model = compFamilyRepository.findById(id);
        CompetitionFamilyDto famDto = modelMapper.map(model, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    public Optional<CompetitionFamilyDto> save(CompetitionFamilyDto compFam) {
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if (savedCompFamily.isPresent()) {
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        CompetitionFamily model =  modelMapper.map(compFam, CompetitionFamily.class);
        CompetitionFamily createdModel= compFamilyRepository.save(model);
        CompetitionFamilyDto famDto = modelMapper.map(createdModel, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    public Optional<CompetitionFamilyDto> updateFamily(Long id, CompetitionFamilyDto compFam) {
        CompetitionFamily model =  modelMapper.map(compFam, CompetitionFamily.class);


        Optional<CompetitionFamily> updateModel= compFamilyRepository.findById(id)
                .map(base -> updateFields(base, model))
                .map(compFamilyRepository::save);

        CompetitionFamilyDto famDto = modelMapper.map(updateModel, CompetitionFamilyDto.class);
        return Optional.of(famDto);
    }

    @Override
    public List<Team> findTeams(Long id) {
        return compFamilyRepository.findTeamsForCompFamily(id);
    }


    private CompetitionFamily updateFields(CompetitionFamily base, CompetitionFamily updatedFam) {
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
}

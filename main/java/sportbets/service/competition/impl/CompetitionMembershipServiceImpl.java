package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.competition.CompetitionMembershipRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.competition.CompetitionMembershipService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionMembershipDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitionMembershipServiceImpl implements CompetitionMembershipService {
    private static final Logger log = LoggerFactory.getLogger(CompetitionMembershipServiceImpl.class);

    private final CompetitionMembershipRepository membershipRepository;
    private final CommunityRepository communityRepository;
    private final CompetitionRepository compRepo;


    public CompetitionMembershipServiceImpl(CompetitionMembershipRepository membershipRepository, CommunityRepository communityRepository, CompetitionRepository compRepo) {
        this.membershipRepository = membershipRepository;
        this.communityRepository = communityRepository;
        this.compRepo = compRepo;

    }


    @Override
    public Optional<CompetitionMembership> findById(Long id) {
        return membershipRepository.findById(id);
    }


    @Override
    @Transactional
    public CompetitionMembership save(CompetitionMembershipDto membershipDto) {
        log.debug("Service save CompetitionMembership {}", membershipDto);
        Optional<CompetitionMembership> entity = membershipRepository.findByCommIdAndCompId(membershipDto.getCommId(), membershipDto.getCompId());

        if (entity.isPresent()) {
            throw new EntityExistsException("CompetitionMembership  already exist with given id:" + membershipDto.getId());
        }

        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Competition competition = compRepo.findById(membershipDto.getCompId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return membershipRepository.save(new CompetitionMembership(community, competition));
    }


    @Override
    @Transactional
    public Optional<CompetitionMembership> update(Long id, CompetitionMembershipDto membershipDto) {

        log.debug("update CompetitionMembership dto:: {}", membershipDto);
        CompetitionMembership updateModel = membershipRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("CompetitionMembership  does not exits given id:" + membershipDto.getId()));


        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Competition competition = compRepo.findById(membershipDto.getCompId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateModel.setCompetition(competition);
        updateModel.setCommunity(community);
        return Optional.of(membershipRepository.save(updateModel));
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        // Idempotent check: If it is already gone, do nothing
        if (membershipRepository.existsById(id)) {
            membershipRepository.deleteById(id);
        }
    }


    @Override
    public List<CompetitionMembership> getAll() {
        return membershipRepository.findAll();
    }

    @Override
    public List<CompetitionDto> findCompetitions(Long commId) {
        List<Competition> comps = membershipRepository.findCompetitions(commId);
        List<CompetitionDto> compDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        for (Competition comp : comps) {
            compDtos.add(myMapper.map(comp, CompetitionDto.class));
        }
        return compDtos;
    }

    @Override
    public CompetitionDto findCurrentCompetition(Long commId) {
        List<Competition> comps = membershipRepository.findCompetitions(commId);
        for(Competition comp : comps) {
            log.debug("comp: {}{}",comp.getName(),comp.getCreatedOn().toString());
        }
        Competition currentComp = comps.stream().max(Comparator.comparing(Competition::getCreatedOn))
                .orElse(null); // Returns null if the collection is empty
        if (currentComp == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Competition Not Found");
        }
        log.debug("Current Competition Id:{}", currentComp.getName());
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        return myMapper.map(currentComp, CompetitionDto.class);
    }
}

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
import sportbets.web.dto.competition.CompetitionMembershipDto;

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
        log.info("Service save CompetitionMembership {}", membershipDto);
        Optional<CompetitionMembership> entity = membershipRepository.findByCommIdAndCompId(membershipDto.getCommId(), membershipDto.getCompId());

        if (entity.isPresent()) {
            log.error("CompetitionMembership already exists");
            throw new EntityExistsException("CompetitionMembership  already exist with given id:" + membershipDto.getId());
        }
        CompetitionMembership model = new CompetitionMembership();
        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Competition competition = compRepo.findById(membershipDto.getCompId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.setCommunity(community);
        model.setCompetition(competition);

        log.info("save CompetitionMembership {}", model);
        return membershipRepository.save(model);
    }


    @Override
    @Transactional
    public Optional<CompetitionMembership> update(Long id, CompetitionMembershipDto membershipDto) {

        log.info("update CompetitionMembership dto:: {}", membershipDto);
        Optional<CompetitionMembership> updateModel = membershipRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("CompetitionMembership  does not exits given id:" + membershipDto.getId());
        }

        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Competition competition = compRepo.findById(membershipDto.getCompId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateModel.get().setCommunity(community);
        updateModel.get().setCompetition(competition);

        log.info("updated CompMemb  with {}", updateModel.get());
        return Optional.of(membershipRepository.save(updateModel.get()));
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        membershipRepository.deleteById(id);
    }


    @Override
    public List<CompetitionMembership> getAll() {
        return membershipRepository.findAll();
    }
}

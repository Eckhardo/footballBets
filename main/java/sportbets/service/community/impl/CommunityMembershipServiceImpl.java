package sportbets.service.community.impl;

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
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.service.community.CommunityMembershipService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityMembershipDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityMembershipServiceImpl implements CommunityMembershipService {

    private static final Logger log = LoggerFactory.getLogger(CommunityMembershipServiceImpl.class);
    private final CommunityMembershipRepository membershipRepo;
    private final CommunityRepository commRepo;
    private final TipperRepository tipperRepo;
    private final ModelMapper mapper;

    public CommunityMembershipServiceImpl(CommunityMembershipRepository membershipRepo, CommunityRepository commRepo, TipperRepository tipperRepo, ModelMapper mapper) {
        this.membershipRepo = membershipRepo;
        this.commRepo = commRepo;
        this.tipperRepo = tipperRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<CommunityMembership> findById(Long id) {
        return membershipRepo.findById(id);
    }


    @Override
    @Transactional
    public CommunityMembership save(CommunityMembershipDto dto) {
        Optional<CommunityMembership> entity = membershipRepo.findByCommIdAndTipperId(dto.getCommId(), dto.getTipperId());
        if (entity.isPresent()) {
            throw new EntityExistsException("CommunityMembership  already exist with given id:" + dto.getId());
        }
        Community community = commRepo.findByName(dto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Tipper tipper = tipperRepo.findById(dto.getTipperId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return membershipRepo.save(new CommunityMembership(community, tipper));

    }

    @Override
    @Transactional
    public Optional<CommunityMembership> update(Long id, CommunityMembershipDto dto) {

        CommunityMembership updateModel = membershipRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CommMemb  does not exits given id:" + dto.getId()));
        Community community = commRepo.findByName(dto.getCommName()).orElseThrow(() -> new EntityNotFoundException("Comm does not exits given id:" + dto.getId()));
        Tipper tipper = tipperRepo.findById(dto.getTipperId()).orElseThrow(() -> new EntityNotFoundException("Tipper does not exits given id:" + dto.getTipperId()));
        updateModel.setCommunity(community);
        updateModel.setTipper(tipper);
        return Optional.of(membershipRepo.save(updateModel));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Idempotent check: If it is already gone, do nothing
        if (membershipRepo.existsById(id)) {
            membershipRepo.deleteById(id);
        }
    }

    @Override
    public List<CommunityMembership> getAll() {
        return membershipRepo.findAll();
    }

    @Override
    public List<CommunityMembership> findCommMembs(Long tipperId) {
        return membershipRepo.findTipperCommMembs(tipperId);
    }

    @Override
    public Optional<CommunityMembership> findByCommIdAndTipperId(Long commId, Long tipperId) {
        return membershipRepo.findByCommIdAndTipperId(commId, tipperId);
    }

    @Override
    public List<Tipper> findTippers(Long communityId) {
        return membershipRepo.findTippers(communityId);
    }

    @Override
    public List<CommunityDto> findCommunities(String username) {
        List<Community> communities = membershipRepo.findCommunities(username);

        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(mapper.map(community, CommunityDto.class));

        }
        return communityDtos;
    }
}

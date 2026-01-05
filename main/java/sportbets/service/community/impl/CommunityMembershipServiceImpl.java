package sportbets.service.community.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import sportbets.web.dto.community.CommunityMembershipDto;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityMembershipServiceImpl implements CommunityMembershipService {

    private final CommunityMembershipRepository membershipRepository;
    private final CommunityRepository communityRepository;
    private final TipperRepository tipperRepository;

    public CommunityMembershipServiceImpl(CommunityMembershipRepository communityMembershipRepository, CommunityRepository communityRepository, TipperRepository tipperRepository) {
        this.membershipRepository = communityMembershipRepository;
        this.communityRepository = communityRepository;
        this.tipperRepository = tipperRepository;

    }

    private static final Logger log = LoggerFactory.getLogger(CommunityMembershipServiceImpl.class);

    @Override
    public Optional<CommunityMembership> findById(Long id) {
        return membershipRepository.findById(id);
    }


    @Override
    @Transactional
    public CommunityMembership save(CommunityMembershipDto membershipDto) {
        log.debug("Service save communityMembership {}", membershipDto);
        Optional<CommunityMembership> entity = membershipRepository.findByCommIdAndTipperId(membershipDto.getCommId(), membershipDto.getTipperId());

        if (entity.isPresent()) {
            log.error("CommunityMembership already exists");
            throw new EntityExistsException("CommunityMembership  already exist with given id:" + membershipDto.getId());
        }
        CommunityMembership model = new CommunityMembership();
        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Tipper tipper = tipperRepository.findById(membershipDto.getTipperId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.setCommunity(community);
        model.setTipper(tipper);

        log.debug("save CommunityMembership {}", model);
        return membershipRepository.save(model);
    }


    @Override
    @Transactional
    public Optional<CommunityMembership> update(Long id, CommunityMembershipDto membershipDto) {

        log.debug("update CommunityMembership dto:: {}", membershipDto);
        Optional<CommunityMembership> updateModel = membershipRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("CommunityMembership  does not exits given id:" + membershipDto.getId());
        }

        Community community = communityRepository.findByName(membershipDto.getCommName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Tipper tipper = tipperRepository.findById(membershipDto.getTipperId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateModel.get().setCommunity(community);
        updateModel.get().setTipper(tipper);
        log.debug("updated CommunityMembership  with {}", updateModel.get());
        return Optional.of(membershipRepository.save(updateModel.get()));
    }
    private CommunityMembership updateFields(CommunityMembership base, CommunityMembership membership) {
        base.setTipper(membership.getTipper());
        base.setCommunity(membership.getCommunity());

        return base;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        membershipRepository.deleteById(id);
    }


    @Override
    public List<CommunityMembership> getAll() {
        return membershipRepository.findAll();
    }

    @Override
    public Optional<CommunityMembership> findByCommIdAndTipperId(Long commId, Long tipperId) {
       return membershipRepository.findByCommIdAndTipperId(commId, tipperId);
    }
}

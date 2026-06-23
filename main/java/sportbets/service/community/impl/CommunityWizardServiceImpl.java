package sportbets.service.community.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.community.CommunityWizardService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;

import java.util.List;

@Service
public class CommunityWizardServiceImpl implements CommunityWizardService {

    private static final Logger log = LoggerFactory.getLogger(CommunityWizardServiceImpl.class);

    private final TipperRepository tipperRepository;
    private final CommunityRepository communityRepository;
    private final CompetitionRepository competitionRepository;
    private final CommunityMembershipRepository membershipRepository;

    public CommunityWizardServiceImpl(TipperRepository tipperRepository, CommunityRepository communityRepository, CompetitionRepository competitionRepository, CommunityMembershipRepository membershipRepository) {
        this.tipperRepository = tipperRepository;
        this.communityRepository = communityRepository;
        this.competitionRepository = competitionRepository;
        this.membershipRepository = membershipRepository;
    }


    @Override
    @Transactional
    public CommunityDto save(CommunityWizardRecord record) {
        log.debug("save CommunityWizardRecord {}", record);
        // retrieve existing objects
        Tipper adminTipper = tipperRepository.findByUsername(record.tipperUserName()).orElseThrow(() -> new EntityNotFoundException("adminTipper not found"));
        Competition savedComp = competitionRepository.findById(record.compId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        // prepare new community
        Community newComm = new Community(record.commName(), record.commDescription());
        CommunityRole communityRole = new CommunityRole(newComm.getName(), newComm.getDescription(), newComm);
        newComm.addCommunityRole(communityRole);
        // prepare community membership
        CommunityMembership communityMembership = new CommunityMembership(newComm, adminTipper);

        // prepare competition membership
        CompetitionMembership competitionMembership = new CompetitionMembership(newComm, savedComp);
        Community savedComm = communityRepository.save(newComm);

        // set admin state:
        adminTipper.setDefaultCompetitionId(savedComp.getId());
        adminTipper.setDefaultCommunityId(savedComm.getId());
        adminTipper.addTipperRole(new TipperRole(communityRole, adminTipper));
        tipperRepository.save(adminTipper);

        List<Tipper> memberTippers = tipperRepository.findBySpecificIds(record.tipperIds());
        for (Tipper tipper : memberTippers) {
            tipper.setDefaultCommunityId(savedComm.getId());
            membershipRepository.save(new CommunityMembership(savedComm, tipper));


        }

        CommunityDto communityDto = new CommunityDto(savedComm.getId(), savedComm.getName(), savedComm.getDescription());

        log.debug("saved CommunityWizardRecord {}", communityDto);
        return communityDto;
    }
}

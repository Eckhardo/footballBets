package sportbets.service.community.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.community.CommunityWizardService;
import sportbets.web.dto.community.CommunityWizardRecord;

@Service
public class CommunityWizardServiceImpl implements CommunityWizardService {


    private static final Logger log = LoggerFactory.getLogger(CommunityWizardServiceImpl.class);
    private final TipperRepository tipperRepository;

    private final CommunityRepository communityRepository;
    private final CompetitionRepository competitionRepository;

    public CommunityWizardServiceImpl(TipperRepository tipperRepository, CommunityRepository communityRepository, CompetitionRepository competitionRepository) {
        this.tipperRepository = tipperRepository;
        this.communityRepository = communityRepository;
        this.competitionRepository = competitionRepository;
    }


    @Override
    @Transactional
    public CommunityWizardRecord save(CommunityWizardRecord record) {
        log.debug("save CommunityWizardRecord {}", record);

        Tipper tipper= tipperRepository.findByUsername(record.tipperUserName()).orElseThrow(()-> new EntityNotFoundException("tipper not found"));

        Competition competition = competitionRepository.findById(record.compId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        Community comm = new Community(record.commName(), record.commDescription());
        CommunityRole communityRole = new CommunityRole(comm.getName(), comm.getDescription(), comm);
        comm.addCommunityRole(communityRole);

        CommunityMembership communityMembership=new CommunityMembership(comm,tipper);

        CompetitionMembership competitionMembership = new CompetitionMembership(comm, competition);
        Community savedComm = communityRepository.save(comm);
        return new CommunityWizardRecord(savedComm.getName(), savedComm.getDescription(), competition.getId(), competition.getName(),tipper.getUsername());
    }
}

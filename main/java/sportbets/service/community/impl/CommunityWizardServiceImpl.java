package sportbets.service.community.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
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
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.service.community.CommunityWizardService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.competition.CompetitionMembershipDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommunityWizardServiceImpl implements CommunityWizardService {

    private static final Logger log = LoggerFactory.getLogger(CommunityWizardServiceImpl.class);

    private final TipperRepository tipperRepository;
    private final CommunityRepository communityRepository;
    private final CompetitionRepository competitionRepository;
    private final CommunityMembershipRepository membershipRepository;
    private final ModelMapper modelMapper;

    public CommunityWizardServiceImpl(TipperRepository tipperRepository, CommunityRepository communityRepository, CompetitionRepository competitionRepository, CommunityMembershipRepository membershipRepository, ModelMapper modelMapper) {
        this.tipperRepository = tipperRepository;
        this.communityRepository = communityRepository;
        this.competitionRepository = competitionRepository;
        this.membershipRepository = membershipRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public CompetitionMembershipDto save(CommunityWizardRecord record) {
        log.debug("save CommunityWizardRecord {}", record);
        // retrieve existing objects
        Tipper adminTipper = tipperRepository.findByUsername(record.tipperUserName()).orElseThrow(() -> new EntityNotFoundException("adminTipper not found"));
        Competition savedComp = competitionRepository.findById(record.compId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        // prepare new community
        Community newComm = new Community(record.commName(), record.commDescription());
        CommunityRole communityRole = new CommunityRole(newComm.getName(), newComm.getDescription(), newComm);
        newComm.addCommunityRole(communityRole);

        List<TippModusDto> tippModus = record.tippModi();
        List<TippModus> tippModi=new ArrayList<>();
        for (TippModusDto tippModusDto : tippModus) {
            final TippModus entity = convertToEntity(tippModusDto);
            entity.setType(TippModusType.fromString(tippModusDto.getType()));
            entity.setCommunity(newComm);
        }


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
            tipper.setDefaultCompetitionId(savedComp.getId());
            membershipRepository.save(new CommunityMembership(savedComm, tipper));
        }

        return new CompetitionMembershipDto(savedComp.getId(), savedComp.getName(), savedComm.getId(),savedComm.getName());

    }

    private TippModus convertToEntity(TippModusDto dto) {
        TippModus entity;
        if (dto instanceof TippModusTotoDto totoDto) {
            entity = modelMapper.map(totoDto, TippModusToto.class);
        } else if (dto instanceof TippModusPointDto pointDto) {
            entity = modelMapper.map(pointDto, TippModusPoint.class);
        } else if (dto instanceof TippModusResultDto resultDto) {
            entity = modelMapper.map(resultDto, TippModusResult.class);
        } else {
            throw new RuntimeException("Unknown tippModus type " + dto.getClass());
        }
        return entity;
    }
}

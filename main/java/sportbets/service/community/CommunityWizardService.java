package sportbets.service.community;

import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.competition.CompetitionMembershipDto;

public interface CommunityWizardService {

    CommunityDto save(CommunityWizardRecord record);

}

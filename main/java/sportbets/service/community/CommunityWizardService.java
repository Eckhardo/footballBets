package sportbets.service.community;

import sportbets.persistence.entity.community.Tipper;
import sportbets.web.dto.community.CommunityWizardRecord;
import sportbets.web.dto.community.TipperDto;

public interface CommunityWizardService {

    CommunityWizardRecord save(CommunityWizardRecord record);

}

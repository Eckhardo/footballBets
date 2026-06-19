package sportbets.web.controller.community;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sportbets.service.community.CommunityWizardService;
import sportbets.web.dto.community.CommunityWizardRecord;

@RestController
@RequestMapping("/commWizard")
class CommunityWizardController {

    private static final Logger log = LoggerFactory.getLogger(CommunityWizardController.class);

    private final CommunityWizardService wizardService;

    public CommunityWizardController(CommunityWizardService wizardService) {
        this.wizardService = wizardService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityWizardRecord post(@RequestBody @Valid CommunityWizardRecord communityWizardRecord) {
        log.debug("save CommunityWizardRecord {}", communityWizardRecord);
        return wizardService.save(communityWizardRecord);
    }
}

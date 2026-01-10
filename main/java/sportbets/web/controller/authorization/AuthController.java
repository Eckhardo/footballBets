package sportbets.web.controller.authorization;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.community.Tipper;
import sportbets.service.authorization.TipperRoleService;
import sportbets.service.community.CommunityMembershipService;
import sportbets.service.community.TipperService;
import sportbets.web.dto.authorization.LoginRequestDto;
import sportbets.web.dto.authorization.UmsInfoDto;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final TipperService tipperService;
    private final TipperRoleService tipperRoleService;
    private final CommunityMembershipService communityMembershipService;
    private final ModelMapper modelMapper;

    public AuthController(TipperService tipperService, TipperRoleService tipperRoleService, CommunityMembershipService communityMembershipService, ModelMapper modelMapper) {
        this.tipperService = tipperService;
        this.tipperRoleService = tipperRoleService;
        this.communityMembershipService = communityMembershipService;
        this.modelMapper = modelMapper;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public UmsInfoDto authenticateUser(@RequestBody @Valid LoginRequestDto loginRequest) {
        log.debug("AuthController. auth::{}", loginRequest);

        Optional<Tipper> tipper = tipperService.authenticate(loginRequest.getUserName(), loginRequest.getPassword());
        if (tipper.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        UmsInfoDto umsInfo = new UmsInfoDto(
                tipper.get().getDefaultCommunityId(),
                tipper.get().getDefaultCompetitionId(),
                tipper.get().isCommunityAdmin(),
                tipper.get().getUsername());
          umsInfo.setLoggedIn(true);
        // fetch communities and competitions where tipper has admin rights
        List<TipperRole> tipperRoles = tipperRoleService.getAllForTipper(tipper.get().getId());
        if (!tipperRoles.isEmpty()) {
            for (TipperRole tipperRole : tipperRoles) {
                if (tipperRole.getRole() instanceof CommunityRole) {
                    umsInfo.getAdminCommunities().add(((CommunityRole) tipperRole.getRole()).getCommunity().getId());
                } else {
                    umsInfo.getAdminCompetitions().add(((CompetitionRole) tipperRole.getRole()).getCompetition().getId());
                }

            }
        }
        // fetch registered communities for common tipper
        List<CommunityMembership> commMembs = communityMembershipService.findCommunities(tipper.get().getId());
        if (!commMembs.isEmpty()) {
            for (CommunityMembership communityMembership : commMembs) {
                umsInfo.getTipperCommunities().add(communityMembership.getCommunity().getId());
            }
        }
        log.info("UmsInfoDto : {}", umsInfo);
        return umsInfo;


    }
}
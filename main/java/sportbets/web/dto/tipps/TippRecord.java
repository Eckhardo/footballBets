package sportbets.web.dto.tipps;

import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.TippModus;

import java.util.List;

public record TippRecord(CommunityMembership comMemb, List<Spiel> spiele, List<TippModusDto> tippModi) {
}

package sportbets.web.dto.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.common.Country;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UmsInfoDto {


    private static final Logger log = LoggerFactory.getLogger(UmsInfoDto.class);
    private String username;
    boolean loggedIn=false;
    private boolean isCommunityAdmin;
    private boolean isCompetitionAdmin;
    private Long defaultCommunityId;
    private Long defaultCompetitionId;
    private Country defaultCountry;
    /**
     * a set of competitions where tipper has admin rights
     */
    private Set<Long> adminCompetitions=new HashSet<>();

    /**
     * a set of communities where tipper has admin rights
     */
    private Set<Long> adminCommunities=new HashSet<>();

    /**
     * a set of communities where tipper has abonnenment
     */
    private Set<Long> tipperCommunities=new HashSet<>();


    public UmsInfoDto(Long defaultCommunityId, Long defaultCompetitionId, boolean isCommunityAdmin, boolean isCompetitionAdmin, String username) {
        this.defaultCommunityId = defaultCommunityId;
        this.defaultCompetitionId = defaultCompetitionId;
        this.isCommunityAdmin = isCommunityAdmin;
        this.isCompetitionAdmin = isCompetitionAdmin;
        this.username = username;
    }

    public Set<Long> getAdminCommunities() {
        return adminCommunities;
    }

    public void setAdminCommunities(Set<Long> adminCommunities) {
        this.adminCommunities = adminCommunities;
    }

    public Set<Long> getAdminCompetitions() {
        return adminCompetitions;
    }

    public void setAdminCompetitions(Set<Long> adminCompetitions) {
        this.adminCompetitions = adminCompetitions;
    }

    public Long getDefaultCommunityId() {
        return defaultCommunityId;
    }

    public void setDefaultCommunityId(Long defaultCommunityId) {
        this.defaultCommunityId = defaultCommunityId;
    }

    public Long getDefaultCompetitionId() {
        return defaultCompetitionId;
    }

    public void setDefaultCompetitionId(Long defaultCompetitionId) {
        this.defaultCompetitionId = defaultCompetitionId;
    }

    public boolean isCommunityAdmin() {
        return isCommunityAdmin;
    }

    public void setCommunityAdmin(boolean communityAdmin) {
        isCommunityAdmin = communityAdmin;
    }

    public boolean getCompetitionAdmin() {
        return isCompetitionAdmin;
    }

    public void setCompetitionAdmin(boolean competitionAdmin) {
        isCompetitionAdmin = competitionAdmin;
    }

    public Set<Long> getTipperCommunities() {
        return tipperCommunities;
    }

    public void setTipperCommunities(Set<Long> tipperCommunities) {
        this.tipperCommunities = tipperCommunities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Country getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(Country defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    @Override
    public String toString() {
        return "UmsInfoDto{" +
                "defaultCommunityId=" + defaultCommunityId +
                ", defaultCompetitionId=" + defaultCompetitionId +
                ", username='" + username + '\'' +
                ", isCommunityAdmin=" + isCommunityAdmin +
                ", size tipper comms=" + tipperCommunities.size() +
                ", size admin comms=" + adminCommunities.size() +
                ", size admin comps=" + adminCompetitions.size() +
                ", isCompetitionAdmin=" + isCompetitionAdmin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UmsInfoDto that = (UmsInfoDto) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}

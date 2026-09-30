package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.entity.competition.Spieltag;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TippConfig {


    private static final Logger log = LoggerFactory.getLogger(TippConfig.class);
    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tippModus", foreignKey = @ForeignKey(name = "FK_TIPP_CONFIG_TO_MODUS"))
    @NotNull
    TippModus tippModus;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Owning Side:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_matchday", foreignKey = @ForeignKey(name = "FK_TIPP_CONFIG_TO_COMP_MEMB"))
    @NotNull
    private Spieltag spieltag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_memb", foreignKey = @ForeignKey(name = "FK_TIPP_CONFIG_TO_MATCHDAY"))
    @NotNull
    private CompetitionMembership competitionMembership;

    public TippConfig() {

    }

    public TippConfig(Spieltag spieltag, CompetitionMembership competitionMembership, TippModus tippModus) {
        this.spieltag = spieltag;
        this.competitionMembership = competitionMembership;
        this.tippModus = tippModus;
        spieltag.addTippConfig(this);
        competitionMembership.addTippConfig(this);
        tippModus.addTippConfig(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Spieltag getSpieltag() {
        return spieltag;
    }

    public void setSpieltag(Spieltag spieltag) {
        this.spieltag = spieltag;
    }

    public CompetitionMembership getCompetitionMembership() {
        return competitionMembership;
    }

    public void setCompetitionMembership(CompetitionMembership competitionMembership) {
        this.competitionMembership = competitionMembership;
    }

    public TippModus getTippModus() {
        return tippModus;
    }

    public void setTippModus(TippModus tippModus) {
        this.tippModus = tippModus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippConfig config = (TippConfig) o;
        return Objects.equals(createdOn, config.createdOn) && Objects.equals(tippModus.getId(), config.tippModus.getId()) && Objects.equals(spieltag.getId(), config.spieltag.getId()) && Objects.equals(competitionMembership.getId(), config.competitionMembership.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdOn, tippModus.getId(), spieltag.getId(), competitionMembership.getId());
    }

    @Override
    public String toString() {
        return "TippConfig{" +
                "createdOn=" + createdOn +
                ", tippModusId=" + tippModus.getId() +
                ", spieltagId=" + spieltag.getId() +
                ", competitionMembershipId=" + competitionMembership.getId() +
                '}';
    }
}
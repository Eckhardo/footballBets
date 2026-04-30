package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.entity.competition.Spieltag;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TippConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tippModus_id",foreignKey = @ForeignKey(name = "FK_TIPP_CONFIG_TO_MODUS"))
    @NotNull
    TippModus tippModus;

    @OneToOne(mappedBy = "tippConfig")
    private Spieltag spieltag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comp_memb_id",foreignKey = @ForeignKey(name = "FK_TIPP_CONFIG_TO_COMP_MEMB"))
    @NotNull
    private CompetitionMembership competitionMembership;

    public TippConfig() {

    }

    public TippConfig(Spieltag spieltag, CompetitionMembership competitionMembership, TippModus tippModus   ) {
        this.spieltag = spieltag;
        this.competitionMembership = competitionMembership;
        this.tippModus = tippModus;
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

    public CompetitionMembership getCompetitionMembership() {
        return competitionMembership;
    }

    public TippModus getTippModus() {
        return tippModus;
    }

    public void setTippModus(TippModus tippModus) {
        this.tippModus = tippModus;
    }

    public void setSpieltag(Spieltag spieltag) {
        this.spieltag = spieltag;
    }

    public void setCompetitionMembership(CompetitionMembership competitionMembership) {
        this.competitionMembership = competitionMembership;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippConfig that = (TippConfig) o;
        return Objects.equals(id, that.id) && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdOn);
    }
}
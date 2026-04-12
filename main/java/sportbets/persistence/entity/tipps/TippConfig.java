package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.entity.competition.Spieltag;

import java.time.LocalDateTime;

@Entity
public class TippConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tippModus_id",foreignKey = @ForeignKey(name = "FK_SPIELTAG_TO_ROUND"))
    @NotNull
    TippModus tippModus;

    @OneToOne(mappedBy = "tippConfig")
    private Spieltag spieltag;

    @OneToOne(mappedBy = "tippConfig")
    private CompetitionMembership competitionMembership;

    public TippConfig() {

    }

    public TippConfig(Spieltag spieltag, CompetitionMembership competitionMembership, TippModus tippModus   ) {
        this.spieltag = spieltag;
        spieltag.setTippConfig(this);
        this.competitionMembership = competitionMembership;
        competitionMembership.setTippConfig(this);
        this.tippModus = tippModus;
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

    public CompetitionMembership getCompetitionMembership() {
        return competitionMembership;
    }

}
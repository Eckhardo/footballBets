package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Tipp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_spiel_id", foreignKey = @ForeignKey(name = "FK_TIPP_TO_GAME"))
    @NotNull
    public Spiel spiel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_commMemb_id", foreignKey = @ForeignKey(name = "FK_TIPP_TO_COMP_MEMB"))
    @NotNull
    private CommunityMembership communityMembership;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tippModus_id", foreignKey = @ForeignKey(name = "FK_TIPP_TO_TIPPMODUS"))
    @NotNull
    private TippModus tippModus;


    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();


    private Integer heimTipp;


    private Integer remisTipp;

    private Integer gastTipp;

    private Integer winPoints;


    public Tipp() {
    }

    public Tipp(Spiel spiel, CommunityMembership commMemb, TippModus tippModus) {
        this.spiel = spiel;
        spiel.addTipp(this);
        this.communityMembership = commMemb;
        commMemb.addTipp(this);
        this.tippModus = tippModus;
        tippModus.addTipp(this);

    }

    public Tipp(Spiel spiel, CommunityMembership commMemb, TippModus tippModus, Integer heimTipp, Integer remisTipp, Integer gastTipp, Integer winPoints) {
        this(spiel, commMemb, tippModus);
        this.heimTipp = heimTipp;
        this.remisTipp = remisTipp;
        this.gastTipp = gastTipp;
        this.winPoints = winPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public Integer getHeimTipp() {
        return heimTipp;
    }

    public void setHeimTipp(Integer heimTipp) {
        this.heimTipp = heimTipp;
    }

    public Integer getRemisTipp() {
        return remisTipp;
    }

    public void setRemisTipp(Integer remisTipp) {
        this.remisTipp = remisTipp;
    }

    public Integer getGastTipp() {
        return gastTipp;
    }

    public void setGastTipp(Integer gastTipp) {
        this.gastTipp = gastTipp;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(Integer winPoints) {
        this.winPoints = winPoints;
    }

    public CommunityMembership getCommunityMembership() {
        return communityMembership;
    }

    public void setCommunityMembership(CommunityMembership communityMembership) {
        this.communityMembership = communityMembership;
    }

    public TippModus getTippModus() {
        return tippModus;
    }

    public void setTippModus(TippModus tippModus) {
        this.tippModus = tippModus;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
// override methods


    @Override
    public String toString() {
        return "Tipp{" +
                "id=" + id +
                ", spielNumber=" + spiel.getSpielNumber() +
                ", createdOn=" + createdOn +
                ", heimTipp=" + heimTipp +
                ", remisTipp=" + remisTipp +
                ", gastTipp=" + gastTipp +
                ", winPoints=" + winPoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tipp tipp = (Tipp) o;
        return Objects.equals(createdOn, tipp.createdOn) && Objects.equals(heimTipp, tipp.heimTipp) && Objects.equals(remisTipp, tipp.remisTipp) && Objects.equals(gastTipp, tipp.gastTipp) && Objects.equals(winPoints, tipp.winPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdOn, heimTipp, remisTipp, gastTipp, winPoints);
    }
}
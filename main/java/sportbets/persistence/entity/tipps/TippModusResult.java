package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.tipps.enums.TotoTrend;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueNameAndCommunity", columnNames = {"name", "fk_comm_id"})})
public class TippModusResult extends TippModus {


    private static final Logger log = LoggerFactory.getLogger(TippModusResult.class);
    //	 ********************** Fields ********************** //
    private Integer tendencyPoints;

    private Integer bonusPoints;

    //	 ********************** Constructors ********************** //

    /**
     * No-arg constructor for JavaBean tools.
     */
    public TippModusResult() {
        super();
    }

    /**
     * Full constructor
     */
    public TippModusResult(String name, TippModusType type, Integer deadline, Community community, Integer tendencyPoints, Integer bonusPoints) {
        super(name, type, deadline, community);
        this.tendencyPoints = tendencyPoints;
        this.bonusPoints = bonusPoints;
    }

    //	 ********************** Getter/Setter Methods ********************** //

    /**
     * @return Returns the tendencyPoints.
     */
    public Integer getTendencyPoints() {
        return tendencyPoints;
    }

    /**
     * @param tendencyPoints The tendencyPoints to set.
     */
    public void setTendencyPoints(Integer tendencyPoints) {
        this.tendencyPoints = tendencyPoints;
    }

    /**
     * @return Returns the bonusPoints.
     */
    public Integer getBonusPoints() {
        return bonusPoints;
    }

    /**
     * @param bonusPoints The bonusPoints to set.
     */
    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    //	 ********************** Business Methods ********************** //
    @Override
    public boolean isTippValid(@NotNull Tipp tipp) {
        log.debug("validate tipp");
        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();
        tipp.setHeimTipp(heim);
        tipp.setGastTipp(gast);

        return true;
    }

    @Override
    public int calculateWinPoints(Tipp tipp, Spiel spiel) {
        log.debug("calculate result {}", this.bonusPoints);
        if (!spiel.isStattgefunden()) {
            return 0;
        }


        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();
        int winPoints = 0;

        TotoTrend trend = spiel.retrieveTotoTrend();

        switch (trend) {
            case HOME_VICTORY:
                if (heim > gast) {
                    winPoints = this.tendencyPoints;
                }
                break;
            case DRAW:
                if (heim == gast) {
                    winPoints = this.tendencyPoints;
                }
                break;

            case GUEST_VICTORY:
                if (heim < gast) {
                    winPoints = this.tendencyPoints;
                }
                break;
            default:


        }
        int bonus=this.calculateTendencyPoints(heim, gast, spiel);
        log.debug("bonus point: {}", bonus);
        log.debug("tendencyPoints points: {}", winPoints);
        return winPoints +bonus ;
    }

    private int calculateTendencyPoints(int heim, int gast, Spiel spiel) {
        if (heim == spiel.getHeimTore() && gast == spiel.getGastTore()) {
            return this.bonusPoints;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "TippModusResult {" +
                "tendencyPoints=" + tendencyPoints +
                ", bonusPoints=" + bonusPoints +
                super.toString() +
                '}';
    }
}

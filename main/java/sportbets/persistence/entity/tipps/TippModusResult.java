package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.tipps.enums.TotoTrend;
import sportbets.web.dto.tipps.TippDto;

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
     * @param tendencyPoints The tendencyPoints to set.
     */
    public void setTendencyPoints(Integer tendencyPoints) {
        this.tendencyPoints = tendencyPoints;
    }

    /**
     * @return Returns the tendencyPoints.
     */
    public Integer getTendencyPoints() {
        return tendencyPoints;
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
    public boolean isTippValid(TippDto tipp) {
        log.debug("validate result");
        boolean heim = tipp.getHeimTipp() != null;
        boolean gast = tipp.getGastTipp() != null;
        return heim && gast;
    }

    @Override
    public int calculateWinPoints(TippDto tipp, Spiel spiel) {
        log.debug("calculate result");
        if (!spiel.isStattgefunden()) {
            return 0;
        }

        int bonusPoints = 0;

        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();

        TotoTrend trend = spiel.retrieveTotoTrend();
        switch (trend) {
            case HOME_VICTORY:
                if (heim > gast) {
                    bonusPoints = this.bonusPoints;
                }
                break;
            case DRAW:
                if (heim == gast) {
                    bonusPoints = this.bonusPoints;
                }
                break;

            case GUEST_VICTORY:
                if (heim < gast) {
                    bonusPoints = this.bonusPoints;
                }
                break;
            default:


        }
        return bonusPoints + this.calculateTendencyPoints(heim, gast, spiel);
    }

    private int calculateTendencyPoints(int heim, int gast, Spiel spiel) {
        if (heim == spiel.getHeimTore() && gast == spiel.getGastTore()) {
            return this.bonusPoints;
        }
        return 0;

    }
}

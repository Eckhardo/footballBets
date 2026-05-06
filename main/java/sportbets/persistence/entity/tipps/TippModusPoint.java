package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.web.dto.tipps.TippDto;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueNameAndCommunity", columnNames = {"name", "fk_comm_id"})})
public class TippModusPoint extends TippModus {
    private static final Logger log = LoggerFactory.getLogger(TippModusPoint.class);


    //	 ********************** Fields ********************** //
    /**
     * total points to be verteilt
     */
    private Integer totalPoints;

    //	 ********************** Constructors ********************** //

    /**
     * No-arg constructor for JavaBean tools.
     */
    public TippModusPoint() {
        super();
    }

    /**
     * Full constructor.
     */
    public TippModusPoint(String name, TippModusType type, Integer deadline, Community community, Integer totalPoints) {
        super(name, type, deadline, community);
        this.totalPoints = totalPoints;

    }

    // ********************** Accessor Methods ********************** //

    /**
     * @return Returns the totalPoints.
     */
    public Integer getTotalPoints() {
        return totalPoints;
    }

    /**
     * @param totalPoints The totalPoints to set.
     */
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    } // ********************** Common Methods ********************** //

    //	 ********************** Business Methods ********************** //
    @Override
    public boolean isTippValid(TippDto tipp) {
        log.debug("validate point");
        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int remis = tipp.getRemisTipp() == null ? 0 : tipp.getRemisTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();

        return heim + remis + gast == totalPoints;
    }

    @Override
    public int calculateWinPoints(TippDto tipp, Spiel spiel) {
        log.debug("calculate point");
        if (!spiel.isStattgefunden()) {
            return 0;
        }

        int sum = 0;
        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int remis = tipp.getRemisTipp() == null ? 0 : tipp.getRemisTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();

        if (spiel.getHeimTore() > spiel.getGastTore()) {
            sum += heim;
        }
        if (spiel.getGastTore() > spiel.getHeimTore()) {
            sum += gast;

        }
        if (spiel.getHeimTore() == spiel.getGastTore()) {
            sum += remis;
        }
        return sum;
    }
}

package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.community.Community;

@Entity
public class TippModusPoint extends TippModus{
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
    public TippModusPoint(TippModusType name, Integer deadline, Community community, Integer totalPoints) {
        super(name,deadline, community);
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
     * @param totalPoints
     *            The totalPoints to set.
     */
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    } // ********************** Common Methods ********************** //

    //	 ********************** Business Methods ********************** //

}

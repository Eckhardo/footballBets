package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.community.Community;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueNameAndCommunity", columnNames = { "name", "fk_comm_id" })})
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
    public TippModusPoint(String name, TippModusType type, Integer deadline, Community community, Integer totalPoints) {
        super(name,type,deadline, community);
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

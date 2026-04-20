package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.community.Community;

@Entity
public class TippModusResult extends TippModus{
    //	 ********************** Fields ********************** //
    private Integer tendencyPoints;

    private Integer bonusPoints;

    //	 ********************** Constructors ********************** //
    /**
     * No-arg constructor for JavaBean tools.
     */
  public  TippModusResult() {
        super();
    }

    /**
     * Full constructor
     */
    public TippModusResult(TippModusType name, Integer deadline, Community community, Integer tendencyPoints, Integer bonusPoints) {
        super(name,deadline, community);
        this.tendencyPoints = tendencyPoints;
        this.bonusPoints = bonusPoints;
    }

    //	 ********************** Getter/Setter Methods ********************** //

    /**
     * @param tendencyPoints
     *            The tendencyPoints to set.
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
     * @param bonusPoints
     *            The bonusPoints to set.
     */
    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    //	 ********************** Business Methods ********************** //

}

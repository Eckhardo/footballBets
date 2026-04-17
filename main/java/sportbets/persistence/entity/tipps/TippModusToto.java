package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import sportbets.common.TippModusType;
import sportbets.persistence.entity.community.Community;


@Entity
public class TippModusToto extends TippModus{
    //	 ********************** Fields ********************** //
    /**
     * No-arg constructor for JavaBean tools.
     */

    public TippModusToto() {

    }

    public TippModusToto(TippModusType name, Integer deadline, Community community) {
        super(name,deadline, community);
    }


    //	 ********************** Business Methods ********************** //

}

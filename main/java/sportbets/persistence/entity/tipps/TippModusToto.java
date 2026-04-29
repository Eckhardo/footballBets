package sportbets.persistence.entity.tipps;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.community.Community;


@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueNameAndCommunity", columnNames = { "name", "fk_comm_id" })})
public class TippModusToto extends TippModus{
    //	 ********************** Fields ********************** //
    /**
     * No-arg constructor for JavaBean tools.
     */

    public TippModusToto() {

    }

    public TippModusToto(String name,TippModusType type, Integer deadline, Community community) {
        super( name,type,deadline, community );
    }


    //	 ********************** Business Methods ********************** //

}

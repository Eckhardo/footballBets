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
public class TippModusToto extends TippModus {
    private static final Logger log = LoggerFactory.getLogger(TippModusToto.class);

    //	 ********************** Fields ********************** //

    /**
     * No-arg constructor for JavaBean tools.
     */

    public TippModusToto() {

    }

    public TippModusToto(String name, TippModusType type, Integer deadline, Community community) {
        super(name, type, deadline, community);
    }


    //	 ********************** Business Methods ********************** //

    @Override
    public boolean isTippValid(TippDto tipp) {
        log.debug("validate toto");
        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int remis = tipp.getRemisTipp() == null ? 0 : tipp.getRemisTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();

        return heim + remis + gast == 1;
    }

    @Override
    public int calculateWinPoints(TippDto tipp, Spiel spiel) {
        log.debug("calculate toto");
        if (!spiel.isStattgefunden()) {
            return 0;
        }
        int heim = tipp.getHeimTipp() == null ? 0 : tipp.getHeimTipp();
        int remis = tipp.getRemisTipp() == null ? 0 : tipp.getRemisTipp();
        int gast = tipp.getGastTipp() == null ? 0 : tipp.getGastTipp();

        if (spiel.getHeimTore()> spiel.getGastTore()) {
            return heim ;
        } else if (spiel.getGastTore()> spiel.getHeimTore()) {
            return gast;

        }
        else{
            return remis;
        }

    }
}

package sportbets.web.dto.competition.search;

import java.io.Serializable;

public class TableSearchCriteria implements Serializable {
    private final Long compId;
    private final Integer startSpieltag;
    private final Integer endSpieltag;
    private final Boolean isHeimOrGast;






    public TableSearchCriteria(Long compId, Integer startSpieltag, Integer endSpieltag, Boolean isHeimOrGast ) {
        this.compId = compId;
        this.endSpieltag = endSpieltag;
        this.isHeimOrGast = isHeimOrGast;
        this.startSpieltag = startSpieltag;
    }

    public Long getCompId() {
        return compId;
    }
    public Integer getEndSpieltag() {
        return endSpieltag;
    }

    public Boolean getHeimOrGast() {
        return isHeimOrGast;
    }

    public Integer getStartSpieltag() {
        return startSpieltag;
    }
}

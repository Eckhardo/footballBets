package sportbets.web.dto.competition.search;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TableSearchCriteria that = (TableSearchCriteria) o;
        return Objects.equals(compId, that.compId) && Objects.equals(startSpieltag, that.startSpieltag) && Objects.equals(endSpieltag, that.endSpieltag) && Objects.equals(isHeimOrGast, that.isHeimOrGast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compId, startSpieltag, endSpieltag, isHeimOrGast);
    }

    @Override
    public String toString() {
        return "TableSearchCriteria{" +
                "compId=" + compId +
                ", startSpieltag=" + startSpieltag +
                ", endSpieltag=" + endSpieltag +
                ", isHeimOrGast=" + isHeimOrGast +
                '}';
    }
}

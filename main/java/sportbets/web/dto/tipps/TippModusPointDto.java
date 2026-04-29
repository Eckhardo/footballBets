package sportbets.web.dto.tipps;

import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippModusPoint}
 */
public class TippModusPointDto extends TippModusDto implements Serializable {
    @Positive
    private Integer totalPoints;

    public TippModusPointDto() {
        super();
    }


    public TippModusPointDto(Long id, String name, String type, Integer deadline, Long commId, String commName, Integer totalPoints) {
        super(id,name , type, deadline, commId, commName);
        this.totalPoints = totalPoints;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TippModusPointDto that = (TippModusPointDto) o;
        return Objects.equals(totalPoints, that.totalPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalPoints);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
package sportbets.web.dto.tipps;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippModusResult}
 */
public class TippModusResultDto extends TippModusDto implements Serializable {

    @Positive(message = "Must be positive number")
    private Integer tendencyPoints;
    @PositiveOrZero
    private Integer bonusPoints;

    public TippModusResultDto() {
        super();
    }

    public TippModusResultDto(Long id, String name, String type, Integer deadline, Long commId, String commName, Integer tendencyPoints, Integer bonusPoints) {
        super(id,name, type, deadline, commId, commName);
        this.tendencyPoints = tendencyPoints;
        this.bonusPoints = bonusPoints;
    }

    public Integer getTendencyPoints() {
        return tendencyPoints;
    }

    public void setTendencyPoints(Integer tendencyPoints) {
        this.tendencyPoints = tendencyPoints;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TippModusResultDto that = (TippModusResultDto) o;

        return Objects.equals(tendencyPoints, that.tendencyPoints) && Objects.equals(bonusPoints, that.bonusPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tendencyPoints, bonusPoints);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", tendencyPoints=" + tendencyPoints +
                ", bonusPoints=" + bonusPoints +
                '}';
    }
}
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
        super(id, name, type, deadline, commId, commName);
        this.totalPoints = totalPoints;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getDescription() {
        return "Verteilungspunkte: Sie bestimmen eine Anzahl von Punkten, die fei nach Wahl auf Heimsieg, Auswärtssieg oder unentschieden verteilt werden. " +
                "Ist die Anzahl der Verteilungspunkte z. B. auf 4 festgelegt, können diese 4 Punkte in allen möglichen Variationen verteilt werden( z.B. 4:0:0, 0:0:4 oder auch 1:0:3 oder 2:2:0 ";
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
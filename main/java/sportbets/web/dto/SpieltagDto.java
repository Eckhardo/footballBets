package sportbets.web.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.Spieltag}
 */
public class SpieltagDto implements Serializable {
    private Long id;
    @NotNull(message = " spieltag number cannot be null")
    private int spieltagNumber;
    private Date startDate;
    @NotNull
    private Long compRoundId;

    public SpieltagDto() {
    }

    public SpieltagDto(Long id, int spieltagNumber, Date startDate) {
        this.id = id;
        this.spieltagNumber = spieltagNumber;
        this.startDate = startDate;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(int spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getCompRoundId() {
        return compRoundId;
    }

    public void setCompRoundId(Long compRoundId) {
        this.compRoundId = compRoundId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpieltagDto entity = (SpieltagDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.spieltagNumber, entity.spieltagNumber) &&
                Objects.equals(this.startDate, entity.startDate) &&
                Objects.equals(this.compRoundId, entity.compRoundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spieltagNumber, startDate,compRoundId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "spieltagNumber = " + spieltagNumber + ", " +
                "startDate = " + startDate + ", " +
                "compRoundId = " + compRoundId + ")";
    }
}
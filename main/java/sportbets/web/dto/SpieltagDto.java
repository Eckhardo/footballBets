package sportbets.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.Spieltag}
 */

public class SpieltagDto implements Serializable {
    private Long id;
    @NotNull(message = " spieltag number cannot be null")
    private int spieltagNumber;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;
    @NotNull
    private Long compRoundId;

    public SpieltagDto() {
    }

    public SpieltagDto(Long id, int spieltagNumber, LocalDateTime startDate) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
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
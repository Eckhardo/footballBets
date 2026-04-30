package sportbets.web.dto.tipps;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippConfig}
 */
public class TippConfigDto implements Serializable {
    private  Long id;
    @NotNull(message = " compMemb id cannot be null")
    private  Long compMembId;
    @NotNull(message = " spieltag id cannot be null")
    private  Long spieltagId;
    @NotNull(message = " spieltag number cannot be null")
    private  int spieltagNumber;
    @NotNull(message = " tipp modus id cannot be null")
    private  Long tippModusId;


    public TippConfigDto(Long id,  Long compMembId, Long spieltagId, int spieltagNumber,Long tippModusId) {
        this.id = id;
        this.compMembId = compMembId;
        this.spieltagId = spieltagId;
        this.spieltagNumber = spieltagNumber;
        this.tippModusId = tippModusId;
    }

    public Long getId() {
        return id;
    }


    public Long getCompMembId() {
        return compMembId;
    }

    public Long getSpieltagId() {
        return spieltagId;
    }

    public Long getTippModusId() {
        return tippModusId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompMembId(Long compMembId) {
        this.compMembId = compMembId;
    }

    public void setSpieltagId(Long spieltagId) {
        this.spieltagId = spieltagId;
    }

    public void setTippModusId(Long tippModusId) {
        this.tippModusId = tippModusId;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(int spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippConfigDto that = (TippConfigDto) o;
        return spieltagNumber == that.spieltagNumber && Objects.equals(compMembId, that.compMembId) && Objects.equals(spieltagId, that.spieltagId) && Objects.equals(tippModusId, that.tippModusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compMembId, spieltagId, spieltagNumber, tippModusId);
    }

    @Override
    public String toString() {
        return "TippConfigDto{" +
                "compMembId=" + compMembId +
                ", spieltagId=" + spieltagId +
                ", spieltagNumber=" + spieltagNumber +
                ", tippModusId=" + tippModusId +
                '}';
    }
}
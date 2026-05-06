package sportbets.web.dto.tipps;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.Tipp}
 */
public class TippDto implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TippDto.class);

    private Long id;
    private Integer heimTipp;
    private Integer remisTipp;
    private Integer gastTipp;
    private Integer winPoints;
    @NotNull(message = " spielId id cannot be null")
    private Long spielId;
    private Integer spielNumber;
    @NotNull(message = " tippModus id cannot be null")
    private Long tippModusId;
    private String tippModusType;
    @NotNull(message = " commMembership id cannot be null")
    private Long commMembId;

    public TippDto() {
    }
    public TippDto(Long id, Integer heimTipp, Integer remisTipp, Integer gastTipp, Integer winPoints) {
        this.id = id;
        this.heimTipp = heimTipp;
        this.remisTipp = remisTipp;
        this.gastTipp = gastTipp;
        this.winPoints = winPoints;

    }


    public TippDto(Long id, Integer heimTipp, Integer remisTipp, Integer gastTipp, Integer winPoints, Long spielId, Integer spielNumber, Long tippModusIdId, String tippModusType, Long commMembId) {
        this(id, heimTipp, remisTipp, gastTipp, winPoints);
        this.spielId = spielId;
        this.spielNumber = spielNumber;
        this.tippModusId = tippModusIdId;
        this.tippModusType = tippModusType;
        this.commMembId = commMembId;
    }

    public Long getId() {
        return id;
    }

    public Integer getHeimTipp() {
        return heimTipp;
    }

    public Integer getRemisTipp() {
        return remisTipp;
    }

    public Integer getGastTipp() {
        return gastTipp;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeimTipp(Integer heimTipp) {
        this.heimTipp = heimTipp;
    }

    public void setRemisTipp(Integer remisTipp) {
        this.remisTipp = remisTipp;
    }

    public void setGastTipp(Integer gastTipp) {
        this.gastTipp = gastTipp;
    }

    public void setWinPoints(Integer winPoints) {
        this.winPoints = winPoints;
    }

    public Long getSpielId() {
        return spielId;
    }

    public void setSpielId(Long spielId) {
        this.spielId = spielId;
    }

    public Integer getSpielNumber() {
        return spielNumber;
    }

    public void setSpielNumber(Integer spielNumber) {
        this.spielNumber = spielNumber;
    }

    public Long getTippModusId() {
        return tippModusId;
    }

    public void setTippModusId(Long tippModusId) {
        this.tippModusId = tippModusId;
    }

    public String getTippModusType() {
        return tippModusType;
    }

    public void setTippModusType(String tippModusType) {
        this.tippModusType = tippModusType;
    }

    public Long getCommMembId() {
        return commMembId;
    }

    public void setCommMembId(Long commMembId) {
        this.commMembId = commMembId;
    }

    @Override
    public String toString() {
        return "TippDto{" +
                "heimTipp=" + heimTipp +
                ", remisTipp=" + remisTipp +
                ", gastTipp=" + gastTipp +
                ", spielId=" + spielId +
                ", spielNumber='" + spielNumber + '\'' +
                ", tippModusType='" + tippModusType + '\'' +
                ", commMembId=" + commMembId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippDto tippDto = (TippDto) o;
        return Objects.equals(heimTipp, tippDto.heimTipp) && Objects.equals(remisTipp, tippDto.remisTipp) && Objects.equals(gastTipp, tippDto.gastTipp) && Objects.equals(spielId, tippDto.spielId) && Objects.equals(tippModusId, tippDto.tippModusId) && Objects.equals(commMembId, tippDto.commMembId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heimTipp, remisTipp, gastTipp, spielId, tippModusId, commMembId);
    }
}
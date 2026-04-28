package sportbets.web.dto.tipps;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippConfig}
 */
public class TippConfigDto implements Serializable {
    private final Long id;
    private final LocalDateTime createdOn;
    @NotNull(message = " compMemb id cannot be null")
    private final Long compMembId;
    @NotNull(message = " spieltag id cannot be null")
    private final Long spieltagId;
    @NotNull(message = " tipp modus id cannot be null")
    private final Long tippModusId;


    public TippConfigDto(Long id, LocalDateTime createdOn, Long compMembId, Long spieltagId, Long tippModusId) {
        this.id = id;
        this.createdOn = createdOn;
        this.compMembId = compMembId;
        this.spieltagId = spieltagId;
        this.tippModusId = tippModusId;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
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
}
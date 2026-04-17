package sportbets.web.dto.tipps;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sportbets.common.TippModusType;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippModusToto}
 */
public class TippModusTotoDto extends TippModusDto implements Serializable {

    public TippModusTotoDto() {
        super();
    }


    public TippModusTotoDto(Long id, String type, Integer deadline, Long commId, String commName) {
        super(id, type, deadline, commId, commName);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
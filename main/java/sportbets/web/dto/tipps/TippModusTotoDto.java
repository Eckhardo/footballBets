package sportbets.web.dto.tipps;

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

}
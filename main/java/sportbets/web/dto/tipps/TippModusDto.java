package sportbets.web.dto.tipps;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link sportbets.persistence.entity.tipps.TippModus}
 */
public class TippModusDto implements Serializable {
    private Long id;
    @NotNull
    private String type;
    @NotNull
    @Positive(message = "Must be positive value")
    private  Integer deadline;

    @NotNull(message = " community id cannot be null")
    private Long commId;
    private String commName;



    public TippModusDto() {

    }

    public TippModusDto(Long id, String type, Integer deadline, Long commId, String commName) {
        this.id = id;
        this.type = type;
        this.deadline = deadline;
        this.commId = commId;
        this.commName = commName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippModusDto that = (TippModusDto) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(deadline, that.deadline) && Objects.equals(commId, that.commId) && Objects.equals(commName, that.commName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, deadline, commId, commName);
    }

    @Override
    public String toString() {
        return "TippModusDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", deadline=" + deadline +
                ", commId=" + commId +
                ", commName='" + commName + '\'' +
                '}';
    }
}
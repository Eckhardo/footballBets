package sportbets.web.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CommunityWizardRecord(

        @NotBlank(message = "community Name must not be blank")
        @Size(min = 5, max = 20, message = "commName must be between 3 and 20 characters")
        String commName,
        @NotBlank(message = "comm description must not be blank")
        String commDescription,
        @NotNull(message = "comp id must not be null")
        Long compId,
        @NotBlank(message = "competition Name must not be blank")
        String compName,
        @NotBlank(message = "tipper Name must not be blank")
        String tipperUserName,
        List<Long> tipperIds
) {

    public CommunityWizardRecord {
        if (commName != null) {
            commName = commName.strip();
        }
        if (commDescription != null) {
            commDescription = commDescription.strip();
        }
        if (compName != null) {
            compName = compName.strip();
        }
        if (tipperUserName != null) {
            tipperUserName = tipperUserName.strip();
        }
    }
}

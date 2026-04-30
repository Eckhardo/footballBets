package sportbets.persistence.rowObject;

import java.time.LocalDateTime;

public record SpielRecord(Long id, LocalDateTime anpfiffDate, String heimTeam, String gastTeam, Integer heimTore,
                          Integer gastTore, boolean stattgefunden) {
}

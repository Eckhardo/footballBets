package sportbets.persistence.rowObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SpielRow(Long id, LocalDateTime anpfiffDate, String heimTeam, String gastTeam, Integer heimTore, Integer gastTore, boolean stattgefunden) {
}

/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.community;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CommunityWizardTippModusRecord(
        @NotNull(message = " name cannot be null")
        String name,
        @NotNull(message = " type cannot be null")
        String type,
        @NotNull
        @Positive(message = "Must be positive value")
        Integer deadline,
        Integer totalPoints,
        Integer tendencyPoints,
        Integer bonusPoints


) {
}

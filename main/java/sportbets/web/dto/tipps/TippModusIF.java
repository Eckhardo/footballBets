/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // The key in your JSON that identifies the class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TippModusPointDescription.class, name = "tippModusPointDescription"),
        @JsonSubTypes.Type(value = TippModusTotoDescription.class, name = "tippModusTotoDescription"),
        @JsonSubTypes.Type(value = TippModusResultDescription.class, name = "tippModusResultDescription")
})
public interface TippModusIF {
    String getType();

    String getName();

    String getDescription();
}

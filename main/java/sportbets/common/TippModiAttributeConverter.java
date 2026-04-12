package sportbets.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TippModiAttributeConverter implements AttributeConverter<TippModi, String> {
    @Override
    public String convertToDatabaseColumn(TippModi attribute) {

        if (attribute == null)
            return null;

        return switch (attribute) {
            case TIPPMODUS_POINT -> attribute.getDisplayName();
            case TIPPMODUS_TOTO -> attribute.getDisplayName();
            case TIPPMODUS_RESULT -> attribute.getDisplayName();

            default -> throw new IllegalArgumentException(attribute + " not supported.");
        };
    }

    @Override
    public TippModi convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case "TOTO_TIPP" -> TippModi.TIPPMODUS_TOTO;
            case "RESULT_TIPP" -> TippModi.TIPPMODUS_RESULT;
            case "POINT_TIPP" -> TippModi.TIPPMODUS_POINT;

            default -> throw new IllegalArgumentException(dbData + " not supported.");
        };
    }
}
package sportbets.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TippModusTypeAttributeConverter implements AttributeConverter<TippModusType, String> {
    @Override
    public String convertToDatabaseColumn(TippModusType attribute) {

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
    public TippModusType convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case "TotoTipp" -> TippModusType.TIPPMODUS_TOTO;
            case "ResultTipp" -> TippModusType.TIPPMODUS_RESULT;
            case "PointTipp" -> TippModusType.TIPPMODUS_POINT;

            default -> throw new IllegalArgumentException(dbData + " not supported.");
        };
    }
}
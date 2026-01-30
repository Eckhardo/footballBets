package sportbets.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CountryAttributeConverter implements AttributeConverter<Country, String> {

    @Override
    public String convertToDatabaseColumn(Country attribute) {
        if (attribute == null)
            return null;

        return switch (attribute) {
            case GERMANY -> attribute.getIsoCode();
            case ENGLAND -> attribute.getIsoCode();
            case ITALY -> attribute.getIsoCode();
            case SPAIN -> attribute.getIsoCode();
            default -> throw new IllegalArgumentException(attribute + " not supported.");
        };
    }

    @Override
    public Country convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case "DE" ->  Country.GERMANY;
            case "IT" -> Country.ITALY;
            case "EN" -> Country.ENGLAND;
            case "ES" -> Country.SPAIN;
            default -> throw new IllegalArgumentException(dbData + " not supported.");
        };
    }

}


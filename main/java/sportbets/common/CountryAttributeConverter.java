package sportbets.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter()
public class CountryAttributeConverter implements AttributeConverter<Country, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Country attribute) {
        if (attribute == null)
            return null;

        switch (attribute) {
            case GERMANY:
                return 1;

            case ENGLAND:
                return 2;

            case ITALY:
                return 3;

            case SPAIN:
                return 4;


            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Country convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
            case 1:
                return Country.GERMANY;

            case 2:
                return Country.ITALY;

            case 3:
                return Country.ENGLAND;

            case 4:
                return Country.SPAIN;

            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }

}


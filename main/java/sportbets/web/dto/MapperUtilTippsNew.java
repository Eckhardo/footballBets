package sportbets.web.dto;

import org.modelmapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sportbets.common.TippModusType;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;



/*
*  Wokrs only for one type of TippModus for unknown reasons
 */
@Deprecated(since = "20.04.2026")
public class MapperUtilTippsNew {


    private static final Logger log = LoggerFactory.getLogger(MapperUtilTippsNew.class);
    private final ModelMapper modelMapper = new ModelMapper();

    public ModelMapper getModelMapperForTippModus() {
        log.info("getModelMapper");

// 1. Basis-TypeMap mit Provider
        TypeMap<TippModus, TippModusDto> baseMap =
                modelMapper.createTypeMap(TippModus.class, TippModusDto.class)
                        .setProvider(hierarchyProvider);
        log.info("baseMap created ");
// 2. Unterklassen-Mapping erbt von Basis
        modelMapper.createTypeMap(TippModusToto.class, TippModusTotoDto.class)
                .includeBase(TippModus.class, TippModusDto.class);
        modelMapper.createTypeMap(TippModusPoint.class, TippModusPointDto.class)
                .includeBase(TippModus.class, TippModusDto.class);
        modelMapper.createTypeMap(TippModusResult.class, TippModusResultDto.class)
                .includeBase(TippModus.class, TippModusDto.class);
        modelMapper.addMappings(new PropertyMap<TippModusPoint, TippModusPointDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);
                using(tippModusToString).map(source.getType()).setType(null);

            }
        });

        return this.modelMapper;
    }

    // Converter Definition
    Converter<TippModusType, String> tippModusToString = new AbstractConverter<TippModusType, String>() {
        protected String convert(TippModusType source) {
            log.info("Converting TippModusType to String {}", source.getDisplayName());
            return source == null ? null : source.getDisplayName();
        }
    };

    Provider<TippModusDto> hierarchyProvider = request -> {
        TippModus source = (TippModus) request.getSource();
        // Logik zur Auswahl der richtigen Unterklasse
        log.info("hierarchyProvider: {}", source.getType());
        return switch (source.getType()) {

            case TIPPMODUS_TOTO -> new TippModusTotoDto();
            case TIPPMODUS_RESULT -> new TippModusResultDto();
            case TIPPMODUS_POINT -> new TippModusPointDto();
            default -> throw new IllegalStateException("Unexpected value: " + source.getType());
        };

    };
}

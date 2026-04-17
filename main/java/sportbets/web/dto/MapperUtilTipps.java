package sportbets.web.dto;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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

@Component
public class MapperUtilTipps {
    private ModelMapper modelMapper;

    public ModelMapper modelMapperForResultTipp() {
        modelMapper = new ModelMapper();
        // Anwendung im ModelMapper
        modelMapper.typeMap(TippModusResult.class, TippModusResultDto.class).addMappings(mapper -> {
            mapper.using(tippModusToString).map(TippModusResult::getType, TippModusResultDto::setType);
        });
        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<TippModusResult, TippModusResultDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);

            }
        });

        return modelMapper;
    }

    public ModelMapper modelMapperForTotoTipp() {
        modelMapper = new ModelMapper();

        // Anwendung im ModelMapper
        modelMapper.typeMap(TippModusToto.class, TippModusTotoDto.class).addMappings(mapper -> {
            mapper.using(tippModusToString).map(TippModusToto::getType, TippModusTotoDto::setType);
        });
        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<TippModusToto, TippModusTotoDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);

            }
        });

        return modelMapper;
    }

    public ModelMapper modelMapperForPointTipp() {
        modelMapper = new ModelMapper();

        // Anwendung im ModelMapper
        modelMapper.typeMap(TippModusPoint.class, TippModusPointDto.class).addMappings(mapper -> {
            mapper.using(tippModusToString).map(TippModusPoint::getType, TippModusPointDto::setType);
        });
        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<TippModusPoint, TippModusPointDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);

            }
        });

        return modelMapper;
    }


    // Converter Definition
    Converter<TippModusType, String> tippModusToString = new AbstractConverter<TippModusType, String>() {
        protected String convert(TippModusType source) {
            return source == null ? null : source.getDisplayName();
        }
    };
}

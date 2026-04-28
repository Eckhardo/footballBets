package sportbets.web.dto;

import org.modelmapper.*;
import org.springframework.stereotype.Component;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

@Component
public class MapperUtilTipps {
    private final ModelMapper modelMapper = new ModelMapper();

    public  ModelMapper modelMapperForResultTipp() {
        // setup
        TypeMap<TippModusResult, TippModusResultDto> typeMap = modelMapper.createTypeMap(TippModusResult.class, TippModusResultDto.class);
        // add deep mapping to flatten source's Community object into a single field in destination
        typeMap
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getCommunity().getName(), TippModusResultDto::setCommName);
                            mapper.map(src -> src.getCommunity().getId(), TippModusResultDto::setCommId);
                            mapper.using(tippModusToString).map(TippModusResult::getType, TippModusResultDto::setType);
                        });
        return modelMapper;
    }


    public ModelMapper modelMapperForTotoTipp() {


        // Convert enum to string
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


        // Convert enum to string
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

package sportbets.web.dto;

import org.modelmapper.*;
import org.springframework.stereotype.Component;
import sportbets.persistence.entity.tipps.*;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.web.dto.tipps.*;

@Component
public class MapperUtilTipps {
    private final ModelMapper modelMapper = new ModelMapper();

    public ModelMapper modelMapperForResultTipp() {
        // setup
        TypeMap<TippModusResult, TippModusResultDto> typeMap = modelMapper.getTypeMap(TippModusResult.class, TippModusResultDto.class);
        if (typeMap == null) {// add deep mapping to flatten source
            modelMapper.addMappings(new PropertyMap<TippModusResult, TippModusResultDto>() {
                @Override
                protected void configure() {
                    // Map the 'id' of the 'parent' object in the source
                    // to the 'parentId' field in the destination
                    map(source.getCommunity().getName()).setCommName(null);
                    map(source.getCommunity().getId()).setCommId(null);
                    using(tippModusToString).map(source.getType()).setType(null);

                }
            });
        }
        return modelMapper;
    }


    public ModelMapper modelMapperForTotoTipp() {

        TypeMap<TippModusToto, TippModusTotoDto> typeMap = modelMapper.getTypeMap(TippModusToto.class, TippModusTotoDto.class);
        if (typeMap == null) {// add deep mapping to flatten source
            modelMapper.addMappings(new PropertyMap<TippModusToto, TippModusTotoDto>() {
                @Override
                protected void configure() {
                    // Map the 'id' of the 'parent' object in the source
                    // to the 'parentId' field in the destination
                    map(source.getCommunity().getName()).setCommName(null);
                    map(source.getCommunity().getId()).setCommId(null);
                 using(tippModusToString).map(source.getType()).setType(null);

                }
            });
        }
        return modelMapper;

    }

    public ModelMapper modelMapperForPointTipp() {

        TypeMap<TippModusPoint, TippModusPointDto> typeMap = modelMapper.getTypeMap(TippModusPoint.class, TippModusPointDto.class);
        if (typeMap == null) {// add deep mapping to flatten source
            modelMapper.addMappings(new PropertyMap<TippModusPoint, TippModusPointDto>() {
                @Override
                protected void configure() {
                    // Map the 'id' of the 'parent' object in the source
                    // to the 'parentId' field in the destination
                    map(source.getCommunity().getName()).setCommName(null);
                    map(source.getCommunity().getId()).setCommId(null);
                   using(tippModusToString).map(source.getType()).setType(null);

                }
            });
        }
        return modelMapper;
    }

    public ModelMapper modelMapperForTippConfig() {
        TypeMap<TippConfig, TippConfigDto> typeMap = modelMapper.getTypeMap(TippConfig.class, TippConfigDto.class);
        // add deep mapping to flatten source

        if (typeMap == null) {
            // Define the custom mapping for ChildEntity -> ChildDTO
            modelMapper.addMappings(new PropertyMap<TippConfig, TippConfigDto>() {
                @Override
                protected void configure() {
                    // Map the 'id' of the 'parent' object in the source
                    // to the 'parentId' field in the destination
                    map(source.getCompetitionMembership().getId()).setCompMembId(null);
                    map(source.getSpieltag().getId()).setSpieltagId(null);
                    map(source.getSpieltag().getSpieltagNumber()).setSpieltagNumber(1);
                    map(source.getTippModus().getId()).setTippModusId(null);

                }
            });
        }

        return modelMapper;
    }
    public ModelMapper modelMapperForTipp() {
        TypeMap<Tipp, TippDto> typeMap = modelMapper.getTypeMap(Tipp.class, TippDto.class);
        // add deep mapping to flatten source

        if (typeMap == null) {
            // Define the custom mapping for ChildEntity -> ChildDTO
            modelMapper.addMappings(new PropertyMap<Tipp, TippDto>() {
                @Override
                protected void configure() {
                    // Map the 'id' of the 'parent' object in the source
                    // to the 'parentId' field in the destination
                    map(source.getCommunityMembership().getId()).setCommMembId(null);
                    map(source.getSpiel().getId()).setSpielId(null);
                    map(source.getSpiel().getSpielNumber()).setSpielNumber(null);
                    map(source.getTippModus().getId()).setTippModusId(null);
                    using(tippModusToString).map(source.getTippModus().getType()).setTippModusType(null);
                }
            });
        }

        return modelMapper;
    }


    // Converter Definition
    Converter<TippModusType, String> tippModusToString = new AbstractConverter<TippModusType, String>() {
        protected String convert(TippModusType source) {
            return source == null ? null : source.getDisplayName();
        }
    };
}

package sportbets.web.dto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Spieltag;

public class MapperUtil {

    public static ModelMapper getModelMapperForFamily() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<Competition, CompetitionDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetitionFamily().getId()).setFamilyId(null);
            }
        });

        return modelMapper;
    }
    public static ModelMapper getModelMapperForCompetition() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<CompetitionRound, CompetitionRoundDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetition().getId()).setCompId(null);
            }
        });

        return modelMapper;
    }
    public static ModelMapper getModelMapperForCompetitionRound() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<Spieltag, SpieltagDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetitionRound().getId()).setCompRoundId(null);
            }
        });

        return modelMapper;
    }
}

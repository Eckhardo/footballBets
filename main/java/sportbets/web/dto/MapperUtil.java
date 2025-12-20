package sportbets.web.dto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import sportbets.persistence.entity.*;

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
                map(source.getCompetitionFamily().getName()).setFamilyName(null);
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
                map(source.getCompetition().getName()).setCompName(null);

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
                map(source.getCompetitionRound().getName()).setCompRoundName(null);
            }
        });

        return modelMapper;
    }
    public static ModelMapper getModelMapperForSpiel() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<Spiel, SpielDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getSpieltag().getId()).setSpieltagId(null);
                map(source.getSpieltag().getSpieltagNumber()).setSpieltagNumber(0);
                map(source.getHeimTeam().getId()).setHeimTeamId(null);
                map(source.getHeimTeam().getAcronym()).setHeimTeamAcronym(null);
                map(source.getGastTeam().getId()).setGastTeamId(null);
                map(source.getGastTeam().getAcronym()).setGastTeamAcronym(null);
            }
        });

        return modelMapper;
    }

    public static ModelMapper getModelMapperForCompTeam() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CompetitionTeam, CompetitionTeamDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetition().getId()).setCompId(null);
                map(source.getCompetition().getName()).setCompName(null);
                map(source.getTeam().getId()).setTeamId(null);
                map(source.getTeam().getAcronym()).setTeamAcronym(null);

            }
        });

        return modelMapper;
    }
}

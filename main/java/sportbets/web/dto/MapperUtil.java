package sportbets.web.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.*;
import sportbets.web.dto.authorization.CommunityRoleDto;
import sportbets.web.dto.authorization.CompetitionRoleDto;
import sportbets.web.dto.authorization.TipperRoleDto;
import sportbets.web.dto.community.CommunityMembershipDto;
import sportbets.web.dto.competition.*;

@Component
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
    public static ModelMapper getModelMapperForCompetitionMembership() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<CompetitionMembership, CompetitionMembershipDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetition().getId()).setCompId(null);
                map(source.getCompetition().getName()).setCompName(null);
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);
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
                map(source.getTeam().isHasClub()).setHasClub(null);

            }
        });

        return modelMapper;
    }

    public static ModelMapper getModelMapperForTipperRole() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<TipperRole, TipperRoleDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getRole().getId()).setRoleId(null);
                map(source.getRole().getName()).setRoleName(null);
                map(source.getTipper().getId()).setTipperId(null);
                map(source.getTipper().getUsername()).setTipperUserName(null);

            }
        });

        return modelMapper;
    }

    public static ModelMapper getModelMapperForCompetitionRole() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CompetitionRole, CompetitionRoleDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCompetition().getId()).setCompetitionId(null);
                map(source.getCompetition().getName()).setCompetitionName(null);


            }
        });

        return modelMapper;


    }



    public static ModelMapper getModelMapperForCommunityMembership() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the custom mapping for ChildEntity -> ChildDTO
        modelMapper.addMappings(new PropertyMap<CommunityMembership, CommunityMembershipDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getTipper().getId()).setTipperId(null);
                map(source.getTipper().getUsername()).setTipperName(null);
                map(source.getCommunity().getId()).setCommId(null);
                map(source.getCommunity().getName()).setCommName(null);
            }
        });

        return modelMapper;
    }

    public static ModelMapper getModelMapperForCommunityRole() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CommunityRole, CommunityRoleDto>() {
            @Override
            protected void configure() {
                // Map the 'id' of the 'parent' object in the source
                // to the 'parentId' field in the destination
                map(source.getCommunity().getId()).setCommunityId(null);
                map(source.getCommunity().getName()).setCommunityName(null);


            }
        });

        return modelMapper;
    }
}

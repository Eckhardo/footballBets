package sportbets.web.dto;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import sportbets.persistence.entity.Competition;

import java.util.Collection;
import java.util.stream.Collectors;

public class ConverterTest {
    private static final Converter<Collection<Competition>, Collection<CompetitionDto>> childConverter =
            (context) -> {
                Collection<Competition> source = context.getSource();
                return source.stream()
                        .map(child -> {
                            MappingContext<Competition, CompetitionDto> subContext = context.create(child, CompetitionDto.class);
                            return context.getMappingEngine().map(subContext);
                        }).collect(Collectors.toList());
            };
}

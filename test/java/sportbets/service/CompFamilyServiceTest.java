package sportbets.service;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.service.impl.CompFamilyServiceImpl;
import sportbets.web.dto.CompetitionFamilyDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CompFamilyServiceTest {

    @Mock
    private CompetitionFamilyRepository familyRepository;

    @InjectMocks
    private CompFamilyServiceImpl compFamilyService;

    private CompetitionFamily family;
    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        //familyRepository = Mockito.mock(CompFamilyRepository.class);
        //compFamilyService = new CompFamilyServiceImpl(familyRepository);
        family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);



    }

    // JUnit test for saveCompFamily method
    @DisplayName("JUnit test for saveCompFamily method")
    @Test
    public void givenCompFamilyObject_whenSaveCompFamily_thenReturnCompFamilyObject() {
        // given - precondition or setup
        ModelMapper mapper = new ModelMapper();
        CompetitionFamilyDto famDto = mapper.map(family, CompetitionFamilyDto.class);
        given(familyRepository.findById(family.getId())).willReturn(Optional.of(family));
        given(compFamilyService.getModelMapperForFamily().map(famDto,CompetitionFamily.class)).willReturn(family);
        given(compFamilyService.getModelMapperForFamily().map(family,CompetitionFamilyDto.class)).willReturn(famDto);
        given(compFamilyService.save(famDto)).willReturn(Optional.of(famDto));


        // when -  action or the behaviour that we are going test
        Optional<CompetitionFamilyDto> savedCompFamily = compFamilyService.save(famDto);

        System.out.println(savedCompFamily);
        // then - verify the output
        assertTrue(savedCompFamily.isPresent());
    }

    // JUnit test for saveCompFamily method
    @DisplayName("JUnit test for saveCompFamily method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveCompFamily_thenThrowsException() {
        // given - precondition or setup
        given(familyRepository.findByName(family.getName()))
                .willReturn(Optional.of(family));
        ModelMapper mapper = new ModelMapper();
        CompetitionFamilyDto famDto = mapper.map(family, CompetitionFamilyDto.class);


        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(EntityExistsException.class, () -> {
            compFamilyService.save(famDto);
        });

        // then
        verify(familyRepository, never()).save(any(CompetitionFamily.class));
    }

    // JUnit test for getAllCompFamilies method
    @DisplayName("JUnit test for getAllCompFamilies method")
    @Test
    public void givenCompFamiliesList_whenGetAllCompFamilies_thenReturnCompFamiliesList() {
        // given - precondition or setup

        CompetitionFamily employee1 = new CompetitionFamily("3. Bundesliga", "3. Deutsche Fussball Bundesliga", true, true);

        given(familyRepository.findAll()).willReturn(List.of(family, employee1));

        // when -  action or the behaviour that we are going test
        List<CompetitionFamilyDto> employeeList = compFamilyService.getAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for getAllCompFamilies method
    @DisplayName("JUnit test for getAllCompFamilies method (negative scenario)")
    @Test
    public void givenEmptyCompFamiliesList_whenGetAllCompFamilies_thenReturnEmptyCompFamiliesList() {
        // given - precondition or setup

        CompetitionFamily testFamily = new CompetitionFamily("1. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);


        given(familyRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<CompetitionFamilyDto> compFamilyList = compFamilyService.getAll();

        // then - verify the output
        assertThat(compFamilyList).isEmpty();

    }

    // JUnit test for getCompFamilyById method
    @DisplayName("JUnit test for getCompFamilyById method")
    @Test
    public void givenCompFamilyId_whenGetCompFamilyById_thenReturnCompFamilyObject() {
        // given
        given(familyRepository.findById(1L)).willReturn(Optional.of(family));

        // when
        Optional<CompetitionFamilyDto> savedCompFamily = compFamilyService.findById(family.getId());
        assertTrue(savedCompFamily.isPresent());

        // then


    }

    // JUnit test for updateCompFamily method
    @DisplayName("JUnit test for updateCompFamily method")
    @Test
    public void givenCompFamilyObject_whenUpdateCompFamily_thenReturnUpdatedCompFamily() {
        // given - precondition or setup

        family.setName("Premier League");
        family.setDescription("Description of PL");
        // when -  action or the behaviour that we are going test
        ModelMapper mapper = new ModelMapper();
        CompetitionFamilyDto famDto = mapper.map(family, CompetitionFamilyDto.class);
        Optional<CompetitionFamilyDto> updatedCompFamily = compFamilyService.updateFamily(family.getId(), famDto);
        // then - verify the output
        updatedCompFamily.ifPresent(compFamily -> {
            assertThat(compFamily.getName()).isEqualTo(family.getName());
            assertThat(compFamily.getDescription()).isEqualTo(family.getDescription());
        });


    }

    // JUnit test for deleteCompFamily method
    @DisplayName("JUnit test for deleteCompFamily method")
    @Test
    public void givenCompFamilyId_whenDeleteCompFamily_thenNothing() {
        // given - precondition or setup
        long compFamilyId = 1L;

        willDoNothing().given(familyRepository).deleteById(compFamilyId);

        // when -  action or the behaviour that we are going test
        compFamilyService.deleteById(compFamilyId);

        // then - verify the output
        verify(familyRepository, times(1)).deleteById(compFamilyId);
    }
}
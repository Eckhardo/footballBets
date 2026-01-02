package sportbets.service.competition;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.service.competition.impl.CompFamilyServiceImpl;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CompFamilyServiceTest {

    private static final String TEST_COMP_FAM = "TestLiga";
    private static final String TEST_COMP = "TestLiga: Saison 2025";
    @Mock
    private CompetitionFamilyRepository familyRepository;
    @Mock
    private ModelMapper modelMapper ;

    @InjectMocks
    private CompFamilyServiceImpl compFamilyService;
    final CompetitionFamily competitionFamily = new CompetitionFamily(TEST_COMP_FAM, "description of testliga", true, true);

    private CompetitionFamilyDto familyDto;
 
    @BeforeEach
    public void setup() {
        familyRepository = Mockito.mock(CompetitionFamilyRepository.class);

        compFamilyService = new CompFamilyServiceImpl(familyRepository,modelMapper);
        familyDto = new CompetitionFamilyDto(1L, "3. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);


    }


    // JUnit test for saveCompFamily method
    @DisplayName("JUnit test for saveCompFamily method which throws exception")
  //  @Test
    public void givenExistingEmail_whenSaveCompFamily_thenThrowsException() {
        // given - precondition or setup
        given(familyRepository.findByName(competitionFamily.getName()))
                .willReturn(Optional.of(competitionFamily));
        ModelMapper mapper = new ModelMapper();


        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(EntityExistsException.class, () -> {
            compFamilyService.save(familyDto);
        });

        // then
        verify(familyRepository, never()).save(any(CompetitionFamily.class));
    }

    // JUnit test for getAllCompFamilies method
    @DisplayName("JUnit test for getAllCompFamilies method")
    @Test
    public void givenCompFamiliesList_whenGetAllCompFamilies_thenReturnCompFamiliesList() {
        // given - precondition or setup

        CompetitionFamily family = new CompetitionFamily("3. Bundesliga", "3. Deutsche Fussball Bundesliga", true, true);

        given(familyRepository.findAll()).willReturn(List.of(this.competitionFamily, family));

        // when -  action or the behaviour that we are going test
        List<CompetitionFamily> famList = compFamilyService.getAll();

        // then - verify the output
        assertThat(famList).isNotNull();
        assertThat(famList.size()).isEqualTo(2);
    }

    // JUnit test for getAllCompFamilies method
    @DisplayName("JUnit test for getAllCompFamilies method (negative scenario)")
    @Test
    public void givenEmptyCompFamiliesList_whenGetAllCompFamilies_thenReturnEmptyCompFamiliesList() {
        // given - precondition or setup

        CompetitionFamily testFamily = new CompetitionFamily("1. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);


        given(familyRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<CompetitionFamily> compFamilyList = compFamilyService.getAll();

        // then - verify the output
        assertThat(compFamilyList).isEmpty();

    }

    // JUnit test for getCompFamilyById method
    @DisplayName("JUnit test for getCompFamilyById method")
    //  @Test
    public void givenCompFamilyId_whenGetCompFamilyById_thenReturnCompFamilyObject() {
        // given
        given(familyRepository.findById(1L)).willReturn(Optional.of(competitionFamily));

        // when
        Optional<CompetitionFamily> savedCompFamily = compFamilyService.findById(familyDto.getId());
        assertTrue(savedCompFamily.isPresent());

        // then


    }

    // JUnit test for updateCompFamily method
    @DisplayName("JUnit test for updateCompFamily method")
  //  @Test
    public void givenCompFamilyObject_whenUpdateCompFamily_thenReturnUpdatedCompFamily() {
        // given - precondition or setup
        familyDto.setId(1L);
        familyDto.setName("Premier League");
        familyDto.setDescription("Description of PL");
        given(compFamilyService.save(familyDto)).willReturn(Optional.of(competitionFamily));
        given(familyRepository.save(competitionFamily)).willReturn(competitionFamily);
        // when -  action or the behaviour that we are going test

        Optional<CompetitionFamily> updatedCompFamily = compFamilyService.updateFamily(familyDto.getId(), familyDto);
        // then - verify the output
        updatedCompFamily.ifPresent(compFamily -> {
            assertThat(compFamily.getName()).isEqualTo(familyDto.getName());
            assertThat(compFamily.getDescription()).isEqualTo(familyDto.getDescription());
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
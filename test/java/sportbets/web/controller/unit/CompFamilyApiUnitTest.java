package sportbets.web.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sportbets.FootballBetsApplication;

import sportbets.config.TestProfileDatabase;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.repository.CompetitionFamilyRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes  = {FootballBetsApplication.class, TestProfileDatabase.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CompFamilyApiUnitTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompetitionFamilyRepository compFamilyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
       compFamilyRepository.deleteAll();
    }

    @Test
    public void givenCompetitionFamilyObject_whenCreateCompetitionFamily_thenReturnSavedCompetitionFamily() throws Exception{

     CompetitionFamily   family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);
        family.setId(1L);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/families")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(family)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(family.getName())))
                .andExpect(jsonPath("$.description",
                        is(family.getDescription())))
                .andExpect(jsonPath("$.hasClubs",
                        is(family.isHasClubs())));

    }

    // JUnit test for Get All familys REST API
    @Test
    public void givenListOfCompetitionFamilys_whenGetAllCompetitionFamilys_thenReturnCompetitionFamilysList() throws Exception{
        // given - precondition or setup
        List<CompetitionFamily> listOfCompetitionFamilys = new ArrayList<>();
        listOfCompetitionFamilys.add(new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true));
        listOfCompetitionFamilys.add(new CompetitionFamily("3. Bundesliga", "3. Deutsche Fussball Bundesliga", true, true));
        compFamilyRepository.saveAll(listOfCompetitionFamilys);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/families"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfCompetitionFamilys.size())));

    }

    // positive scenario - valid family id
    // JUnit test for GET family by id REST API
    @Test
    public void givenCompetitionFamilyId_whenGetCompetitionFamilyById_thenReturnCompetitionFamilyObject() throws Exception{
        // given - precondition or setup
        CompetitionFamily   family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);
        family.setId(1L);

        compFamilyRepository.save(family);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/families/{id}", family.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(family.getName())))
                .andExpect(jsonPath("$.description", is(family.getDescription())))
                .andExpect(jsonPath("$.hasClubs", is(family.isHasClubs())));

    }

    // negative scenario - valid family id
    // JUnit test for GET family by id REST API
    @Test
    public void givenInvalidCompetitionFamilyId_whenGetCompetitionFamilyById_thenReturnEmpty() throws Exception{
        // given - precondition or setup

        CompetitionFamily   family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);
        family.setId(1L);

        compFamilyRepository.save(family);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/families/{id}", family.getId()));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update family REST API - positive scenario
    @Test
    public void givenUpdatedCompetitionFamily_whenUpdateCompetitionFamily_thenReturnUpdateCompetitionFamilyObject() throws Exception{
        // given - precondition or setup

        CompetitionFamily   family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);

        compFamilyRepository.save(family);

        CompetitionFamily  updatedCompetitionFamily = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussballliga", true, true);



        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/families/{id}", family.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCompetitionFamily)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedCompetitionFamily.getName())))
                .andExpect(jsonPath("$.description", is(updatedCompetitionFamily.getDescription())))
                .andExpect(jsonPath("$.hasClubs", is(updatedCompetitionFamily.isHasClubs())));
    }

    // JUnit test for update family REST API - negative scenario
    @Test
    public void givenUpdatedCompetitionFamily_whenUpdateCompetitionFamily_thenReturn404() throws Exception{
        // given - precondition or setup
        CompetitionFamily   family = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);

        compFamilyRepository.save(family);

        CompetitionFamily  updatedCompetitionFamily = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussballliga", true, true);


        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/families/{id}", family.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCompetitionFamily)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete family REST API
    @Test
    public void givenCompetitionFamilyId_whenDeleteCompetitionFamily_thenReturn200() throws Exception{
        // given - precondition or setup
        CompetitionFamily savedCompetitionFamily = new CompetitionFamily("2. Bundesliga", "2. Deutsche Fussball Bundesliga", true, true);

        compFamilyRepository.save(savedCompetitionFamily);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/families/{id}", savedCompetitionFamily.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
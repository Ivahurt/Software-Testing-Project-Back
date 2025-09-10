package fon.bg.ac.rs.istanisic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fon.bg.ac.rs.istanisic.service.PaymentService;
import fon.bg.ac.rs.istanisic.service.PersonResidenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.service.PersonService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonResidenceService personResidenceService;

    @MockBean
    private  PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<PersonDTO> personsDisplay;
    private PersonDTO personSave;

    @BeforeEach
    void setUp() {
        val person1 = new PersonDTO(
                1L, "Pera", "Peric",
                LocalDate.of(2000, 1, 1), 13453213L,
                "Zajecar", "Zajecar"
        );
        val person2 = new PersonDTO(
                2L, "Sava", "Savic",
                LocalDate.of(1990, 2, 2), 12223213L,
                "Beograd", "Beograd"
        );

        personSave = new PersonDTO(
                3L, "Sara", "Saric",
                LocalDate.of(2001, 10, 10), 12223777L,
                "Zajecar", "Beograd"
        );

        personsDisplay = List.of(person1, person2);
    }

    @Test
    @DisplayName("Get all persons test")
    void getAllTest() throws Exception {
        when(personService.getAll()).thenReturn(personsDisplay);

        val responseJson = mvc.perform(get("/person/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        val responseList = objectMapper.readValue(responseJson,
                new TypeReference<List<PersonDTO>>() {});

        assertThat(responseList).containsExactlyInAnyOrderElementsOf(personsDisplay);
    }

    @Test
    @DisplayName("Save person test")
    void savePersonTest() throws Exception {
        val jsonRequest = objectMapper.writeValueAsString(personSave);
        when(personService.savePerson(personSave)).thenReturn(personSave);

        val responseJson = mvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        val responsePerson = objectMapper.readValue(responseJson, PersonDTO.class);
        assertThat(responsePerson).isEqualTo(personSave);
    }

    @Test
    @DisplayName("Save person bad payload test")
    void savePersonBadPayloadTest() throws Exception {
        final String badJson = "Nije validan JSON";

        val responseJson = mvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        final Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap)
                .containsEntry("detail", "Nevalidan JSON u zahtevu")
                .containsEntry("status", "400")
                .containsEntry("title", "Bad Request");

        verify(personService, never()).savePerson(any(PersonDTO.class));
    }

    @Test
    @DisplayName("Delete person test")
    void deletePersonTest() throws Exception {
        doNothing().when(personService).deletePerson(1L);

        mvc.perform(delete("/person/{uniqueId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete non-existent person test")
    void deleteNonExistentPersonTest() throws Exception {
        doThrow(new EntityNotFoundException("Osoba sa zadatim ID-jem ne postoji"))
                .when(personService).deletePerson(100L);

        val responseJson = mvc.perform(delete("/person/{uniqueId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        final Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap)
                .containsEntry("message", "Osoba sa zadatim ID-jem ne postoji");
    }

    @Test
    @DisplayName("Update malformed person test")
    void updateMalformedPersonTest() throws Exception {
        val malformedPerson = new PersonUpdateDTO(null, null);
        val jsonRequest = objectMapper.writeValueAsString(malformedPerson);

        when(personService.updatePerson(malformedPerson))
                .thenThrow(new IllegalArgumentException("Osoba ne sme da sadrži nepopunjena ili nevalidna polja."));

        val responseJson = mvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        final Map<String, Object> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap).containsKey("message");
        assertThat(responseMap).containsKey("errors");

        Map<String, String> errors = (Map<String, String>) responseMap.get("errors");
        assertThat(errors)
                .containsEntry("cityResidenceName", "must not be null")
                .containsEntry("uniqueIdentificationNumber", "must not be null");
    }
}
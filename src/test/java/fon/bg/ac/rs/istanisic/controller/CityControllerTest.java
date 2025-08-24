package fon.bg.ac.rs.istanisic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.dto.CityUpdateDTO;
import fon.bg.ac.rs.istanisic.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CityController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CityDTO> citiesDisplay;
    private CityDTO citySave;

    @BeforeEach
    void setUp() {
        CityDTO city1 = new CityDTO(1L, 11000, "Beograd", 1500000);
        CityDTO city2 = new CityDTO(2L, 34000, "Nis", 260000);
        citySave = new CityDTO(3L, 21000, "Novi Sad", 400000);
        citiesDisplay = List.of(city1, city2);
    }

    @Test
    @DisplayName("Get all cities test")
    void getAllCitiesTest() throws Exception {
        when(cityService.getAll()).thenReturn(citiesDisplay);

        String responseJson = mvc.perform(get("/city/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<CityDTO> responseList = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseList).containsExactlyInAnyOrderElementsOf(citiesDisplay);
    }

    @Test
    @DisplayName("Save city test")
    void saveCityTest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(citySave);
        when(cityService.saveCity(citySave)).thenReturn(citySave);

        String responseJson = mvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CityDTO responseCity = objectMapper.readValue(responseJson, CityDTO.class);
        assertThat(responseCity).isEqualTo(citySave);
    }

    @Test
    @DisplayName("Save city bad payload test")
    void saveCityBadPayloadTest() throws Exception {
        String badJson = "Invalid JSON";

        String responseJson = mvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap)
                .containsEntry("status", "400")
                .containsEntry("title", "Bad Request");

        verify(cityService, never()).saveCity(any(CityDTO.class));
    }

    @Test
    @DisplayName("Delete city test")
    void deleteCityTest() throws Exception {
        doNothing().when(cityService).deleteCity(11000);

        mvc.perform(delete("/city/{postalCode}", 11000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete non-existent city test")
    void deleteNonExistentCityTest() throws Exception {
        doThrow(new Exception("Mesto ne postoji")).when(cityService).deleteCity(99999);

        String responseJson = mvc.perform(delete("/city/{postalCode}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap.get("message"))
                .isEqualToNormalizingUnicode("Došlo je do greške: Mesto ne postoji");
    }

    @Test
    @DisplayName("Update city test")
    void updateCityTest() throws Exception {
        CityUpdateDTO updateDTO = new CityUpdateDTO(11000, 2000000);
        CityDTO returnedDTO = new CityDTO(1L, 11000, "Beograd", 2000000);

        when(cityService.updateCity(updateDTO)).thenReturn(returnedDTO);

        String jsonRequest = objectMapper.writeValueAsString(updateDTO);
        String responseJson = mvc.perform(put("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CityDTO responseCity = objectMapper.readValue(responseJson, CityDTO.class);
        assertThat(responseCity).isEqualTo(returnedDTO);
    }

    @Test
    @DisplayName("Update malformed city test")
    void updateMalformedCityTest() throws Exception {
        CityUpdateDTO malformedDTO = new CityUpdateDTO(null, null);
        String jsonRequest = objectMapper.writeValueAsString(malformedDTO);

        when(cityService.updateCity(malformedDTO))
                .thenThrow(new IllegalArgumentException("Nevalidna polja u zahtevu"));

        String responseJson = mvc.perform(put("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> responseMap = objectMapper.readValue(responseJson, new TypeReference<>() {});
        assertThat(responseMap).containsKey("message");
    }
}
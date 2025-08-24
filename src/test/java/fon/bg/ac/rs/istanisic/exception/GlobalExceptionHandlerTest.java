package fon.bg.ac.rs.istanisic.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("EntityNotFoundException handler test")
    void handleEntityNotFoundTest() {
        EntityNotFoundException ex = new EntityNotFoundException("Mesto ne postoji");

        ResponseEntity<Map<String, String>> response = handler.handleEntityNotFound(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("message", "Mesto ne postoji");
    }

    @Test
    @DisplayName("HttpMessageNotReadableException handler test")
    void handleBadJsonTest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Nevalidan JSON");

        ResponseEntity<Map<String, String>> response = handler.handleBadJson(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody())
                .containsEntry("status", "400")
                .containsEntry("title", "Bad Request")
                .containsEntry("detail", "Nevalidan JSON u zahtevu");
    }

    @Test
    @DisplayName("IllegalArgumentException handler test")
    void handleIllegalArgumentTest() {
        IllegalArgumentException ex = new IllegalArgumentException("Pogrešan argument");

        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("message", "Pogrešan argument");
    }

    @Test
    @DisplayName("General Exception handler test")
    void handleGeneralExceptionTest() {
        Exception ex = new Exception("Neočekivana greška");

        ResponseEntity<Map<String, String>> response = handler.handleGeneral(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).containsEntry("message", "Došlo je do greške: Neočekivana greška");
    }

    @Test
    @DisplayName("MethodArgumentNotValidException handler test")
    void handleValidationExceptionsTest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                Collections.singletonList(new FieldError("obj", "fieldName", "Polje je obavezno"))
        );

        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        Map<String, Object> body = response.getBody();
        assertThat(body).containsEntry("message", "Došlo je do greške: Validation failed for argument");

        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertThat(errors).containsEntry("fieldName", "Polje je obavezno");
    }
}

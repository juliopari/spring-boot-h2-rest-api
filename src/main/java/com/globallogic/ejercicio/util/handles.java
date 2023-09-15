package com.globallogic.ejercicio.util;

import com.globallogic.ejercicio.dtos.ErrorMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class handles {

  public static ResponseEntity<?> handleMissingFields() {
    final Map<String, Object> response = new HashMap<>();
    response.put("errors", new ErrorMessage("El campo password y/o email es obligatorio"));
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  public static ResponseEntity<?> handleExistingUser() {
    final Map<String, Object> response = new HashMap<>();
    response.put("errors", new ErrorMessage("El correo ya existe"));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public static ResponseEntity<?> handleDatabaseError() {
    final Map<String, Object> response = new HashMap<>();
    response.put("errors", new ErrorMessage("Error al realizar el insert en la base de datos"));
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static ResponseEntity<?> handleLoginError(final Exception e) {
    Map<String, Object> response = new HashMap<>();
    response.put("errors", new ErrorMessage("Error al buscar el usuario"));
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static ResponseEntity<?> handleValidationErrors(final BindingResult result) {
    List<ErrorMessage> errors =
        result.getFieldErrors().stream()
            .map(
                err ->
                    new ErrorMessage(
                        " El campo: '" + err.getField() + "' " + err.getDefaultMessage()))
            .collect(Collectors.toList());
    Map<String, Object> response = new HashMap<>();
    response.put("data", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}

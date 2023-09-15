package com.globallogic.ejercicio.controller;

import static com.globallogic.ejercicio.util.AuthUtils.*;
import static com.globallogic.ejercicio.util.handles.*;

import com.globallogic.ejercicio.dtos.NewUserDTO;
import com.globallogic.ejercicio.model.User;
import com.globallogic.ejercicio.service.IUserService;
import com.globallogic.ejercicio.util.Token;
import com.nimbusds.jose.JOSEException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired private IUserService userService;

  @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
  public ResponseEntity<?> save(
      @Valid @RequestBody User request,
      final BindingResult result,
      HttpServletRequest requestServlet)
      throws JOSEException {
    try {
      if (result.hasErrors()) {
        return handleValidationErrors(result);
      }
      if (request.getPassword() == null || request.getEmail() == null) {
        return handleMissingFields();
      }
      final User user = userService.findByEmail(request.getEmail());

      if (user == null) {
        return createUser(request, requestServlet, userService);
      } else {
        return handleExistingUser();
      }
    } catch (DataAccessException e) {
      return handleDatabaseError();
    }
  }

  @GetMapping(value = "/login")
  public ResponseEntity<?> loginUser(HttpServletRequest request) {
    try {
      final User user = getUserByEmail(request, userService);
      updateUserTokenAndLastLogin(request, user, userService);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (Exception e) {
      return handleLoginError(e);
    }
  }
}

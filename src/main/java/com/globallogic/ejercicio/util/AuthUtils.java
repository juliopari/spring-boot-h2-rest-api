package com.globallogic.ejercicio.util;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import com.globallogic.ejercicio.service.IUserService;
import org.joda.time.DateTime;

import com.globallogic.ejercicio.model.User;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthUtils {

  private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
  private static final String TOKEN_SECRET = "abc";
  public static final String AUTH_HEADER = "X-Auth-Token";

  public static String createToken(
      final String host, final User sub, final HashMap<String, Object> customClaims)
      throws JOSEException {
    final JWTClaimsSet claim = new JWTClaimsSet();
    claim.setSubject(new Gson().toJson(sub));
    claim.setIssuer(host);
    claim.setIssueTime(DateTime.now().toDate());
    claim.setExpirationTime(DateTime.now().plusMinutes(1000000000).toDate());
    if (customClaims != null) {
      claim.setCustomClaims(customClaims);
    }
    final JWSSigner signer = new MACSigner(TOKEN_SECRET);
    SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);
    jwt.sign(signer);
    return jwt.serialize();
  }

  public static boolean validate(final ReadOnlyJWTClaimsSet cs)
      throws ParseException, JOSEException {
    if (cs.getExpirationTime().before(new Date())) {
      return false;
    } else {
      return true;
    }
  }

  public static ReadOnlyJWTClaimsSet decodeToken(final String authHeader)
      throws ParseException, JOSEException {
    SignedJWT signedJWT = SignedJWT.parse(authHeader);
    if (signedJWT.verify(new MACVerifier(TOKEN_SECRET))) {
      return signedJWT.getJWTClaimsSet();
    } else {
      throw new JOSEException("Error al verificar la firma");
    }
  }

  public static User getUser(final HttpServletRequest request) throws Exception {
    try {
      final ReadOnlyJWTClaimsSet cs = decodeToken(request.getHeader(AUTH_HEADER));
      if (validate(cs)) {
        return new Gson().fromJson(cs.getSubject(), User.class);
      } else {
        throw new LoginException();
      }
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } catch (JOSEException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static User getUserByEmail(
      final HttpServletRequest request, final IUserService userService) throws Exception {
    return userService.findByEmail(AuthUtils.getUser(request).getEmail());
  }

  public static void updateUserTokenAndLastLogin(
      final HttpServletRequest request, final User user, final IUserService userService)
      throws JOSEException {
    user.setToken(Token.createToken(request, user));
    user.setLastLogin(new Date());
    userService.save(user);
  }

  public static ResponseEntity<?> createUser(
      final User request, final HttpServletRequest requestServlet, final IUserService userService)
      throws JOSEException {

    final User user =
        new User(
            request.getName(),
            request.getPassword(),
            Token.createToken(requestServlet, new User(request.getEmail())),
            request.getEmail(),
            new Date(),
            null,
            new Date(),
            true,
            request.getPhones());

    final User newUser = userService.save(user);

    final Map<String, Object> response = new HashMap<>();
    response.put("id", newUser.getIdUser());
    response.put("created", newUser.getCreated());
    response.put("lastLogin", (newUser.getLastLogin() != null) ? newUser.getLastLogin() : "null");
    response.put("token", (newUser.getToken() != null) ? newUser.getToken() : "null");
    response.put("isActive", newUser.getIsactive());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}

package com.globallogic.ejercicio.dtos;

import com.globallogic.ejercicio.model.Phone;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

public class NewUserDTO {
  @Size(min = 3, message = "Debe tener minimo 3 caracteres")
  private String name;

  @Pattern(
      regexp = "^([a-zA-Z0-9_.+-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$",
      message = "El formato ingresado no es el correcto.")
  private String email;

  @Size(min = 8, message = "Debe tener minimo 8 caracteres")
  @Size(max = 12, message = "Debe tener maximo 12 caracteres")
  @Pattern(
      regexp = "^(?:[a-z]*[A-Z][a-z]+|[a-z]+[A-Z][a-z]*)(?=(.*[0-9]{2})).{4,}$",
      message =
          "El formato ingresado no es el correcto, 1 mayuscula, letras minisculas, 2 numeros.")
  private String password;

  private Set<Phone> phones;

  public NewUserDTO(
      final String name, final String email, final String password, final Set<Phone> phones) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phones = phones;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Set<Phone> getPhones() {
    return phones;
  }
}

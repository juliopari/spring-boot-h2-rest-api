package com.globallogic.ejercicio.repository;

import com.globallogic.ejercicio.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, UUID> {

  public User findByEmail(String email);
}

package com.globallogic.ejercicio.service;

import com.globallogic.ejercicio.model.User;

public interface IUserService {

  public User save(User user);

  public User findByEmail(String email);
}

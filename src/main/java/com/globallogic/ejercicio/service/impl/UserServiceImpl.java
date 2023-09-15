package com.globallogic.ejercicio.service.impl;

import com.globallogic.ejercicio.model.User;
import com.globallogic.ejercicio.repository.IUserRepository;
import com.globallogic.ejercicio.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

  @Autowired private IUserRepository userRepo;

  @Override
  public User save(User user) {
    return userRepo.save(user);
  }

  @Override
  public User findByEmail(String email) {
    return userRepo.findByEmail(email);
  }
}

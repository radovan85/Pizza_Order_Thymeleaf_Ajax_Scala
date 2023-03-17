package com.radovan.spring.service.impl

import com.radovan.spring.entity.UserEntity
import com.radovan.spring.exceptions.InvalidUserException
import com.radovan.spring.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsImpl extends UserDetailsService {

  @Autowired
  private var userService:UserService = _

  override def loadUserByUsername(name: String): UserEntity = {
    val user = userService.getUserByEmail(name)
    if (user == null) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    user
  }
}


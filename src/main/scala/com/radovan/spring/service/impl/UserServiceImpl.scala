package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.entity.{RoleEntity, UserEntity}
import com.radovan.spring.exceptions.{ExistingEmailException, InvalidUserException}
import com.radovan.spring.repository.{RoleRepository, UserRepository}
import com.radovan.spring.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class UserServiceImpl extends UserService {

  @Autowired
  private var userRepository:UserRepository = _

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var passwordEncoder:BCryptPasswordEncoder = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def updateUser(id: Integer, user: UserDto): UserDto = {
    val tempUser = Optional.ofNullable(userRepository.getById(id))
    var returnValue:UserDto = null
    if (tempUser.isPresent) {
      val userEntity = tempConverter.userDtoToEntity(user)
      userEntity.setEnabled(tempUser.get.getEnabled)
      userEntity.setRoles(tempUser.get.getRoles)
      userEntity.setId(tempUser.get.getId)
      val updatedUser = userRepository.saveAndFlush(userEntity)
      returnValue = tempConverter.userEntityToDto(updatedUser)
    }
    else {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def deleteUser(id: Integer): Unit = {
    userRepository.clearUserRoles(id)
    userRepository.deleteById(id)
    userRepository.flush()
  }

  override def getUserById(id: Integer): UserDto = {
    val userEntity = Optional.ofNullable(userRepository.getById(id))
    var returnValue:UserDto = null
    if (userEntity.isPresent) returnValue = tempConverter.userEntityToDto(userEntity.get)
    else {
      val error = new Error("Invalid User")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def listAllUsers: util.List[UserDto] = {
    val allUsers = Optional.ofNullable(userRepository.findAll)
    val returnValue = new util.ArrayList[UserDto]
    if (!allUsers.isEmpty) {
      for (userEntity <- allUsers.get.asScala) {
        val userDto = tempConverter.userEntityToDto(userEntity)
        returnValue.add(userDto)
      }
    }
    returnValue
  }

  override def getUserByEmail(email: String): UserEntity = {
    val userEntity = Optional.ofNullable(userRepository.findByEmail(email))
    var returnValue:UserEntity = null
    if (userEntity.isPresent) returnValue = userEntity.get
    else {
      val error = new Error("Invalid User")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def storeUser(user: UserDto): UserDto = {
    val testUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail))
    if (testUser.isPresent) {
      val error = new Error("Email exists")
      throw new ExistingEmailException(error)
    }
    val role = roleRepository.findByRole("ROLE_USER")
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.toByte)
    val roles = new util.ArrayList[RoleEntity]
    roles.add(role)
    val userEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles)
    userEntity.setEnabled(1.toByte)
    val storedUser = userRepository.save(userEntity)
    val users = new util.ArrayList[UserEntity]
    users.add(storedUser)
    role.setUsers(users)
    roleRepository.saveAndFlush(role)
    val returnValue = tempConverter.userEntityToDto(storedUser)
    returnValue
  }

  override def getCurrentUser: UserDto = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val userEntity = userRepository.getById(authUser.getId)
    val returnValue = tempConverter.userEntityToDto(userEntity)
    returnValue
  }

  override def suspendUser(userId: Integer): Unit = {
    val userEntity = userRepository.getById(userId)
    userEntity.setEnabled(0.toByte)
    userRepository.saveAndFlush(userEntity)
  }

  override def clearSuspension(userId: Integer): Unit = {
    val userEntity = userRepository.getById(userId)
    userEntity.setEnabled(1.toByte)
    userRepository.saveAndFlush(userEntity)
  }
}


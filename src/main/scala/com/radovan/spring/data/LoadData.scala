package com.radovan.spring.data

import com.radovan.spring.repository.RoleRepository
import com.radovan.spring.entity.RoleEntity
import com.radovan.spring.entity.UserEntity
import com.radovan.spring.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import java.util.Optional
import org.springframework.stereotype.Component
import java.util


@Component
class LoadData {

  private var roleRepository: RoleRepository = _
  private var userRepository: UserRepository = _
  private var passwordEncoder: BCryptPasswordEncoder = _

  @Autowired
  def this(roleRepository:RoleRepository,userRepository:UserRepository
           ,passwordEncoder:BCryptPasswordEncoder){

    this()
    this.roleRepository = roleRepository
    this.userRepository = userRepository
    this.passwordEncoder = passwordEncoder
    addRolesData ()
    addAdminData ()
  }

  def addRolesData (): Unit = {
    val role1: Optional[RoleEntity] = Optional.ofNullable(roleRepository.findByRole("ADMIN"))
    val role2: Optional[RoleEntity] = Optional.ofNullable(roleRepository.findByRole("ROLE_USER"))

    if(role1.isPresent){

    }else{
      roleRepository.save(new RoleEntity("ADMIN"))
    }

    if(role2.isPresent){

    }else{
      roleRepository.save(new RoleEntity("ROLE_USER"))
    }


  }


  def addAdminData(): Unit = {
    val role: Optional[RoleEntity] = Optional.ofNullable(roleRepository.findByRole("ADMIN"))
    if(role.isPresent){
      val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
      roles.add(role.get())
      val userEntity:Optional[UserEntity] =
        Optional.ofNullable(userRepository.findByEmail("doe@luv2code.com"))
      if(userEntity.isPresent){
        println("Admin already added")
      }else{
        val adminEntity:UserEntity = new UserEntity("John","Doe","doe@luv2code.com","admin123",1.toByte)
        val password = adminEntity.getPassword
        adminEntity.setPassword(passwordEncoder.encode(password))
        adminEntity.setRoles(roles)
        val storedAdmin = userRepository.save(adminEntity)
        val users = new util.ArrayList[UserEntity]
        users.add(storedAdmin)
        val roleAdmin:RoleEntity = role.get()
        roleAdmin.setUsers(users)
        roleRepository.saveAndFlush(roleAdmin)
      }
    }
  }



}

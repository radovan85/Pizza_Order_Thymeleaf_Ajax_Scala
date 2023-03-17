package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoleDto
import com.radovan.spring.repository.RoleRepository
import com.radovan.spring.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class RoleServiceImpl extends RoleService {

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def listAllAuthorities: util.List[RoleDto] = {
    val allRoles = Optional.ofNullable(roleRepository.findAll)
    val returnValue = new util.ArrayList[RoleDto]
    if (!allRoles.isEmpty) {
      for (role <- allRoles.get.asScala) {
        val roleDto = tempConverter.roleEntityToDto(role)
        returnValue.add(roleDto)
      }
    }
    returnValue
  }

  override def getRoleById(id: Integer): RoleDto = {
    val roleEntity = Optional.ofNullable(roleRepository.getById(id))
    var returnValue:RoleDto = null
    if (roleEntity.isPresent) returnValue = tempConverter.roleEntityToDto(roleEntity.get)
    returnValue
  }
}

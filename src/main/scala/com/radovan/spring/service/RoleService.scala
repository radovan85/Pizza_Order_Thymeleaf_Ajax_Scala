package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.RoleDto

trait RoleService {

  def listAllAuthorities: util.List[RoleDto]

  def getRoleById(id: Integer): RoleDto
}

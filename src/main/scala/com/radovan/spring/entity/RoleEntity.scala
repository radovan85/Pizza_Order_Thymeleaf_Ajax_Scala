package com.radovan.spring.entity

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, ManyToMany, Table, Transient}
import org.springframework.security.core.GrantedAuthority
import java.util

import scala.beans.BeanProperty

@Entity
@Table(name = "roles")
@SerialVersionUID(1L)
class RoleEntity extends GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(unique = true)
  @BeanProperty var role:String = _

  @Transient
  @ManyToMany(mappedBy = "roles")
  @BeanProperty var users:util.List[UserEntity] = _

  def this(role: String) {
    this()
    this.role = role
  }

  override def getAuthority: String = {
    getRole
  }
}

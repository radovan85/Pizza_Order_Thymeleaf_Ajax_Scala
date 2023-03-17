package com.radovan.spring.entity

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, JoinTable, ManyToMany, Table}
import org.hibernate.annotations.{Fetch, FetchMode}
import org.springframework.security.core.userdetails.UserDetails
import java.util

import org.springframework.security.core.GrantedAuthority

import scala.beans.BeanProperty

@Entity
@Table(name = "users")
@SerialVersionUID(1L)
class UserEntity extends UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false, name = "first_name")
  @BeanProperty var firstName:String = _

  @Column(nullable = false, name = "last_name")
  @BeanProperty var lastName:String = _

  @Column(nullable = false, unique = true)
  @BeanProperty var email:String = _

  @BeanProperty var password:String = _

  @BeanProperty var enabled:Byte = _

  @ManyToMany(fetch = FetchType.EAGER, cascade = Array(CascadeType.MERGE))
  @JoinTable(name = "users_roles", joinColumns = Array(new JoinColumn(name = "User_id")), inverseJoinColumns = Array(new JoinColumn(name = "roles_id")))
  @Fetch(FetchMode.SUBSELECT)
  @BeanProperty var roles:util.List[RoleEntity] = _

  def this(firstName: String, lastName: String, email: String, password: String, enabled: Byte) {
    this()
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.password = password
    this.enabled = enabled
  }

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = {
    roles
  }

  override def getUsername: String = email

  override def isAccountNonExpired = true

  override def isAccountNonLocked = true

  override def isCredentialsNonExpired = true

  override def isEnabled = true

  override def toString: String = "UserEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ", enabled=" + enabled + "]"
}

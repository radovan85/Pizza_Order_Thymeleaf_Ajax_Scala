package com.radovan.spring.entity

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "customers")
@SerialVersionUID(1L)
class CustomerEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var customerId:Integer = _

  @Column(name = "phone")
  @BeanProperty var customerPhone:String = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "shipping_address_id")
  @BeanProperty var shippingAddress:ShippingAddressEntity = _

  @OneToOne(cascade = Array(CascadeType.MERGE), fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @BeanProperty var user:UserEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _


}


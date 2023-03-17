package com.radovan.spring.entity

import java.lang.Float
import java.util

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}
import org.hibernate.annotations.{Fetch, FetchMode}

import scala.beans.BeanProperty

@Entity
@Table(name = "carts")
@SerialVersionUID(1L)
class CartEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var cartId:Integer = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @OneToMany(mappedBy = "cart", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var cartItems:util.List[CartItemEntity] = _

  @Column(name = "cart_price")
  @BeanProperty var cartPrice:Float = _


}


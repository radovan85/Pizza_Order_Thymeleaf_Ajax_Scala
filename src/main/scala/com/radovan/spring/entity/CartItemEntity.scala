package com.radovan.spring.entity

import java.lang.Float
import javax.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "cart_items")
@SerialVersionUID(1L)
class CartItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var cartItemId:Integer = _

  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Float = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "size_id")
  @BeanProperty var pizzaSize:PizzaSizeEntity = _

  @ManyToOne
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _


}


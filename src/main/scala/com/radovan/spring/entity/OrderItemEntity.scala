package com.radovan.spring.entity

import java.lang.Float

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "order_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var orderItemId:Integer = _

  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Float = _
  @BeanProperty var pizza:String = _

  @Column(name = "pizza_size")
  @BeanProperty  var pizzaSize:String = _

  @Column(name = "pizza_price")
  @BeanProperty var pizzaPrice:Float = _

  @ManyToOne
  @JoinColumn(name = "order_id")
  @BeanProperty var order:OrderEntity = _


}

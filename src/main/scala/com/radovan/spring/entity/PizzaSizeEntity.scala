package com.radovan.spring.entity

import java.lang.Float

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "pizza_sizes")
@SerialVersionUID(1L)
class PizzaSizeEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "pizza_size_id")
  @BeanProperty var pizzaSizeId:Integer = _

  @BeanProperty var name:String = _
  @BeanProperty var price:Float = _

  @ManyToOne
  @JoinColumn(name = "pizza_id")
  @BeanProperty var pizza:PizzaEntity = _


}


package com.radovan.spring.entity

import java.util
import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table, Transient}
import org.hibernate.annotations.{Fetch, FetchMode}

import scala.beans.BeanProperty

@Entity
@Table(name = "pizzas")
@SerialVersionUID(1L)
class PizzaEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "pizza_id")
  @BeanProperty var pizzaId:Integer = _

  @BeanProperty var name:String = _
  @BeanProperty var description:String = _

  @Transient
  @OneToMany(mappedBy = "pizza", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var pizzaSizes:util.List[PizzaSizeEntity] = _

  @Column(name = "image_name")
  @BeanProperty var imageName:String = _

  @Transient def getMainImagePath: String = {
    if (pizzaId == null || imageName == null) return "/images/pizzaImages/unknown.jpg"
    "/images/pizzaImages/" + this.imageName
  }


}


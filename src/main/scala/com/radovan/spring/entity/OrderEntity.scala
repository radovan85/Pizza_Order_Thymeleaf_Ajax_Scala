package com.radovan.spring.entity

import java.util
import java.lang.Float
import java.sql.Timestamp

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}
import org.hibernate.annotations.{CreationTimestamp, Fetch, FetchMode}

import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var customerOrderId:Integer = _

  @Column(name = "order_price")
  @BeanProperty var orderPrice:Float = _

  @OneToOne
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @CreationTimestamp
  @Column(name = "created_at")
  @BeanProperty var createdAt:Timestamp = _

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var orderedItems:util.List[OrderItemEntity] = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  @BeanProperty var address:OrderAddressEntity = _


}


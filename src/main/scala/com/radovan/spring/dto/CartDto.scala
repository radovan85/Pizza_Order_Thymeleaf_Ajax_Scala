package com.radovan.spring.dto

import java.lang.Float
import java.util

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartDto extends Serializable {

  @BeanProperty var cartId:Integer = _
  @BeanProperty var customerId:Integer = _
  @BeanProperty var cartItemsIds:util.List[Integer] = _
  @BeanProperty var cartPrice:Float = _

}


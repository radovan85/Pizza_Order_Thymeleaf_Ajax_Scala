package com.radovan.spring.dto

import java.lang.Float
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderItemDto extends Serializable {

  @BeanProperty var orderItemId:Integer = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Float = _
  @BeanProperty var pizza:String = _
  @BeanProperty var pizzaSize:String = _
  @BeanProperty var pizzaPrice:Float = _
  @BeanProperty var orderId:Integer = _


}


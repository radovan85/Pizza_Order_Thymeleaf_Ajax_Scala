package com.radovan.spring.dto

import java.lang.Float

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartItemDto extends Serializable {

  @BeanProperty var cartItemId:Integer = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Float = _
  @BeanProperty var pizzaSizeId:Integer = _
  @BeanProperty var cartId:Integer = _


}

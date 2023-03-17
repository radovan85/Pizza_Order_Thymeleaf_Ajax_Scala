package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CustomerDto extends Serializable {

  @BeanProperty var customerId:Integer = _
  @BeanProperty var customerPhone:String = _
  @BeanProperty var shippingAddressId:Integer = _
  @BeanProperty var userId:Integer = _
  @BeanProperty var cartId:Integer = _

}


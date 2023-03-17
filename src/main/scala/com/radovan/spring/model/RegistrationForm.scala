package com.radovan.spring.model

import com.radovan.spring.dto.{CustomerDto, ShippingAddressDto, UserDto}

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RegistrationForm extends Serializable {

  @BeanProperty var user:UserDto = _
  @BeanProperty var shippingAddress:ShippingAddressDto = _
  @BeanProperty var customer:CustomerDto = _


}


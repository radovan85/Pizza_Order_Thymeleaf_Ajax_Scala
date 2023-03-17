package com.radovan.spring.dto

import java.lang.Float
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PizzaSizeDto extends Serializable {

  @BeanProperty var pizzaSizeId:Integer = _
  @BeanProperty var name:String = _
  @BeanProperty var price:Float = _
  @BeanProperty var pizzaId:Integer = _

}


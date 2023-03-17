package com.radovan.spring.dto

import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PizzaDto extends Serializable {

  @BeanProperty var pizzaId:Integer = _
  @BeanProperty var name:String = _
  @BeanProperty var description:String = _
  @BeanProperty var pizzaSizesIds:util.List[Integer] = _
  @BeanProperty var imageName:String = _

  def getMainImagePath: String = {
    if (pizzaId == null || imageName == null) return "/images/pizzaImages/unknown.jpg"
    "/images/pizzaImages/" + this.imageName
  }

}


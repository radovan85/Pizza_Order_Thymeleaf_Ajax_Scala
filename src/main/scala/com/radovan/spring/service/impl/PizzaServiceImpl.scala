package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PizzaDto
import com.radovan.spring.repository.PizzaRepository
import com.radovan.spring.service.PizzaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class PizzaServiceImpl extends PizzaService {

  @Autowired
  private var pizzaRepository:PizzaRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def listAll: util.List[PizzaDto] = {
    val allPizzas = Optional.ofNullable(pizzaRepository.findAll)
    val returnValue = new util.ArrayList[PizzaDto]
    if (!allPizzas.isEmpty) {
      for (pizza <- allPizzas.get.asScala) {
        val pizzaDto = tempConverter.pizzaEntityToDto(pizza)
        returnValue.add(pizzaDto)
      }
    }
    returnValue
  }

  override def getPizzaById(pizzaId: Integer): PizzaDto = {
    var returnValue:PizzaDto = null
    val pizzaEntity = Optional.ofNullable(pizzaRepository.getById(pizzaId))
    if (pizzaEntity.isPresent) returnValue = tempConverter.pizzaEntityToDto(pizzaEntity.get)
    returnValue
  }

  override def deletePizza(pizzaId: Integer): Unit = {
    pizzaRepository.deleteById(pizzaId)
    pizzaRepository.flush()
  }

  override def addPizza(pizza: PizzaDto): PizzaDto = {
    val pizzaEntity = tempConverter.pizzaDtoToEntity(pizza)
    val storedPizza = pizzaRepository.save(pizzaEntity)
    val returnValue = tempConverter.pizzaEntityToDto(storedPizza)
    returnValue
  }

  override def listAllByKeyword(keyword: String): util.List[PizzaDto] = {
    val allPizzas = Optional.ofNullable(pizzaRepository.findAllByKeyword(keyword))
    val returnValue = new util.ArrayList[PizzaDto]
    if (!allPizzas.isEmpty) {
      for (pizza <- allPizzas.get.asScala) {
        val pizzaDto = tempConverter.pizzaEntityToDto(pizza)
        returnValue.add(pizzaDto)
      }
    }
    returnValue
  }
}


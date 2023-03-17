package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PizzaSizeDto
import com.radovan.spring.exceptions.ExistingSizeException
import com.radovan.spring.repository.PizzaSizeRepository
import com.radovan.spring.service.PizzaSizeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class PizzaSizeServiceImpl extends PizzaSizeService {

  @Autowired
  private var pizzaSizeRepository:PizzaSizeRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def addPizzaSize(pizzaSize: PizzaSizeDto): PizzaSizeDto = {
    val sizeId = Optional.ofNullable(pizzaSize.getPizzaSizeId)
    val sizeOpt = Optional.ofNullable(pizzaSizeRepository.findByNameAndPizzaId(pizzaSize.getName, pizzaSize.getPizzaId))
    if (sizeOpt.isPresent && !sizeId.isPresent) {
      val error = new Error("Size already exists")
      throw new ExistingSizeException(error)
    }
    val sizeEntity = tempConverter.pizzaSizeDtoToEntity(pizzaSize)
    val storedSize = pizzaSizeRepository.save(sizeEntity)
    val returnValue = tempConverter.pizzaSizeEntityToDto(storedSize)
    returnValue
  }

  override def getPizzaSizeById(pizzaSizeId: Integer): PizzaSizeDto = {
    var returnValue:PizzaSizeDto = null
    val sizeEntity = Optional.ofNullable(pizzaSizeRepository.getById(pizzaSizeId))
    if (sizeEntity.isPresent) returnValue = tempConverter.pizzaSizeEntityToDto(sizeEntity.get)
    returnValue
  }

  override def deletePizzaSize(pizzaSizeId: Integer): Unit = {
    pizzaSizeRepository.deleteById(pizzaSizeId)
    pizzaSizeRepository.flush()
  }

  override def listAll: util.List[PizzaSizeDto] = {
    val returnValue = new util.ArrayList[PizzaSizeDto]
    val allSizes = Optional.ofNullable(pizzaSizeRepository.findAll)
    if (!allSizes.isEmpty) {
      for (size <- allSizes.get.asScala) {
        val sizeDto = tempConverter.pizzaSizeEntityToDto(size)
        returnValue.add(sizeDto)
      }
    }
    returnValue
  }

  override def listAllByPizzaId(pizzaId: Integer): util.List[PizzaSizeDto] = {
    val returnValue = new util.ArrayList[PizzaSizeDto]
    val allSizes = Optional.ofNullable(pizzaSizeRepository.findAllByPizzaId(pizzaId))
    if (!allSizes.isEmpty) {
      for (size <- allSizes.get.asScala) {
        val sizeDto = tempConverter.pizzaSizeEntityToDto(size)
        returnValue.add(sizeDto)
      }
    }
    returnValue
  }

  override def deleteAllByPizzaId(pizzaId: Integer): Unit = {
    pizzaSizeRepository.deleteAllByPizzaId(pizzaId)
    pizzaSizeRepository.flush()
  }
}


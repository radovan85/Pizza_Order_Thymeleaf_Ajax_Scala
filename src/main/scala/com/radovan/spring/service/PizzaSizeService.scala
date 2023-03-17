package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.PizzaSizeDto

trait PizzaSizeService {

  def addPizzaSize(pizzaSize: PizzaSizeDto): PizzaSizeDto

  def getPizzaSizeById(pizzaSizeId: Integer): PizzaSizeDto

  def deletePizzaSize(pizzaSizeId: Integer): Unit

  def deleteAllByPizzaId(pizzaId: Integer): Unit

  def listAll: util.List[PizzaSizeDto]

  def listAllByPizzaId(pizzaId: Integer): util.List[PizzaSizeDto]
}

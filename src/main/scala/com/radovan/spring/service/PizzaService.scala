package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.PizzaDto

trait PizzaService {

  def listAll: util.List[PizzaDto]

  def getPizzaById(pizzaId: Integer): PizzaDto

  def deletePizza(pizzaId: Integer): Unit

  def addPizza(pizza: PizzaDto): PizzaDto

  def listAllByKeyword(keyword: String): util.List[PizzaDto]
}

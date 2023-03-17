package com.radovan.spring.controller

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.service.{PizzaService, PizzaSizeService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RequestParam}

@Controller
@RequestMapping(value = Array("/pizzas"))
class PizzaController {

  @Autowired
  private var pizzaService:PizzaService = _

  @Autowired
  private var pizzaSizeService:PizzaSizeService = _

  @RequestMapping(value = Array("/allPizzas"), method = Array(RequestMethod.GET))
  def listAllPizzas(map: ModelMap): String = {
    val allPizzas = pizzaService.listAll
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaListUser :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/pizzaDetails/{pizzaId}"), method = Array(RequestMethod.GET))
  def getPizzaDetails(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = pizzaService.getPizzaById(pizzaId)
    val pizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    map.put("pizza", pizza)
    map.put("pizzaSizes", pizzaSizes)
    "fragments/pizzaDetailsUser :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/orderPizza/{pizzaId}"), method = Array(RequestMethod.GET))
  def orderPizza(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val cartItem = new CartItemDto
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("cartItem", cartItem)
    "fragments/orderPizza :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/searchPizza"), method = Array(RequestMethod.GET))
  def searchPizza(@RequestParam("keyword") keyword: String, map: ModelMap): String = {
    val allPizzas = pizzaService.listAllByKeyword(keyword)
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/searchResult :: ajaxLoadedContent"
  }
}


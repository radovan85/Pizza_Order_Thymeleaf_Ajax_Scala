package com.radovan.spring.controller

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.exceptions.CartItemsNumberException
import com.radovan.spring.service.{CartItemService, CartService, CustomerService, PizzaService, PizzaSizeService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/carts"))
class CartController {

  @Autowired
  private var pizzaService:PizzaService = _

  @Autowired
  private var pizzaSizeService:PizzaSizeService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var cartService:CartService = _

  @RequestMapping(value = Array("/addToCart/{pizzaId}"), method = Array(RequestMethod.GET))
  def addToCart(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val cartItem = new CartItemDto
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("cartItem", cartItem)
    "fragments/addPizzaToCart :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/storeCartItem"), method = Array(RequestMethod.POST))
  def storeCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val pizzaSizeId = cartItem.getPizzaSizeId
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cartId = customer.getCartId
    cartItem.setCartId(cartId)
    val cartItems = cartItemService.listAllByCartId(cartId)
    var pizzaNumber:Integer = 0
    for (itemDto <- cartItems.asScala) {
      pizzaNumber = pizzaNumber + itemDto.getQuantity
    }
    pizzaNumber = pizzaNumber + cartItem.getQuantity
    if (pizzaNumber > 20.asInstanceOf[Integer]) {
      val error = new Error("Maximum 20 items allowed")
      throw new CartItemsNumberException(error)
    }
    for (itemDto <- cartItems.asScala) {
      if (itemDto.getPizzaSizeId eq pizzaSizeId) {
        val quantity = cartItem.getQuantity + itemDto.getQuantity
        cartItem.setQuantity(quantity)
        cartItem.setCartItemId(itemDto.getCartItemId)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cartId)
        return "fragments/homePage :: ajaxLoadedContent"
      }
    }
    cartItemService.addCartItem(cartItem)
    cartService.refreshCartState(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/itemAdded"), method = Array(RequestMethod.GET))
  def itemAddedToCart = "fragments/itemAdded :: ajaxLoadedContent"

  @RequestMapping(value = Array("/cart"), method = Array(RequestMethod.GET))
  def goToCart(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getCartId)
    val allPizzaSizes = pizzaSizeService.listAll
    val allPizzas = pizzaService.listAll
    map.put("allCartItems", allCartItems)
    map.put("allPizzas", allPizzas)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("cart", cart)
    "fragments/cart :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteItem/{cartId}/{itemId}"), method = Array(RequestMethod.GET))
  def deleteItem(@PathVariable("cartId") cartId: Integer, @PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(cartId, itemId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteAllItems/{cartId}"), method = Array(RequestMethod.GET))
  def clearCart(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/validateCart/{cartId}"), method = Array(RequestMethod.GET))
  def cartValidation(@PathVariable("cartId") cartId: Integer): String = {
    cartService.validateCart(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/cartError"), method = Array(RequestMethod.GET))
  def cartWarning = "fragments/invalidCart :: ajaxLoadedContent"

  @RequestMapping(value = Array("/itemsError"), method = Array(RequestMethod.GET))
  def itemNumberErr = "fragments/itemsNumberError :: ajaxLoadedContent"
}


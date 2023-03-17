package com.radovan.spring.controller

import com.radovan.spring.dto.{CustomerDto, OrderDto, ShippingAddressDto}
import com.radovan.spring.service.{CartItemService, CartService, CustomerService, OrderService, PizzaService, PizzaSizeService, ShippingAddressService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var shippingAddressService:ShippingAddressService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var pizzaService:PizzaService = _

  @Autowired
  private var pizzaSizeService:PizzaSizeService = _

  @Autowired
  private var orderService:OrderService = _

  @RequestMapping(value = Array("/confirmUserData"), method = Array(RequestMethod.GET))
  def confirmData(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val shippingAddress = new ShippingAddressDto
    val currentAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    map.put("shippingAddress", shippingAddress)
    map.put("currentAddress", currentAddress)
    "fragments/userDataConfirm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/confirmShippingAddress"), method = Array(RequestMethod.POST))
  def confirmShippingAddress(@ModelAttribute("shippingAddress") shippingAddress: ShippingAddressDto): String = {
    shippingAddressService.addShippingAddress(shippingAddress)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/phoneConfirmation"), method = Array(RequestMethod.GET))
  def confirmPhone(map: ModelMap): String = {
    val customer = new CustomerDto
    val authUser = userService.getCurrentUser
    val currentCustomer = customerService.getCustomerByUserId(authUser.getId)
    map.put("customer", customer)
    map.put("currentCustomer", currentCustomer)
    "fragments/phoneConfirmation :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/phoneConfirmation"), method = Array(RequestMethod.POST))
  def confirmPhonePost(@ModelAttribute("customer") customer: CustomerDto): String = {
    customerService.addCustomer(customer)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/orderConfirmation"), method = Array(RequestMethod.GET))
  def renderOrderForm(map: ModelMap): String = {
    val order = new OrderDto
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getCartId)
    val allPizzas = pizzaService.listAll
    val allPizzaSizes = pizzaSizeService.listAll
    val shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    map.put("order", order)
    map.put("allCartItems", allCartItems)
    map.put("allPizzas", allPizzas)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("shippingAddress", shippingAddress)
    map.put("cart", cart)
    "fragments/orderConfirmation :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/cancelOrder"), method = Array(RequestMethod.GET))
  def cancelOrder = "fragments/checkOutCancelled :: ajaxLoadedContent"

  @RequestMapping(value = Array("/createOrder"), method = Array(RequestMethod.GET))
  def addOrder(): String = {
    orderService.addCustomerOrder()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/orderCompleted"), method = Array(RequestMethod.GET))
  def orderExecuted = "fragments/orderCompleted :: ajaxLoadedContent"
}


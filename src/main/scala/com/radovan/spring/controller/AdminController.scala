package com.radovan.spring.controller

import java.nio.file.{Files, Paths}
import java.util.Optional

import com.radovan.spring.dto.{PizzaDto, PizzaSizeDto}
import com.radovan.spring.exceptions.ImagePathException
import com.radovan.spring.service.{CartItemService, CartService, CustomerService, OrderAddressService, OrderItemService, OrderService, PizzaService, PizzaSizeService, ShippingAddressService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod, RequestParam}
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  @Autowired
  private var pizzaService:PizzaService = _

  @Autowired
  private var pizzaSizeService:PizzaSizeService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var orderService:OrderService = _

  @Autowired
  private var orderAddressService:OrderAddressService = _

  @Autowired
  private var orderItemService:OrderItemService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var shippingAddressService:ShippingAddressService = _

  @RequestMapping(value = Array("/createPizza"), method = Array(RequestMethod.GET))
  def renderPizzaForm(map: ModelMap): String = {
    val pizza = new PizzaDto
    map.put("pizza", pizza)
    "fragments/pizzaForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allPizzas"), method = Array(RequestMethod.GET))
  def listAllPizzas(map: ModelMap): String = {
    val allPizzas = pizzaService.listAll
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidPath"), method = Array(RequestMethod.GET))
  def invalidImagePath = "fragments/invalidImagePath :: ajaxLoadedContent"

  @RequestMapping(value = Array("/createPizza"), method = Array(RequestMethod.POST))
  @throws[Throwable]
  def createBook(@ModelAttribute("pizza") pizza: PizzaDto, map: ModelMap, @RequestParam("pizzaImage") file: MultipartFile, @RequestParam("imgName") imgName: String): String = {
    val fileLocation = "C:\\Users\\Radovan\\IdeaProjects\\Pizza_Order_SpringMvc_Scala\\src\\main\\resources\\static\\images\\pizzaImages"
    var imageUUID:String = null
    val locationPath = Paths.get(fileLocation)
    if (!Files.exists(locationPath)) {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    imageUUID = file.getOriginalFilename
    val fileNameAndPath = Paths.get(fileLocation, imageUUID)
    if (file != null && !file.isEmpty) {
      Files.write(fileNameAndPath, file.getBytes)
      System.out.println("IMage Save at:" + fileNameAndPath.toString)
    }
    else imageUUID = imgName
    pizza.setImageName(imageUUID)
    pizzaService.addPizza(pizza)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/pizzaDetails/{pizzaId}"), method = Array(RequestMethod.GET))
  def getPizzaDetails(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("pizza", pizza)
    "fragments/pizzaDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/updatePizza/{pizzaId}"), method = Array(RequestMethod.GET))
  def renderUpdatePizzaForm(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = new PizzaDto
    val currentPizza = pizzaService.getPizzaById(pizzaId)
    map.put("pizza", pizza)
    map.put("currentPizza", currentPizza)
    "fragments/updatePizzaForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deletePizza/{pizzaId}"), method = Array(RequestMethod.GET))
  @throws[Throwable]
  def deletePizza(@PathVariable("pizzaId") pizzaId: Integer): String = {
    val pizza = pizzaService.getPizzaById(pizzaId)
    val allSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    for (pizzaSize <- allSizes.asScala) {
      cartItemService.eraseAllByPizzaSizeId(pizzaSize.getPizzaSizeId)
    }
    val path = Paths.get("C:\\Users\\Radovan\\IdeaProjects\\Pizza_Order_SpringMvc_Scala\\src\\main\\resources\\static\\images\\pizzaImages\\" + pizza.getImageName)
    if (Files.exists(path)) Files.delete(path)
    else {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    pizzaSizeService.deleteAllByPizzaId(pizzaId)
    pizzaService.deletePizza(pizzaId)
    val allCarts = cartService.listAll
    for (cart <- allCarts.asScala) {
      cartService.refreshCartState(cart.getCartId)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/sizeList/{pizzaId}"), method = Array(RequestMethod.GET))
  def getSizeList(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    "fragments/pizzaSizesForPizza :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allSizes"), method = Array(RequestMethod.GET))
  def getAllPizzaSizes(map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAll
    val allPizzas = pizzaService.listAll
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaSizeList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allSizes/{pizzaId}"), method = Array(RequestMethod.GET))
  def allSizesByPizzaId(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaSizeListByPizza :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createPizzaSize"), method = Array(RequestMethod.GET))
  def renderPizzaSizeForm(map: ModelMap): String = {
    val pizzaSize = new PizzaSizeDto
    val allPizzas = pizzaService.listAll
    map.put("pizzaSize", pizzaSize)
    map.put("allPizzas", allPizzas)
    "fragments/pizzaSizeForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createPizzaSize"), method = Array(RequestMethod.POST))
  def storePizzaSize(@ModelAttribute("pizzaSize") pizzaSize: PizzaSizeDto): String = {
    val pizzaSizeId = Optional.ofNullable(pizzaSize.getPizzaSizeId)
    pizzaSizeService.addPizzaSize(pizzaSize)
    if (pizzaSizeId.isPresent) {
      val allItems = cartItemService.listAllByPizzaSizeId(pizzaSizeId.get)
      for (item <- allItems.asScala) {
        item.setPrice(pizzaSize.getPrice * item.getQuantity)
        cartItemService.addCartItem(item)
      }
      val allCarts = cartService.listAll
      for (cart <- allCarts.asScala) {
        cartService.refreshCartState(cart.getCartId)
      }
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/updatePizzaSize/{pizzaSizeId}"), method = Array(RequestMethod.GET))
  def renderUpdatePizzaSizeForm(@PathVariable("pizzaSizeId") pizzaSizeId: Integer, map: ModelMap): String = {
    val pizzaSize = new PizzaSizeDto
    val currentPizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId)
    val allPizzas = pizzaService.listAll
    map.put("pizzaSize", pizzaSize)
    map.put("allPizzas", allPizzas)
    map.put("currentPizzaSize", currentPizzaSize)
    "fragments/updatePizzaSizeForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deletePizzaSize/{pizzaSizeId}"), method = Array(RequestMethod.GET))
  def deletePizzaSize(@PathVariable("pizzaSizeId") pizzaSizeId: Integer): String = {
    cartItemService.eraseAllByPizzaSizeId(pizzaSizeId)
    pizzaSizeService.deletePizzaSize(pizzaSizeId)
    val allCarts = cartService.listAll
    for (cart <- allCarts.asScala) {
      cartService.refreshCartState(cart.getCartId)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/pizzaSizeDetails/{pizzaSizeId}"), method = Array(RequestMethod.GET))
  def getPizzaSizeDetails(@PathVariable("pizzaSizeId") pizzaSizeId: Integer, map: ModelMap): String = {
    val pizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId)
    val pizza = pizzaService.getPizzaById(pizzaSize.getPizzaId)
    map.put("pizzaSize", pizzaSize)
    map.put("pizza", pizza)
    "fragments/pizzaSizeDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allCustomers"), method = Array(RequestMethod.GET))
  def getAllCustomers(map: ModelMap): String = {
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 8.asInstanceOf[Integer])
    "fragments/customerList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/customerDetails/{customerId}"), method = Array(RequestMethod.GET))
  def getCustomerDetails(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomer(customerId)
    val user = userService.getUserById(customer.getUserId)
    map.put("customer", customer)
    map.put("user", user)
    "fragments/customerDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/suspendUser/{userId}"), method = Array(RequestMethod.GET))
  def suspendUser(@PathVariable("userId") userId: Integer): String = {
    userService.suspendUser(userId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/clearSuspension/{userId}"), method = Array(RequestMethod.GET))
  def removeSuspension(@PathVariable("userId") userId: Integer): String = {
    userService.clearSuspension(userId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allOrders"), method = Array(RequestMethod.GET))
  def listAllOrders(map: ModelMap): String = {
    val allOrders = orderService.listAll
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 8.asInstanceOf[Integer])
    "fragments/orderList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteOrder/{orderId}"), method = Array(RequestMethod.GET))
  def removeOrder(@PathVariable("orderId") orderId: Integer): String = {
    orderService.deleteOrder(orderId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/orderDetails/{orderId}"), method = Array(RequestMethod.GET))
  def getOrderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrder(orderId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderedItems", orderedItems)
    "fragments/orderDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/existingSizeError"), method = Array(RequestMethod.GET))
  def sizeError = "fragments/pizzaSizeError :: ajaxLoadedContent"

  @RequestMapping(value = Array("/deleteCustomer/{customerId}"), method = Array(RequestMethod.GET))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    val customer = customerService.getCustomer(customerId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    val user = userService.getUserById(customer.getUserId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    for (order <- allOrders.asScala) {
      orderItemService.eraseAllByOrderId(order.getCustomerOrderId)
      orderService.deleteOrder(order.getCustomerOrderId)
    }
    cartItemService.eraseAllCartItems(cart.getCartId)
    customerService.resetCustomer(customerId)
    shippingAddressService.deleteShippingAddress(shippingAddress.getShippingAddressId)
    cartService.deleteCart(cart.getCartId)
    customerService.deleteCustomer(customerId)
    userService.deleteUser(user.getId)
    "fragments/homePage :: ajaxLoadedContent"
  }
}


package com.radovan.spring.converter

import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

import com.radovan.spring.dto.{CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, PizzaDto, PizzaSizeDto, RoleDto, ShippingAddressDto, UserDto}
import com.radovan.spring.entity.{CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PizzaEntity, PizzaSizeEntity, RoleEntity, ShippingAddressEntity, UserEntity}
import com.radovan.spring.repository.{CartItemRepository, CartRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, PizzaRepository, PizzaSizeRepository, RoleRepository, ShippingAddressRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._

class TempConverter {

  @Autowired
  private var mapper:ModelMapper = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var userRepository:UserRepository = _

  @Autowired
  private var shippingAddressRepository:ShippingAddressRepository = _

  @Autowired
  private var pizzaRepository:PizzaRepository = _

  @Autowired
  private var pizzaSizeRepository:PizzaSizeRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var orderItemRepository:OrderItemRepository = _

  @Autowired
  private var orderRepository:OrderRepository = _

  @Autowired
  private var orderAddressRepository:OrderAddressRepository = _

  def cartEntityToDto(cartEntity: CartEntity): CartDto = {
    val returnValue = mapper.map(cartEntity, classOf[CartDto])
    val cartPrice = Optional.ofNullable(cartEntity.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0f)
    val customerEntity = Optional.ofNullable(cartEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val itemsIds = new util.ArrayList[Integer]
    val cartItems = Optional.ofNullable(cartEntity.getCartItems)
    if (!cartItems.isEmpty) {
      for (itemEntity <- cartItems.get.asScala) {
        val itemId = itemEntity.getCartItemId
        itemsIds.add(itemId)
      }
    }
    returnValue.setCartItemsIds(itemsIds)
    returnValue
  }

  def cartDtoToEntity(cartDto: CartDto): CartEntity = {
    val returnValue = mapper.map(cartDto, classOf[CartEntity])
    val cartPrice = Optional.ofNullable(cartDto.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0f)
    val customerId = Optional.ofNullable(cartDto.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val cartItems = new util.ArrayList[CartItemEntity]
    val itemIds = Optional.ofNullable(cartDto.getCartItemsIds)
    if (!itemIds.isEmpty) {
      for (itemId <- itemIds.get.asScala) {
        val itemEntity = cartItemRepository.getById(itemId)
        cartItems.add(itemEntity)
      }
    }
    returnValue.setCartItems(cartItems)
    returnValue
  }

  def cartItemEntityToDto(cartItemEntity: CartItemEntity): CartItemDto = {
    val returnValue = mapper.map(cartItemEntity, classOf[CartItemDto])
    val pizzaSize = Optional.ofNullable(cartItemEntity.getPizzaSize)
    if (pizzaSize.isPresent) {
      var price = pizzaSize.get.getPrice
      val quantity = returnValue.getQuantity
      price = price * quantity
      returnValue.setPrice(price)
      returnValue.setPizzaSizeId(pizzaSize.get.getPizzaSizeId)
    }
    val cart = Optional.ofNullable(cartItemEntity.getCart)
    if (cart.isPresent) returnValue.setCartId(cart.get.getCartId)
    returnValue
  }

  def cartItemDtoToEntity(cartItemDto: CartItemDto): CartItemEntity = {
    val returnValue = mapper.map(cartItemDto, classOf[CartItemEntity])
    val cartId = Optional.ofNullable(cartItemDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val pizzaSizeId = Optional.ofNullable(cartItemDto.getPizzaSizeId)
    if (pizzaSizeId.isPresent) {
      val pizzaSizeEntity = pizzaSizeRepository.getById(pizzaSizeId.get)
      var price = pizzaSizeEntity.getPrice
      val quantity = returnValue.getQuantity
      price = price * quantity
      returnValue.setPrice(price)
      returnValue.setPizzaSize(pizzaSizeEntity)
    }
    returnValue
  }

  def customerEntityToDto(customerEntity: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customerEntity, classOf[CustomerDto])
    val shippingAddressEntity = Optional.ofNullable(customerEntity.getShippingAddress)
    if (shippingAddressEntity.isPresent) returnValue.setShippingAddressId(shippingAddressEntity.get.getShippingAddressId)
    val cartEntity = Optional.ofNullable(customerEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val userEntity = Optional.ofNullable(customerEntity.getUser)
    if (userEntity.isPresent) returnValue.setUserId(userEntity.get.getId)
    returnValue
  }

  def customerDtoToEntity(customerDto: CustomerDto): CustomerEntity = {
    val returnValue = mapper.map(customerDto, classOf[CustomerEntity])
    val shippingAddressId = Optional.ofNullable(customerDto.getShippingAddressId)
    if (shippingAddressId.isPresent) {
      val shippingAddressEntity = shippingAddressRepository.getById(shippingAddressId.get)
      returnValue.setShippingAddress(shippingAddressEntity)
    }
    val cartId = Optional.ofNullable(customerDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val userId = Optional.ofNullable(customerDto.getUserId)
    if (userId.isPresent) {
      val userEntity = userRepository.getById(userId.get)
      returnValue.setUser(userEntity)
    }
    returnValue
  }

  def pizzaEntityToDto(pizzaEntity: PizzaEntity): PizzaDto = {
    val returnValue = mapper.map(pizzaEntity, classOf[PizzaDto])
    val pizzaSizes = Optional.ofNullable(pizzaEntity.getPizzaSizes)
    val pizzaSizesIds = new util.ArrayList[Integer]
    if (!pizzaSizes.isEmpty) {
      for (size <- pizzaSizes.get.asScala) {
        pizzaSizesIds.add(size.getPizzaSizeId)
      }
    }
    returnValue.setPizzaSizesIds(pizzaSizesIds)
    returnValue
  }

  def pizzaDtoToEntity(pizzaDto: PizzaDto): PizzaEntity = {
    val returnValue = mapper.map(pizzaDto, classOf[PizzaEntity])
    val pizzaSizeIds = Optional.ofNullable(pizzaDto.getPizzaSizesIds)
    val pizzaSizes = new util.ArrayList[PizzaSizeEntity]
    if (!pizzaSizeIds.isEmpty) {
      for (sizeId <- pizzaSizeIds.get.asScala) {
        val pizzaSizeEntity = pizzaSizeRepository.getById(sizeId)
        pizzaSizes.add(pizzaSizeEntity)
      }
    }
    returnValue.setPizzaSizes(pizzaSizes)
    returnValue
  }

  def pizzaSizeEntityToDto(sizeEntity: PizzaSizeEntity): PizzaSizeDto = {
    val returnValue = mapper.map(sizeEntity, classOf[PizzaSizeDto])
    val pizzaEntity = Optional.ofNullable(sizeEntity.getPizza)
    if (pizzaEntity.isPresent) returnValue.setPizzaId(pizzaEntity.get.getPizzaId)
    returnValue
  }

  def pizzaSizeDtoToEntity(sizeDto: PizzaSizeDto): PizzaSizeEntity = {
    val returnValue = mapper.map(sizeDto, classOf[PizzaSizeEntity])
    val pizzaId = Optional.ofNullable(sizeDto.getPizzaId)
    if (pizzaId.isPresent) {
      val pizzaEntity = pizzaRepository.getById(pizzaId.get)
      returnValue.setPizza(pizzaEntity)
    }
    returnValue
  }

  def orderEntityToDto(orderEntity: OrderEntity): OrderDto = {
    val returnValue = mapper.map(orderEntity, classOf[OrderDto])
    val addressEntity = Optional.ofNullable(orderEntity.getAddress)
    if (addressEntity.isPresent) returnValue.setAddressId(addressEntity.get.getOrderAddressId)
    val customerEntity = Optional.ofNullable(orderEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val cartEntity = Optional.ofNullable(orderEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val orderedItemsIds = new util.ArrayList[Integer]
    val orderedItems = Optional.ofNullable(orderEntity.getOrderedItems)
    if (!orderedItems.isEmpty) {
      for (item <- orderedItems.get.asScala) {
        val itemId = item.getOrderItemId
        orderedItemsIds.add(itemId)
      }
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val createdAtLocal = createdAtOpt.get.toLocalDateTime
      val createdAtStr = createdAtLocal.format(formatter)
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue.setOrderedItemsIds(orderedItemsIds)
    returnValue
  }

  def orderDtoToEntity(orderDto: OrderDto): OrderEntity = {
    val returnValue = mapper.map(orderDto, classOf[OrderEntity])
    val addressId = Optional.ofNullable(orderDto.getAddressId)
    if (addressId.isPresent) {
      val address = orderAddressRepository.getById(addressId.get)
      returnValue.setAddress(address)
    }
    val customerId = Optional.ofNullable(orderDto.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val cartId = Optional.ofNullable(orderDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val orderedItems = new util.ArrayList[OrderItemEntity]
    val orderedItemsIds = Optional.ofNullable(orderDto.getOrderedItemsIds)
    if (!orderedItemsIds.isEmpty) {
      for (itemId <- orderedItemsIds.get.asScala) {
        val itemEntity = orderItemRepository.getById(itemId)
        orderedItems.add(itemEntity)
      }
    }
    returnValue.setOrderedItems(orderedItems)
    returnValue
  }

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderEntity = Optional.ofNullable(address.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getCustomerOrderId)
    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderId = Optional.ofNullable(address.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def orderItemEntityToDto(itemEntity: OrderItemEntity): OrderItemDto = {
    val returnValue = mapper.map(itemEntity, classOf[OrderItemDto])
    val orderEntity = Optional.ofNullable(itemEntity.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getCustomerOrderId)
    returnValue
  }

  def orderItemDtoToEntity(itemDto: OrderItemDto): OrderItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[OrderItemEntity])
    val orderId = Optional.ofNullable(itemDto.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def cartItemToOrderItemEntity(cartItemEntity: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItemEntity, classOf[OrderItemEntity])
    val pizzaSize = Optional.ofNullable(cartItemEntity.getPizzaSize)
    if (pizzaSize.isPresent) {
      val pizzaEntity = Optional.ofNullable(pizzaSize.get.getPizza)
      if (pizzaEntity.isPresent) {
        returnValue.setPizza(pizzaEntity.get.getName)
        returnValue.setPizzaSize(pizzaSize.get.getName)
        returnValue.setPizzaPrice(pizzaSize.get.getPrice)
        returnValue.setPrice(pizzaSize.get.getPrice * returnValue.getQuantity)
      }
    }
    returnValue
  }

  def shippingAddressEntityToDto(addressEntity: ShippingAddressEntity): ShippingAddressDto = {
    val returnValue = mapper.map(addressEntity, classOf[ShippingAddressDto])
    val customerEntity = Optional.ofNullable(addressEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def shippingAddressDtoToEntity(addressDto: ShippingAddressDto): ShippingAddressEntity = {
    val returnValue = mapper.map(addressDto, classOf[ShippingAddressEntity])
    val customerId = Optional.ofNullable(addressDto.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val roles = Optional.ofNullable(userEntity.getRoles)
    val rolesIds = new util.ArrayList[Integer]
    if (!roles.isEmpty) {
      for (roleEntity <- roles.get.asScala) {
        rolesIds.add(roleEntity.getId)
      }
    }
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue = mapper.map(userDto, classOf[UserEntity])
    val roles = new util.ArrayList[RoleEntity]
    val rolesIds = Optional.ofNullable(userDto.getRolesIds)
    if (!rolesIds.isEmpty) {
      for (roleId <- rolesIds.get.asScala) {
        val role = roleRepository.getById(roleId)
        roles.add(role)
      }
    }
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue = mapper.map(roleEntity, classOf[RoleDto])
    val users = Optional.ofNullable(roleEntity.getUsers)
    val userIds = new util.ArrayList[Integer]
    if (!users.isEmpty) {
      for (user <- users.get.asScala) {
        userIds.add(user.getId)
      }
    }
    returnValue.setUsersIds(userIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds = Optional.ofNullable(roleDto.getUsersIds)
    val users = new util.ArrayList[UserEntity]
    if (!usersIds.isEmpty) {
      for (userId <- usersIds.get.asScala) {
        val userEntity = userRepository.getById(userId)
        users.add(userEntity)
      }
    }
    returnValue.setUsers(users)
    returnValue
  }

  def shippingAddressToOrderAddress(shippingAddress: ShippingAddressEntity): OrderAddressEntity = {
    val returnValue = mapper.map(shippingAddress, classOf[OrderAddressEntity])
    returnValue
  }
}


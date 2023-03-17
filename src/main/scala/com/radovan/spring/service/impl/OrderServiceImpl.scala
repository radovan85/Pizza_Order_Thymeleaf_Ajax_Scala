package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import java.lang.Float

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entity.{OrderEntity, OrderItemEntity, UserEntity}
import com.radovan.spring.repository.{CartItemRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.service.{CartService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class OrderServiceImpl extends OrderService {

  @Autowired
  private var orderRepository:OrderRepository = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var orderItemRepository:OrderItemRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var orderAddressRepository:OrderAddressRepository = _

  override def addCustomerOrder(): OrderDto = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity = customerRepository.findByUserId(authUser.getId)
    val shippingAddress = customerEntity.getShippingAddress
    val cartEntity = customerEntity.getCart
    val orderEntity = new OrderEntity
    val orderedItems = new util.ArrayList[OrderItemEntity]
    val cartItems = Optional.ofNullable(cartEntity.getCartItems)
    if (!cartItems.isEmpty) {
      for (item <- cartItems.get.asScala) {
        val orderedItem = tempConverter.cartItemToOrderItemEntity(item)
        val storedOrderedItem = orderItemRepository.save(orderedItem)
        orderedItems.add(storedOrderedItem)
      }
    }
    val orderAddress = tempConverter.shippingAddressToOrderAddress(shippingAddress)
    val storedOrderAddress = orderAddressRepository.save(orderAddress)
    orderEntity.setCart(cartEntity)
    orderEntity.setCustomer(customerEntity)
    orderEntity.setOrderedItems(orderedItems)
    orderEntity.setAddress(storedOrderAddress)
    orderEntity.setOrderPrice(cartEntity.getCartPrice)
    val storedOrder = orderRepository.save(orderEntity)
    storedOrderAddress.setOrder(storedOrder)
    orderAddressRepository.saveAndFlush(storedOrderAddress)
    val returnValue = tempConverter.orderEntityToDto(storedOrder)
    for (item <- storedOrder.getOrderedItems.asScala) {
      item.setOrder(storedOrder)
      orderItemRepository.saveAndFlush(item)
    }
    cartItemRepository.removeAllByCartId(cartEntity.getCartId)
    cartService.refreshCartState(cartEntity.getCartId)
    returnValue
  }

  override def listAll: util.List[OrderDto] = {
    val allOrders = orderRepository.findAll
    val returnValue = new util.ArrayList[OrderDto]
    for (order <- allOrders.asScala) {
      val orderDto = tempConverter.orderEntityToDto(order)
      returnValue.add(orderDto)
    }
    returnValue
  }

  override def calculateOrderTotal(orderId: Integer): Float = {
    val orderTotal = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId))
    var returnValue = 0f
    if (orderTotal.isPresent) returnValue = orderTotal.get
    returnValue
  }

  override def getOrder(orderId: Integer): OrderDto = {
    val orderEntity = orderRepository.getById(orderId)
    val returnValue = tempConverter.orderEntityToDto(orderEntity)
    returnValue
  }

  override def deleteOrder(orderId: Integer): Unit = {
    orderRepository.deleteById(orderId)
    orderRepository.flush()
  }

  override def listAllByCustomerId(customerId: Integer): util.List[OrderDto] = {
    val returnValue = new util.ArrayList[OrderDto]
    val allOrders = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId))
    if (!allOrders.isEmpty) {
      for (order <- allOrders.get.asScala) {
        val orderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }
    }
    returnValue
  }
}


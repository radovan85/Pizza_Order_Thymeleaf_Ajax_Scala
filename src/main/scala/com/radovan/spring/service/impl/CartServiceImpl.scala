package com.radovan.spring.service.impl

import java.lang.Float
import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartDto
import com.radovan.spring.exceptions.InvalidCartException
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class CartServiceImpl extends CartService {

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  override def getCartByCartId(cartId: Integer): CartDto = {
    val cartEntity = Optional.ofNullable(cartRepository.getById(cartId))
    var returnValue = new CartDto
    if (cartEntity.isPresent) returnValue = tempConverter.cartEntityToDto(cartEntity.get)
    else {
      val error = new Error("Invalid cart")
      throw new InvalidCartException(error)
    }
    returnValue
  }

  override def refreshCartState(cartId: Integer): Unit = {
    val cartEntity = cartRepository.getById(cartId)
    val price = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId))
    if (price.isPresent) cartEntity.setCartPrice(price.get)
    else cartEntity.setCartPrice(0f)
    cartRepository.saveAndFlush(cartEntity)
  }

  override def calculateGrandTotal(cartId: Integer): Float = {
    val grandTotal = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId))
    var returnValue = 0f
    if (grandTotal.isPresent) returnValue = grandTotal.get
    returnValue
  }

  override def validateCart(cartId: Integer): CartDto = {
    val cartEntity = Optional.ofNullable(cartRepository.getById(cartId))
    var returnValue:CartDto = null
    val error = new Error("Invalid Cart")
    if (cartEntity.isPresent) {
      if (cartEntity.get.getCartItems.size == 0) throw new InvalidCartException(error)
      returnValue = tempConverter.cartEntityToDto(cartEntity.get)
    }
    else throw new InvalidCartException(error)
    returnValue
  }

  override def listAll: util.List[CartDto] = {
    val returnValue = new util.ArrayList[CartDto]
    val allCarts = Optional.ofNullable(cartRepository.findAll)
    if (!allCarts.isEmpty) {
      for (cart <- allCarts.get.asScala) {
        val cartDto = tempConverter.cartEntityToDto(cart)
        returnValue.add(cartDto)
      }
    }
    returnValue
  }

  override def deleteCart(cartId: Integer): Unit = {
    cartRepository.deleteById(cartId)
    cartRepository.flush()
  }
}


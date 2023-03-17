package com.radovan.spring.service

import java.util
import java.lang.Float

import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartByCartId(cartId: Integer): CartDto

  def refreshCartState(cartId: Integer): Unit

  def calculateGrandTotal(cartId: Integer): Float

  def validateCart(cartId: Integer): CartDto

  def listAll: util.List[CartDto]

  def deleteCart(cartId: Integer): Unit
}

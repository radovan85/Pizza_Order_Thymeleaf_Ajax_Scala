package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.OrderItemDto

trait OrderItemService {

  def listAllByOrderId(orderId: Integer): util.List[OrderItemDto]

  def eraseAllByOrderId(orderId: Integer): Unit
}

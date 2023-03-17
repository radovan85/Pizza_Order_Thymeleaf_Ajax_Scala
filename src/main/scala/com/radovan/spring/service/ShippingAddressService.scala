package com.radovan.spring.service

import com.radovan.spring.dto.ShippingAddressDto

trait ShippingAddressService {

  def addShippingAddress(shippingAddress: ShippingAddressDto): ShippingAddressDto

  def updateShippingAddress(id: Integer, shippingAddress: ShippingAddressDto): ShippingAddressDto

  def getShippingAddress(addressId: Integer): ShippingAddressDto

  def deleteShippingAddress(addressId: Integer): Unit
}

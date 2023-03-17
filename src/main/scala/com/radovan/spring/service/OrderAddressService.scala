package com.radovan.spring.service

import com.radovan.spring.dto.OrderAddressDto

trait OrderAddressService {

  def getAddressById(addressId: Integer): OrderAddressDto

  def deleteAddress(addressId: Integer): Unit
}


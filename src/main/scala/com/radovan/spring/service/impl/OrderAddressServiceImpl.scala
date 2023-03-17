package com.radovan.spring.service.impl

import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderAddressDto
import com.radovan.spring.repository.OrderAddressRepository
import com.radovan.spring.service.OrderAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderAddressServiceImpl extends OrderAddressService {

  @Autowired
  private var addressRepository:OrderAddressRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def getAddressById(addressId: Integer): OrderAddressDto = {
    var returnValue:OrderAddressDto = null
    val addressEntity = Optional.ofNullable(addressRepository.getById(addressId))
    if (addressEntity.isPresent) returnValue = tempConverter.orderAddressEntityToDto(addressEntity.get)
    returnValue
  }

  override def deleteAddress(addressId: Integer): Unit = {
    addressRepository.deleteById(addressId)
    addressRepository.flush()
  }
}


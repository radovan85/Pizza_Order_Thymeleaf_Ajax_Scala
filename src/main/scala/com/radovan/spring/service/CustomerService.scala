package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.model.RegistrationForm

trait CustomerService {

  def storeCustomer(form: RegistrationForm): CustomerDto

  def getAllCustomers: util.List[CustomerDto]

  def getCustomer(id: Integer): CustomerDto

  def getCustomerByUserId(userId: Integer): CustomerDto

  def getCustomerByCartId(cartId: Integer): CustomerDto

  def updateCustomer(customerId: Integer, customer: CustomerDto): CustomerDto

  def addCustomer(customer: CustomerDto): CustomerDto

  def resetCustomer(customerId: Integer): Unit

  def deleteCustomer(customerId: Integer): Unit
}


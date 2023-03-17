package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.entity.{CartEntity, RoleEntity, UserEntity}
import com.radovan.spring.exceptions.ExistingEmailException
import com.radovan.spring.model.RegistrationForm
import com.radovan.spring.repository.{CartRepository, CustomerRepository, RoleRepository, ShippingAddressRepository, UserRepository}
import com.radovan.spring.service.CustomerService
import javax.management.RuntimeErrorException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class CustomerServiceImpl extends CustomerService {

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var shippAddressRepository:ShippingAddressRepository = _

  @Autowired
  private var userRepository:UserRepository = _

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var passwordEncoder:BCryptPasswordEncoder = _

  override def getAllCustomers: util.List[CustomerDto] = {
    val allCustomers = Optional.ofNullable(customerRepository.findAll)
    val returnValue = new util.ArrayList[CustomerDto]
    if (!allCustomers.isEmpty) {
      for (customer <- allCustomers.get.asScala) {
        val customerDto = tempConverter.customerEntityToDto(customer)
        returnValue.add(customerDto)
      }
    }
    returnValue
  }

  override def getCustomer(id: Integer): CustomerDto = {
    val customerEntity = Optional.ofNullable(customerRepository.getById(id))
    var returnValue = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def getCustomerByUserId(userId: Integer): CustomerDto = {
    val customerEntity = Optional.ofNullable(customerRepository.findByUserId(userId))
    var returnValue = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def getCustomerByCartId(cartId: Integer): CustomerDto = {
    val customerEntity = Optional.ofNullable(customerRepository.findByCartId(cartId))
    var returnValue = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def updateCustomer(customerId: Integer, customer: CustomerDto): CustomerDto = {
    val tempCustomer = Optional.ofNullable(customerRepository.getById(customerId))
    var returnValue:CustomerDto = null
    if (tempCustomer.isPresent) {
      val customerEntity = tempConverter.customerDtoToEntity(customer)
      customerEntity.setCustomerId(tempCustomer.get.getCustomerId)
      customerEntity.setCart(tempCustomer.get.getCart)
      customerEntity.setShippingAddress(tempCustomer.get.getShippingAddress)
      customerEntity.setUser(tempCustomer.get.getUser)
      val updatedCustomer = customerRepository.saveAndFlush(customerEntity)
      returnValue = tempConverter.customerEntityToDto(updatedCustomer)
    }
    returnValue
  }

  override def storeCustomer(form: RegistrationForm): CustomerDto = {
    val userDto = form.getUser
    val testUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail))
    if (testUser.isPresent) {
      val error = new Error("Email exists")
      throw new ExistingEmailException(error)
    }
    val role = roleRepository.findByRole("ROLE_USER")
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword))
    userDto.setEnabled(1.toByte)
    val roles = new util.ArrayList[RoleEntity]
    roles.add(role)
    val userEntity = tempConverter.userDtoToEntity(userDto)
    userEntity.setRoles(roles)
    userEntity.setEnabled(1.toByte)
    val storedUser = userRepository.save(userEntity)
    val users = new util.ArrayList[UserEntity]
    users.add(storedUser)
    role.setUsers(users)
    roleRepository.saveAndFlush(role)
    val shippAddressDto = form.getShippingAddress
    val shippAddressEntity = tempConverter.shippingAddressDtoToEntity(shippAddressDto)
    val storedShippAddress = shippAddressRepository.save(shippAddressEntity)
    val cartEntity = new CartEntity
    val storedCart = cartRepository.save(cartEntity)
    val customerDto = form.getCustomer
    customerDto.setUserId(storedUser.getId)
    customerDto.setCartId(storedCart.getCartId)
    customerDto.setShippingAddressId(storedShippAddress.getShippingAddressId)
    val customerEntity = tempConverter.customerDtoToEntity(customerDto)
    val storedCustomer = customerRepository.save(customerEntity)
    storedCart.setCustomer(storedCustomer)
    cartRepository.saveAndFlush(storedCart)
    storedShippAddress.setCustomer(storedCustomer)
    shippAddressRepository.saveAndFlush(storedShippAddress)
    val returnValue = tempConverter.customerEntityToDto(storedCustomer)
    returnValue
  }

  override def addCustomer(customer: CustomerDto): CustomerDto = {
    val customerEntity = tempConverter.customerDtoToEntity(customer)
    val storedCustomer = customerRepository.save(customerEntity)
    val returnValue = tempConverter.customerEntityToDto(storedCustomer)
    returnValue
  }

  override def resetCustomer(customerId: Integer): Unit = {
    val customerOptional = Optional.ofNullable(customerRepository.getById(customerId))
    if (customerOptional.isPresent) {
      val cartOptional = Optional.ofNullable(customerOptional.get.getCart)
      if (cartOptional.isPresent) {
        val cartEntity = cartOptional.get
        cartEntity.setCustomer(null)
        cartRepository.saveAndFlush(cartEntity)
        val customerEntity = customerOptional.get
        customerEntity.setCart(null)
        customerEntity.setShippingAddress(null)
        customerEntity.setUser(null)
        customerRepository.saveAndFlush(customerEntity)
      }
    }
  }

  override def deleteCustomer(customerId: Integer): Unit = {
    customerRepository.deleteById(customerId)
    customerRepository.flush()
  }
}


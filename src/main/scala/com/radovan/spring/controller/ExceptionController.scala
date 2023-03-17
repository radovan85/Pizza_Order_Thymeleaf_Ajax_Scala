package com.radovan.spring.controller

import com.radovan.spring.exceptions.{CartItemsNumberException, ExistingEmailException, ExistingSizeException, ImagePathException, InvalidCartException, InvalidUserException, SuspendedUserException}
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseStatus}

@ControllerAdvice
class ExceptionController {

  @ResponseStatus
  @ExceptionHandler(Array(classOf[ExistingEmailException]))
  def handleExistingEmailException(ex: ExistingEmailException): ResponseEntity[_] = ResponseEntity.internalServerError.body("Email exists already!")

  @ResponseStatus
  @ExceptionHandler(Array(classOf[InvalidUserException]))
  def handleInvalidUserException(ex: InvalidUserException): ResponseEntity[_] = ResponseEntity.internalServerError.body("Invalid user!")

  @ResponseStatus
  @ExceptionHandler(Array(classOf[ImagePathException]))
  def handleImagePathException(ex: ImagePathException): ResponseEntity[_] = {
    System.out.println("Error image fires up!")
    ResponseEntity.internalServerError.body("Invalid image path")
  }

  @ResponseStatus
  @ExceptionHandler(Array(classOf[InvalidCartException]))
  def handleInvalidCartException(ex: InvalidCartException): ResponseEntity[_] = ResponseEntity.internalServerError.body("Invalid cart")

  @ResponseStatus
  @ExceptionHandler(Array(classOf[ExistingSizeException]))
  def handleExistingSizeException(ex: ExistingSizeException): ResponseEntity[_] = ResponseEntity.internalServerError.body("Existing size")

  @ResponseStatus
  @ExceptionHandler(Array(classOf[CartItemsNumberException]))
  def handleCartItemsNumberException(ex: CartItemsNumberException): ResponseEntity[_] = ResponseEntity.internalServerError.body("Maximum 20 items allowed")

  @ResponseStatus
  @ExceptionHandler(Array(classOf[SuspendedUserException])) def handleSuspendedUserException(ex: SuspendedUserException): ResponseEntity[_] = {
    SecurityContextHolder.clearContext()
    ResponseEntity.internalServerError.body("Account Suspended!")
  }
}


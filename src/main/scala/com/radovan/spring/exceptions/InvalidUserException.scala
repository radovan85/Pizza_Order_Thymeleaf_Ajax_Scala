package com.radovan.spring.exceptions

import javax.management.RuntimeErrorException

@SerialVersionUID(1L)
class InvalidUserException(val e: Error) extends RuntimeErrorException(e) {

}


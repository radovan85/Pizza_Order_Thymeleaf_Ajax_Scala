package com.radovan.spring.exceptions

import javax.management.RuntimeErrorException

@SerialVersionUID(1L)
class ExistingEmailException(val e: Error) extends RuntimeErrorException(e) {

}

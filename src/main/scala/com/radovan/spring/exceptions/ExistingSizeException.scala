package com.radovan.spring.exceptions

import javax.management.RuntimeErrorException

@SerialVersionUID(1L)
class ExistingSizeException(val e: Error) extends RuntimeErrorException(e) {

}


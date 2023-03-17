package com.radovan.spring.repository

import com.radovan.spring.entity.OrderAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait OrderAddressRepository extends JpaRepository[OrderAddressEntity, Integer] {

}

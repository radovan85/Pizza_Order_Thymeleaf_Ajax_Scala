package com.radovan.spring.repository

import com.radovan.spring.entity.CartEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CartRepository extends JpaRepository[CartEntity, Integer] {

  @Query(value = "select * from carts where customer_id = :customerId", nativeQuery = true)
  def findByCustomerId(@Param("customerId") customerId: Integer): CartEntity
}

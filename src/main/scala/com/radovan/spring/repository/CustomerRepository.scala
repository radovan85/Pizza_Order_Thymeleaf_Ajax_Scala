package com.radovan.spring.repository

import com.radovan.spring.entity.CustomerEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CustomerRepository extends JpaRepository[CustomerEntity, Integer] {

  def findByUserId(@Param("userId") userId: Integer): CustomerEntity

  @Query(value = "select * from customers where cart_id = :cartId", nativeQuery = true)
  def findByCartId(@Param("cartId") cartId: Integer): CustomerEntity
}

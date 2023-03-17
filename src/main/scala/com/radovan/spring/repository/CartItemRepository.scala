package com.radovan.spring.repository

import java.lang.Float
import java.util

import com.radovan.spring.entity.CartItemEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CartItemRepository extends JpaRepository[CartItemEntity, Integer] {

  @Query(value = "select * from cart_items where cart_id = :cartId", nativeQuery = true)
  def findAllByCartId(@Param("cartId") cartId: Integer): util.List[CartItemEntity]

  @Query(value = "select * from cart_items where size_id = :pizzaSizeId", nativeQuery = true)
  def findAllByPizzaSizeId(@Param("pizzaSizeId") pizzaSizeId: Integer): util.List[CartItemEntity]

  @Modifying
  @Query(value = "delete from cart_items where cart_id = :cartId", nativeQuery = true)
  def removeAllByCartId(@Param("cartId") cartId: Integer): Unit

  @Modifying
  @Query(value = "delete from cart_items where cart_id = :cartId and id = :itemId", nativeQuery = true)
  def removeCartItem(@Param("cartId") cartId: Integer, @Param("itemId") itemId: Integer): Unit

  @Query(value = "select sum(price) from cart_items where cart_id = :cartId", nativeQuery = true)
  def calculateGrandTotal(@Param("cartId") cartId: Integer): Float

  @Modifying
  @Query(value = "delete from cart_items where size_id = :pizzaSizeId", nativeQuery = true)
  def removeAllByPizzaSizeId(@Param("pizzaSizeId") pizzaSizeId: Integer): Unit
}


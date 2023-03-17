package com.radovan.spring.repository

import java.util

import com.radovan.spring.entity.PizzaSizeEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait PizzaSizeRepository extends JpaRepository[PizzaSizeEntity, Integer] {

  @Query(value = "select * from pizza_sizes where pizza_id = :pizzaId", nativeQuery = true)
  def findAllByPizzaId(@Param("pizzaId") pizzaId: Integer): util.List[PizzaSizeEntity]

  @Modifying
  @Query(value = "delete from pizza_sizes where pizza_id = :pizzaId", nativeQuery = true)
  def deleteAllByPizzaId(@Param("pizzaId") pizzaId: Integer): Unit

  @Query(value = "select * from pizza_sizes where name = :name and pizza_id = :pizzaId", nativeQuery = true)
  def findByNameAndPizzaId(@Param("name") name: String, @Param("pizzaId") pizzaId: Integer): PizzaSizeEntity
}

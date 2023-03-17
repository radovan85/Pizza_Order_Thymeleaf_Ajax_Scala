package com.radovan.spring.repository

import java.util

import com.radovan.spring.entity.PizzaEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait PizzaRepository extends JpaRepository[PizzaEntity, Integer] {

  @Query(value = "select * from pizzas where name ilike CONCAT('%', :keyword, '%')", nativeQuery = true)
  def findAllByKeyword(@Param("keyword") keyword: String): util.List[PizzaEntity]
}

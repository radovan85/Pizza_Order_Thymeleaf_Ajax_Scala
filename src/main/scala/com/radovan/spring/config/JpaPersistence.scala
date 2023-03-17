package com.radovan.spring.config

import java.util.Properties

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.{JpaTransactionManager, LocalContainerEntityManagerFactoryBean}
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = Array("com.radovan.spring.repository"))
class JpaPersistence {

  @Bean
  def entityManagerFactory: LocalContainerEntityManagerFactoryBean = {
    val em = new LocalContainerEntityManagerFactoryBean
    em.setDataSource(getHikariDataSource)
    em.setPackagesToScan("com.radovan.spring")
    val vendorAdapter = new HibernateJpaVendorAdapter
    em.setJpaVendorAdapter(vendorAdapter)
    em.setJpaProperties(additionalProperties)
    em
  }

  @Bean
  def getHikariDataSource: HikariDataSource = {
    val returnValue = new HikariDataSource
    returnValue.setDriverClassName("org.postgresql.Driver")
    returnValue.setJdbcUrl("jdbc:postgresql://localhost:5432/pizzadb")
    returnValue.setUsername("postgres")
    returnValue.setPassword("1111")
    returnValue
  }

  @Bean
  def transactionManager: PlatformTransactionManager = {
    val transactionManager = new JpaTransactionManager
    transactionManager.setEntityManagerFactory(entityManagerFactory.getObject)
    transactionManager
  }

  @Bean
  def exceptionTranslation = new PersistenceExceptionTranslationPostProcessor

  private[config]
  def additionalProperties = {
    val properties = new Properties
    properties.setProperty("hibernate.hbm2ddl.auto", "update")
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect")
    properties.setProperty("hibernate.enable_lazy_load_no_trans", "true")
    properties
  }
}


package com.radovan.spring.config

import com.radovan.spring.converter.TempConverter
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.servlet.config.annotation.{CorsRegistry, EnableWebMvc, ResourceHandlerRegistry, ViewResolverRegistry, WebMvcConfigurer}
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring5.view.ThymeleafViewResolver

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = Array("com.radovan.spring"))
class SpringMvcConfiguration extends WebMvcConfigurer {

  @Bean
  def templateResolver: SpringResourceTemplateResolver = {
    val templateResolver = new SpringResourceTemplateResolver
    templateResolver.setPrefix("/WEB-INF/templates/")
    templateResolver.setSuffix(".html")
    templateResolver
  }

  @Bean
  def templateEngine: SpringTemplateEngine = {
    val templateEngine = new SpringTemplateEngine
    templateEngine.setTemplateResolver(templateResolver)
    templateEngine.setEnableSpringELCompiler(true)
    templateEngine.addDialect(springSecurityDialect)
    templateEngine
  }

  override def configureViewResolvers(registry: ViewResolverRegistry): Unit = {
    val resolver = new ThymeleafViewResolver
    resolver.setTemplateEngine(templateEngine)
    registry.viewResolver(resolver)
  }

  override def addResourceHandlers(registry: ResourceHandlerRegistry): Unit = {
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
    registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
  }

  @Bean
  def getMapper: ModelMapper = {
    val mapper = new ModelMapper
    mapper.getConfiguration.setAmbiguityIgnored(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
    mapper.getConfiguration.setMatchingStrategy(MatchingStrategies.STRICT)
    mapper
  }

  @Bean
  def multipartResolver: MultipartResolver = {
    val multipartResolver = new CommonsMultipartResolver
    multipartResolver.setMaxUploadSize(10240000)
    multipartResolver
  }

  @Bean
  def springSecurityDialect = new SpringSecurityDialect

  override def addCorsMappings(registry: CorsRegistry): Unit = {
    registry.addMapping("/**").allowedOrigins("http://localhost:8080")
  }

  @Bean
  def getTempConverter = new TempConverter
}


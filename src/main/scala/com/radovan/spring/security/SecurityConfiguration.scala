package com.radovan.spring.security

import com.radovan.spring.security.handler.LoginSuccessHandler
import com.radovan.spring.service.impl.UserDetailsImpl
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @throws[Exception]
  override def configure(auth: AuthenticationManagerBuilder): Unit = {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)

  }

  @throws[Exception]
  override protected def configure(http: HttpSecurity): Unit = {
    http.formLogin.loginPage("/login").successHandler(new LoginSuccessHandler).loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password").permitAll
    http.logout.permitAll.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").and.csrf.disable
    http.authorizeRequests.antMatchers("/login").anonymous.antMatchers("/loginErrorPage").anonymous.antMatchers("/", "/suspensionPage").permitAll.antMatchers("/registerComplete", "/registerFail").permitAll.antMatchers("/loginPassConfirm", "/suspensionChecker").permitAll.antMatchers("/admin/**").hasAuthority("ADMIN").antMatchers("/carts/**", "/orders/**").hasAuthority("ROLE_USER").antMatchers("/register").anonymous.anyRequest.authenticated
  }

  @Bean
  override def userDetailsService = new UserDetailsImpl

  @Bean
  def passwordEncoder = new BCryptPasswordEncoder

  @throws[Exception]
  override def configure(web: WebSecurity): Unit = {
    web.ignoring.antMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**")
  }
}


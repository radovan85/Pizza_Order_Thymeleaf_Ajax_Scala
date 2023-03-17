package com.radovan.spring.security.handler

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class LoginSuccessHandler extends AuthenticationSuccessHandler {

  @throws[IOException]
  @throws[ServletException]
  override def onAuthenticationSuccess(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, authentication: Authentication): Unit = {
  }
}

package com.radovan.spring.controller

import java.security.Principal
import java.util.Optional

import com.radovan.spring.entity.UserEntity
import com.radovan.spring.exceptions.{InvalidUserException, SuspendedUserException}
import com.radovan.spring.model.RegistrationForm
import com.radovan.spring.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, RequestMethod, RequestParam}
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MainController {

  @Autowired
  private var customerService:CustomerService = _

  @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
  def indexPage = "index"

  @RequestMapping(value = Array("/home"), method = Array(RequestMethod.GET))
  def home = "fragments/homePage :: ajaxLoadedContent"

  @RequestMapping(value = Array("/login"), method = Array(RequestMethod.GET))
  def login(@RequestParam(value = "error", required = false) error: String, @RequestParam(value = "logout", required = false) logout: String, map: ModelMap) = "fragments/login :: ajaxLoadedContent"

  @RequestMapping(value = Array("/loginPassConfirm"), method = Array(RequestMethod.POST))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipal = Optional.ofNullable(principal)
    if (!authPrincipal.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loginErrorPage"), method = Array(RequestMethod.GET))
  def loginError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password")
    "fragments/login :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loggedout"), method = Array(RequestMethod.POST))
  def logout(redirectAttributes: RedirectAttributes): String = {
    SecurityContextHolder.clearContext()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/register"), method = Array(RequestMethod.GET))
  def renderRegistrationForm(map: ModelMap): String = {
    val registerForm = new RegistrationForm
    map.put("registerForm", registerForm)
    "fragments/registrationForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/register"), method = Array(RequestMethod.POST))
  def storeCustomer(@ModelAttribute("registerForm") registerForm: RegistrationForm): String = {
    customerService.storeCustomer(registerForm)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/registerComplete"), method = Array(RequestMethod.GET))
  def registrationCompleted = "fragments/registration_completed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/registerFail"), method = Array(RequestMethod.GET))
  def registrationFailed = "fragments/registration_failed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/suspensionChecker"), method = Array(RequestMethod.POST))
  def checkForSuspension: String = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    if (authUser.getEnabled == 0.toByte) {
      val error = new Error("Account suspended!")
      throw new SuspendedUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/suspensionPage"), method = Array(RequestMethod.GET))
  def suspensionInterceptor(map: ModelMap): String = {
    map.put("alert", "Account suspended!")
    "fragments/login :: ajaxLoadedContent"
  }
}


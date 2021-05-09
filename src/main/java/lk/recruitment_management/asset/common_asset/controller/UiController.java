package lk.recruitment_management.asset.common_asset.controller;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.user_management.entity.Role;
import lk.recruitment_management.asset.user_management.entity.User;
import lk.recruitment_management.asset.user_management.entity.UserSessionLog;
import lk.recruitment_management.asset.user_management.service.UserService;
import lk.recruitment_management.asset.user_management.service.UserSessionLogService;
import lk.recruitment_management.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UiController {

  private final UserService userService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserSessionLogService userSessionLogService;
  private final ApplicantService applicantService;

  @Autowired
  public UiController(UserService userService, DateTimeAgeService dateTimeAgeService,
                      UserSessionLogService userSessionLogService, ApplicantService applicantService) {
    this.userService = userService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userSessionLogService = userSessionLogService;
    this.applicantService = applicantService;
  }

  @GetMapping( value = {"/", "/index"} )
  public String index() {
    return "index";
  }

  @GetMapping( value = {"/home", "/mainWindow"} )
  public String getHome(Model model, RedirectAttributes redirectAttributes) {
    //do some logic here if you want something to be done whenever
    User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
    //when user has role applicant
    for ( Role role : authUser.getRoles() ) {
      Applicant applicant = applicantService.findByEmail(authUser.getUsername());
      if (applicant ==null){
        redirectAttributes.addFlashAttribute("applicant",new Applicant(authUser.getUsername()));
        return "redirect:/applicant/add";
      }
        if ( role.getRoleName().equals("APPLICANT") ) {
          return "applicant/mainWindow";
        }
    }


    return "mainWindow1";
  }

  @GetMapping( value = {"/login"} )
  public String getLogin() {
    return "login/login";
  }

  @GetMapping( value = {"/login/error10"} )
  public String getLogin10(Model model) {
    model.addAttribute("err", "You already entered wrong credential more than 10 times. \n Please meet the system" +
        " admin");
    return "login/login";
  }

  @GetMapping( value = {"/login/noUser"} )
  public String getLoginNoUser(Model model) {
    model.addAttribute("err", "There is no user according to the user name. \n Please try again !!");
    return "login/login";
  }

  @GetMapping( value = {"/unicodeTamil"} )
  public String getUnicodeTamil() {
    return "fragments/unicodeTamil";
  }

  @GetMapping( value = {"/unicodeSinhala"} )
  public String getUnicodeSinhala() {
    return "fragments/unicodeSinhala";
  }

  @GetMapping( "/education" )
  public String form() {
    return "education/addEducation";
  }
}

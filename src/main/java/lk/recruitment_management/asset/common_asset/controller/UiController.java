package lk.recruitment_management.asset.common_asset.controller;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.user_management.entity.User;
import lk.recruitment_management.asset.user_management.service.UserService;
import lk.recruitment_management.asset.user_management.service.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UiController {

  private final UserService userService;
  private final UserSessionLogService userSessionLogService;
  private final ApplicantService applicantService;

  @Autowired
  public UiController(UserService userService, UserSessionLogService userSessionLogService,
                      ApplicantService applicantService) {
    this.userService = userService;
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
    Applicant applicant = applicantService.findByEmail(authUser.getUsername());
    if ( applicant == null ) {
      redirectAttributes.addFlashAttribute("applicant", new Applicant(authUser.getUsername()));
      return "redirect:/applicant/add";
    }
    //when user has role applicant

    if ( authUser.getEmployee() == null ) {
      return "applicant/applicantMainWindow";
    }


    return "mainWindow1";
  }

  @GetMapping( value = {"/login"} )
  public String getLogin() {
    return "login/login";
  }


}

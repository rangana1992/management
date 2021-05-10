package lk.recruitment_management.asset.common_asset.controller;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.user_management.entity.User;
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
import java.util.stream.Collectors;

@Controller
public class UiController {

  private final UserService userService;
  private final UserSessionLogService userSessionLogService;
  private final ApplicantService applicantService;
  private final GazetteService gazetteService;
  private final DateTimeAgeService dateTimeAgeService;
  private final ApplicantGazetteService applicantGazetteService;

  @Autowired
  public UiController(UserService userService, UserSessionLogService userSessionLogService,
                      ApplicantService applicantService, GazetteService gazetteService,
                      DateTimeAgeService dateTimeAgeService, ApplicantGazetteService applicantGazetteService) {
    this.userService = userService;
    this.userSessionLogService = userSessionLogService;
    this.applicantService = applicantService;
    this.gazetteService = gazetteService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.applicantGazetteService = applicantGazetteService;
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
      int applicantAge = dateTimeAgeService.getAge(applicant.getDateOfBirth());
      //need to send applied gazette and unapply gazette
      List< Gazette > gazettes =
          gazetteService.findAll().stream().filter(x -> x.getGazetteStatus().equals(GazetteStatus.AC) && x.getAge() >= applicantAge).collect(Collectors.toList());

      // applied gazette
      List< Gazette > gazetteDb = applicantGazetteService.findByApplicant(applicant);
      gazettes.removeAll(gazetteDb);

      model.addAttribute("appliedGazettes", gazetteDb);
      model.addAttribute("unAppliedGazette", gazettes);

      return "applicant/applicantMainWindow";
    }


    return "mainWindow1";
  }

  @GetMapping( value = {"/login"} )
  public String getLogin() {
    return "login/login";
  }


}

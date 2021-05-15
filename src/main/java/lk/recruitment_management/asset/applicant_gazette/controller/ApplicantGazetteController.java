package lk.recruitment_management.asset.applicant_gazette.controller;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.user_management.service.UserService;
import lk.recruitment_management.util.service.MakeAutoGenerateNumberService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping( "/applicantGazette" )
public class ApplicantGazetteController {

  private final ApplicantGazetteService applicantGazetteService;
  private final GazetteService gazetteService;
  private final ApplicantService applicantService;
  private final UserService userService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  public ApplicantGazetteController(ApplicantGazetteService applicantGazetteService, GazetteService gazetteService,
                                    ApplicantService applicantService, UserService userService,
                                    MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.applicantGazetteService = applicantGazetteService;
    this.gazetteService = gazetteService;
    this.applicantService = applicantService;
    this.userService = userService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  @GetMapping( "/apply/{id}" )
  public String apply(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    ApplicantGazette applicantGazette = new ApplicantGazette();
    applicantGazette.setApplicant(applicantService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    applicantGazette.setGazette(gazette);
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.P);
    model.addAttribute("gazette", gazette);
    model.addAttribute("applicantGazette", applicantGazette);
    model.addAttribute("applyingRanks", ApplyingRank.values());
    return "applicantGazette/addApplicantGazette";
  }

  @PostMapping( "/save" )
  public String persist(@ModelAttribute ApplicantGazette applicantGazette) {

    if ( applicantGazette.getId() == null ) {
      ApplicantGazette lastApplicantGazette = applicantGazetteService.lastApplicantGazette();
      if ( lastApplicantGazette == null ) {
        applicantGazette.setCode("SLPAG" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        applicantGazette.setCode("SLPAG" + makeAutoGenerateNumberService.numberAutoGen(lastApplicantGazette.getCode().substring(5)).toString());
      }
    }
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.P);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/home";
  }

  @GetMapping( value = "/approve/{id}" )
  public String approve(@PathVariable Integer id) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.A);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/applicant";

  }

  @GetMapping( value = "/reject/{id}" )
  public String reject(@PathVariable Integer id) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.REJ);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/applicant";
  }

  // toview details
  @GetMapping("/view/{id}")
  public String applicantHistory(@PathVariable("id")Integer id , Model model){
    Applicant applicant = applicantService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    Gazette gazette = gazetteService.findById(id);
    ApplicantGazette applicantGazette = applicantGazetteService.findByGazetteAndApplicant(gazette,applicant);
    model.addAttribute("applicantGazette",applicantGazette);
    model.addAttribute("gazette",gazette);
    return "applicantInterview/applicantInterviewDetail";
  }



}

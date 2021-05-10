package lk.recruitment_management.asset.applicant_gazette.controller;


import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.user_management.service.UserService;
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

  public ApplicantGazetteController(ApplicantGazetteService applicantGazetteService, GazetteService gazetteService,
                                    ApplicantService applicantService, UserService userService) {
    this.applicantGazetteService = applicantGazetteService;
    this.gazetteService = gazetteService;
    this.applicantService = applicantService;
    this.userService = userService;
  }

  @GetMapping( "/apply/{id}" )
  public String apply(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    ApplicantGazette applicantGazette = new ApplicantGazette();
    applicantGazette.setApplicant(applicantService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    applicantGazette.setGazette(gazette);
    model.addAttribute("gazette", gazette);
    model.addAttribute("applicantGazette", applicantGazette);
    model.addAttribute("applyingRanks", ApplyingRank.values());
    return "applicantGazette/addApplicantGazette";
  }

  @PostMapping( "/save" )
  public String persist(@ModelAttribute ApplicantGazette applicantGazette) {
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.NTA);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/home";
  }



}

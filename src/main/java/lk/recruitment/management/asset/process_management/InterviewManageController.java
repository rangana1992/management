package lk.recruitment.management.asset.process_management;

import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping( "/interviewManage" )
public class InterviewManageController {
  private final ApplicantService applicantService;

  public InterviewManageController(ApplicantService applicantService) {
    this.applicantService = applicantService;
  }
  private String commonThing(Model model, List< Applicant > applicants, String title, String uri, String btnText) {
    model.addAttribute("applicants", applicants);
    model.addAttribute("headerTitle", title);
    model.addAttribute("uri", uri);
    model.addAttribute("btnText", btnText);
    return "interviewSchedule/interview";
  }

  @GetMapping("/firstInterview")
  public String firstInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "First Interview",
                       "firstInterviewPdf", "First Interview Pdf");
  }
  //todo -> first interview result enter
  @GetMapping("/secondInterview")
  public String secondInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.SND), "Second Interview",
                       "secondInterviewPdf", "Second Interview Pdf");
  }
  //todo-> second interview result enter
  @GetMapping("/thirdInterview")
  public String thirdInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.TND), "Third Interview",
                       "thirdInterviewPdf", "Third Interview Pdf");
  }
  //todo-> third interview result enter
  @GetMapping("/fourthInterview")
  public String fourthInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "Fourth Interview",
                       "fourthInterviewPdf", "Fourth Interview Pdf");
  }
  //todo-> fourth interview result enter
}

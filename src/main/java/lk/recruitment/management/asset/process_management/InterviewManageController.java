package lk.recruitment.management.asset.process_management;

import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/*
 @GetMapping( value = "/pdf" )
  public void allPdf(HttpServletRequest request, HttpServletResponse response) {
    List< Applicant > employees = applicantService.getAllEmployeePdfAndExcel();
    boolean isFlag = applicantService.createPdf(employees, context, request, response);

    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + "employees" + ".pdf");
      fileHandelService.filedownload(fullPath, response, "employees.pdf");
    }

  }

  @GetMapping( value = "/excel" )
  public void allExcel(HttpServletRequest request, HttpServletResponse response) {
    List< Applicant > employees = applicantService.getAllEmployeePdfAndExcel();
    boolean isFlag = applicantService.createExcell(employees, context, request, response);
    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + "employees" + ".xls");
      fileHandelService.filedownload(fullPath, response, "employees.xls");

    }

  }
*/

//todo-> no need to manage pdf to 3rd and 4th

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

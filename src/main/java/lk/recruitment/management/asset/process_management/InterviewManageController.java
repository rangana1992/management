package lk.recruitment.management.asset.process_management;

import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import lk.recruitment.management.util.service.FileHandelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/interviewManage" )
public class InterviewManageController {
  private final ApplicantService applicantService;
  private final FileHandelService fileHandelService;
  private final ServletContext context;

  public InterviewManageController(ApplicantService applicantService, FileHandelService fileHandelService,
                                   ServletContext context) {
    this.applicantService = applicantService;
    this.fileHandelService = fileHandelService;
    this.context = context;
  }

  private String commonThing(Model model, List< Applicant > applicants, String title, String uriPdf,
                             String btnTextPdf, String uriExcel, String btnTextExcel) {
    model.addAttribute("applicants", applicants);
    model.addAttribute("headerTitle", title);
    model.addAttribute("uriPdf", uriPdf);
    model.addAttribute("btnTextPdf", btnTextPdf);
    model.addAttribute("uriExcel", uriExcel);
    model.addAttribute("btnTextExcel", btnTextExcel);
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
  */
  @GetMapping( value = "/{interviewType}" )
  public void allExcel(@PathVariable( "interviewType" ) String interviewType, HttpServletRequest request,
                       HttpServletResponse response) {
    List< Applicant > applicants;

    switch ( interviewType ) {
      case "firstInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FST);
        break;
      case "secondInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.SND);
        break;
      case "thirdInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.TND);
        break;
      case "fourthInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FTH);
        break;
      default:
        applicants = null;
    }

    boolean isFlag = applicantService.createExcel(applicants, context, request, response);
    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + "applicants" + ".xls");
      fileHandelService.fileDownload(fullPath, response, "applicant.xls");
    }
  }


//todo-> no need to manage pdf to 3rd and 4th

  @GetMapping( "/firstInterview" )
  public String firstInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "First Interview",
                       "firstInterviewPdf", "First Interview Pdf", "firstInterviewExcel", "First Interview Excel");
  }

  //todo -> first interview result enter
  @GetMapping( "/secondInterview" )
  public String secondInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.SND), "Second Interview",
                       "secondInterviewPdf", "Second Interview Pdf",
                       "secondInterviewExcel", "Second Interview Excel");
  }

  //todo-> second interview result enter
  @GetMapping( "/thirdInterview" )
  public String thirdInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.TND), "Third Interview",
                       "", "",
                       "thirdInterviewExcel", "Third Interview Excel");
  }

  //todo-> third interview result enter
  @GetMapping( "/fourthInterview" )
  public String fourthInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "Fourth Interview",
                       "", "",
                       "fourthInterviewExcel", "Fourth Interview Excel");
  }
  //todo-> fourth interview result enter
}

package lk.recruitment_management.asset.process_management;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.ApplicantSisCrdCid;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums.InternalDivision;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums.PassFailed;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.service.ApplicantSisCrdCidService;
import lk.recruitment_management.util.service.FileHandelService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping( "/interviewManage" )
public class InterviewManageController {
  private final ApplicantService applicantService;
  private final FileHandelService fileHandelService;
  private final ServletContext context;
  private final ApplicantSisCrdCidService applicantSisCrdCidService;

  public InterviewManageController(ApplicantService applicantService, FileHandelService fileHandelService,
                                   ServletContext context, ApplicantSisCrdCidService applicantSisCrdCidService) {
    this.applicantService = applicantService;
    this.fileHandelService = fileHandelService;
    this.context = context;
    this.applicantSisCrdCidService = applicantSisCrdCidService;
  }

  private String commonThing(Model model, List< Applicant > applicants, String title, String uriPdf,
                             String btnTextPdf, String uriExcel, String btnTextExcel,boolean addStatus) {
    model.addAttribute("applicants", applicants);
    model.addAttribute("headerTitle", title);
    model.addAttribute("uriPdf", uriPdf);
    model.addAttribute("btnTextPdf", btnTextPdf);
    model.addAttribute("uriExcel", uriExcel);
    model.addAttribute("btnTextExcel", btnTextExcel);
    model.addAttribute("addStatus", addStatus);
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
    String sheetName;
    switch ( interviewType ) {
      case "firstInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FST);
        sheetName = "First Interview Applicant List";
        break;
      case "secondInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.SND);
        sheetName = "Second Interview Applicant List";
        break;
      case "thirdInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.TND);
        sheetName = "Third Interview Applicant List";
        break;
      case "fourthInterviewExcel":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FTH);
        sheetName = "Fourth Interview Applicant List";
        break;
      case "SIS":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
        sheetName = InternalDivision.SIS.getInternalDivision();
        break;
      case "CID":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
        sheetName = InternalDivision.CID.getInternalDivision();
        break;
      case "CRD":
        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
        sheetName = InternalDivision.CRD.getInternalDivision();
        break;
      default:
        applicants = null;
        sheetName = "No Applicant to show";
    }
    boolean isFlag = applicantService.createExcel(applicants, context, request, response, sheetName);
    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + sheetName + ".xls");
      fileHandelService.fileDownload(fullPath, response, sheetName + ".xls");
    }
  }

//todo-> no need to manage pdf to 3rd and 4th

  @GetMapping( "/firstInterview" )
  public String firstInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "First Interview",
                       "firstInterviewPdf", "First Interview Pdf", "firstInterviewExcel", "First Interview Excel",true);
  }

  //todo -> first interview result enter

  @GetMapping( "/secondInterview" )
  public String secondInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.SND), "Second Interview",
                       "secondInterviewPdf", "Second Interview Pdf",
                       "secondInterviewExcel", "Second Interview Excel",true);
  }

  //todo-> second interview result enter

  @GetMapping( "/thirdInterview" )
  public String thirdInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.TND), "Third Interview",
                       null, null,
                       "thirdInterviewExcel", "Third Interview Excel",false);
  }

  //todo-> third interview result enter
  @GetMapping( "/fourthInterview" )
  public String fourthInterview(Model model) {
    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FTH), "Fourth Interview",
                       null, null,
                       "fourthInterviewExcel", "Fourth Interview Excel",false);
  }

  //todo-> fourth interview result enter

  @GetMapping( "/cidcrdsis" )
  public String cidCRDSIS(Model model) {
    model.addAttribute("applicants", applicantService.findByApplicantStatus(ApplicantStatus.FTH));
//form action
    model.addAttribute("formAction", "cidcrdsis");
    //cid
    model.addAttribute("uriCID", "CID");
    model.addAttribute("btnTextCID", "Get CID Excel");
    model.addAttribute("internalDivisionCID", InternalDivision.CID);
    //sis
    model.addAttribute("uriSIS", "SIS");
    model.addAttribute("btnTextSIS", "Get SIS Excel");
    model.addAttribute("internalDivisionSIS", InternalDivision.SIS);
    //crd
    model.addAttribute("uriCRD", "CRD");
    model.addAttribute("btnTextCRD", "Get CRD Excel");
    model.addAttribute("internalDivisionCRD", InternalDivision.CRD);
    return "interviewSchedule/interviewCIDSISCRD";
  }

  @PostMapping( "/cidcrdsis" )
  public String saveResult(@ModelAttribute ApplicantSisCrdCid applicantSisCrdCid,
                           RedirectAttributes redirectAttributes) throws IOException {

    int i = 0;
    HSSFWorkbook workbook = new HSSFWorkbook(applicantSisCrdCid.getMultipartFile().getInputStream());
    //Creates a worksheet object representing the first sheet
    HSSFSheet worksheet = workbook.getSheetAt(0);
    //Reads the data in excel file until last row is encountered

    InternalDivision internalDivision;

    if ( InternalDivision.SIS.getInternalDivision().equals(worksheet.getSheetName()) ) {
      internalDivision = InternalDivision.SIS;
    } else if ( InternalDivision.CID.getInternalDivision().equals(worksheet.getSheetName()) ) {
      internalDivision = InternalDivision.CID;
    } else if ( InternalDivision.CRD.getInternalDivision().equals(worksheet.getSheetName()) ) {
      internalDivision = InternalDivision.CRD;
    } else {
      internalDivision = InternalDivision.NOT;
    }
    if ( InternalDivision.NOT.equals(internalDivision) ) {
      redirectAttributes.addFlashAttribute("message", internalDivision.getInternalDivision());
      return "redirect:/interviewManage/cidcrdsis";
    }


    while ( i < worksheet.getLastRowNum() ) {
      HSSFRow row = worksheet.getRow(i++);

      if ( i == 1 ) {
        if ( !row.getCell(3).getRichStringCellValue().toString().equals("NIC") && !row.getCell(6).getRichStringCellValue().toString().equals("Result") ) {
          redirectAttributes.addFlashAttribute("message", "Some one change the excel sheet please provide valid excel" +
              " sheet");
          return "redirect:/interviewManage/cidcrdsis";
        }
      } else {
        ApplicantSisCrdCid applicantSisCrdCidToSave = new ApplicantSisCrdCid();
        //get applicant using NIC
        Applicant applicant = applicantService.findByNic(row.getCell(3).getRichStringCellValue().getString());
        //get applicant result
        PassFailed passFailed;
        if ( PassFailed.PASS.getPassFailed().equals(row.getCell(6).getRichStringCellValue().toString()) ) {
          passFailed = PassFailed.PASS;
        } else {
          passFailed = PassFailed.FAILED;
        }
        // get all applicant Sis Crd Cid result
        List< ApplicantSisCrdCid > applicantSisCrdCids = applicantSisCrdCidService.findByApplicant(applicant);
        // get all applicant Sis Crd Cid result size
        if ( applicantSisCrdCids.size() == 2 ) {
          if ( PassFailed.PASS.equals(passFailed) && PassFailed.PASS.equals(applicantSisCrdCids.get(0).getPassFailed()) && PassFailed.PASS.equals(applicantSisCrdCids.get(1).getPassFailed()) ) {
            applicantSisCrdCidToSave.setApplicant(applicant);
            applicantSisCrdCidToSave.setPassFailed(passFailed);
            applicantSisCrdCidToSave.setInternalDivision(internalDivision);
            applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
            //all result would be passed applicant status needs to change and applicant is suitable to second interview
            applicant.setApplicantStatus(ApplicantStatus.SND);
            applicantService.persist(applicant);

          } else {
            applicantSisCrdCidToSave.setApplicant(applicant);
            applicantSisCrdCidToSave.setPassFailed(passFailed);
            applicantSisCrdCidToSave.setInternalDivision(internalDivision);
            applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
            //all result would be passed applicant status needs to change and applicant is not suitable to second interview
            applicant.setApplicantStatus(ApplicantStatus.FSTR);
            applicantService.persist(applicant);
          }
          // need to validate all result status is pass
        } else {
          applicantSisCrdCidToSave.setApplicant(applicant);
          applicantSisCrdCidToSave.setPassFailed(passFailed);
          applicantSisCrdCidToSave.setInternalDivision(internalDivision);
          applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
        }
      }
    }
    return "redirect:/interviewManage/cidcrdsis";
  }

}

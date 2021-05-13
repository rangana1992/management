package lk.recruitment_management.asset.process_management.interview_manage;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_gazette_interview.service.ApplicantGazetteInterviewService;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.service.ApplicantSisCrdCidService;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.interview.entity.Enum.InterviewName;
import lk.recruitment_management.asset.interview.service.InterviewService;
import lk.recruitment_management.util.service.FileHandelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
  private final ApplicantSisCrdCidService applicantSisCrdCidService;
  private final InterviewService interviewService;
  private final ApplicantGazetteInterviewService applicantGazetteInterviewService;
  private final GazetteService gazetteService;

  public InterviewManageController(ApplicantService applicantService, FileHandelService fileHandelService,
                                   ServletContext context, ApplicantSisCrdCidService applicantSisCrdCidService,
                                   InterviewService interviewService,
                                   ApplicantGazetteInterviewService applicantGazetteInterviewService,
                                   GazetteService gazetteService) {
    this.applicantService = applicantService;
    this.fileHandelService = fileHandelService;
    this.context = context;
    this.applicantSisCrdCidService = applicantSisCrdCidService;
    this.interviewService = interviewService;
    this.applicantGazetteInterviewService = applicantGazetteInterviewService;
    this.gazetteService = gazetteService;
  }

  private String commonThing(Model model, List< Applicant > applicants, String title, String uriPdf,
                             String btnTextPdf, String uriExcel, String btnTextExcel, boolean addStatus,
                             String resultEnter) {
    model.addAttribute("applicants", applicants);
    model.addAttribute("headerTitle", title);
    model.addAttribute("uriPdf", uriPdf);
    model.addAttribute("btnTextPdf", btnTextPdf);
    model.addAttribute("uriExcel", uriExcel);
    model.addAttribute("btnTextExcel", btnTextExcel);
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("resultEnter", resultEnter);
    return "interviewSchedule/interview";
  }

  @GetMapping("/manage")
  private String gazette(Model model){
    model.addAttribute("gazettes", gazetteService.findByGazetteStatus(GazetteStatus.IN));
    return "interviewSchedule/gazetteView";
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
  @GetMapping( value = "/{interviewType}/{id}" )
  public void allExcel(@PathVariable( "interviewType" ) String interviewType,@PathVariable("id")Integer id, HttpServletRequest request,
                       HttpServletResponse response) {
        List< Applicant > applicants = new ArrayList<>();


    String sheetName =" sample";
//    switch ( interviewType ) {
//      case "firstInterviewExcel":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FST);
//        sheetName = "First Interview Applicant List";
//        break;
//      case "secondInterviewExcel":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.SND);
//        sheetName = "Second Interview Applicant List";
//        break;
//      case "thirdInterviewExcel":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.TND);
//        sheetName = "Third Interview Applicant List";
//        break;
//      case "fourthInterviewExcel":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FTH);
//        sheetName = "Fourth Interview Applicant List";
//        break;
//      case "SIS":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
//        sheetName = InternalDivision.SIS.getInternalDivision();
//        break;
//      case "CID":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
//        sheetName = InternalDivision.CID.getInternalDivision();
//        break;
//      case "CRD":
//        applicants = applicantService.findByApplicantStatus(ApplicantStatus.FSTP);
//        sheetName = InternalDivision.CRD.getInternalDivision();
//        break;
//      default:
//        applicants = null;
//        sheetName = "No Applicant to show";
//    }

    boolean isFlag = applicantService.createExcel(applicants, context, request, response, sheetName);
    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + sheetName + ".xls");
      fileHandelService.fileDownload(fullPath, response, sheetName + ".xls");
    }
  }
//first intrerview gazzet

//  @GetMapping( "/firstInterview" )
//  public String firstInterview(Model model) {
//    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FST), "First Interview",
//                       "firstInterviewPdf", "First Interview Pdf", "firstInterviewExcel", "First Interview Excel",
//                       true, "firstResult");
//  }

  //first interview pdf printing
  @GetMapping( "/firstInterviewPdf" )
  public String firstInterviewPdf() {

    return "print/pdfSilentPrint";
  }

/*  @GetMapping(value = "/file/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity< InputStreamResource > invoicePrint(@PathVariable("id")Integer id) throws DocumentException {
    var headers = new HttpHeaders();
    headers.add("Content-Disposition", "inline; filename=interview.pdf");
    InputStreamResource pdfFile = new InputStreamResource(applicantService.createPDF(id));

    return ResponseEntity
        .ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfFile);
  }

  @GetMapping("/fileView/{id}")
  public String fileRequest(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
    model.addAttribute("pdfFile", MvcUriComponentsBuilder
        .fromMethodName(InvoiceController.class, "invoicePrint", id)
        .toUriString());
    model.addAttribute("redirectUrl",MvcUriComponentsBuilder
        .fromMethodName(InvoiceController.class, "getInvoiceForm","")
        .toUriString());
    return "print/pdfSilentPrint";
  }*/

  //first interview result enter
  @GetMapping( "/firstResult/{id}" )
  public String firstInterviewResult(@PathVariable( "id" ) Integer id, Model model) {
    Applicant applicant = applicantService.findById(id);
    model.addAttribute("applicantDetail", applicant);
    //todo :
//    model.addAttribute("applicantInterviews", applicantGazetteInterviewService.findByApplicant(applicant)
//        .stream()
//        .filter(x -> x.getApplicant().equals(applicant) && x.getApplicantGazetteInterviewStatus().equals(ApplicantGazetteInterviewStatus.ACT))
//        .collect(Collectors.toList()));
    model.addAttribute("interviews", interviewService.findByInterviewName(InterviewName.FIRST));
    return "interviewSchedule/addApplicantInterviewResult";
  }

  //absent first todo
//  @GetMapping( "/absent/firstResult/{id}" )
//  public String firstAbsentInterviewResult(@PathVariable( "id" ) Integer id) {
//    Applicant applicant = applicantService.findById(id);
//    applicant.setApplicantStatus(ApplicantStatus.FSTR);
//    applicantService.persist(applicant);
//    return "redirect:/interviewManage/firstInterview";
//  }

  //todo

//  @GetMapping( "/secondInterview" )
//  public String secondInterview(Model model) {
//    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.SND), "Second Interview",
//                       "secondInterviewPdf", "Second Interview Pdf",
//                       "secondInterviewExcel", "Second Interview Excel", true, "secondResult");
//  }

  //second interview result enter
  @GetMapping( "/secondResult/{id}" )
  public String secondInterviewResult(@PathVariable( "id" ) Integer id, Model model) {
    //todo

    System.out.println("interview second result");
    return "";
  }

  // absent second todo
//  @GetMapping( "/absent/secondResult/{id}" )
//  public String secondAbsentInterviewResult(@PathVariable( "id" ) Integer id) {
//    Applicant applicant = applicantService.findById(id);
//    applicant.setApplicantStatus(ApplicantStatus.SNDR);
//    applicantService.persist(applicant);
//    return "redirect:/interviewManage/secondInterview";
//  }

  //todo
//
//  @GetMapping( "/thirdInterview" )
//  public String thirdInterview(Model model) {
//    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.TND), "Third Interview",
//                       null, null,
//                       "thirdInterviewExcel", "Third Interview Excel", false, null);
//  }

  //todo-> third interview result enter
//  @GetMapping( "/fourthInterview" )
//  public String fourthInterview(Model model) {
//    return commonThing(model, applicantService.findByApplicantStatus(ApplicantStatus.FTH), "Fourth Interview",
//                       null, null,
//                       "fourthInterviewExcel", "Fourth Interview Excel", false, null);
//  }

  //todo-> fourth interview result enter
//
//  @GetMapping( "/cidcrdsis" )
//  public String cidCRDSIS(Model model) {
//    model.addAttribute("applicants", applicantService.findByApplicantStatus(ApplicantStatus.FTH));
////form action
//    model.addAttribute("formAction", "cidcrdsis");
//    //cid
//    model.addAttribute("uriCID", "CID");
//    model.addAttribute("btnTextCID", "Get CID Excel");
//    model.addAttribute("internalDivisionCID", InternalDivision.CID);
//    //sis
//    model.addAttribute("uriSIS", "SIS");
//    model.addAttribute("btnTextSIS", "Get SIS Excel");
//    model.addAttribute("internalDivisionSIS", InternalDivision.SIS);
//    //crd
//    model.addAttribute("uriCRD", "CRD");
//    model.addAttribute("btnTextCRD", "Get CRD Excel");
//    model.addAttribute("internalDivisionCRD", InternalDivision.CRD);
//    return "interviewSchedule/interviewCIDSISCRD";
//  }

  //todo

//  @PostMapping( "/cidcrdsis" )
//  public String saveResult(@ModelAttribute ApplicantSisCrdCid applicantSisCrdCid,
//                           RedirectAttributes redirectAttributes) throws IOException {
//
//    int i = 0;
//    HSSFWorkbook workbook = new HSSFWorkbook(applicantSisCrdCid.getMultipartFile().getInputStream());
//    //Creates a worksheet object representing the first sheet
//    HSSFSheet worksheet = workbook.getSheetAt(0);
//    //Reads the data in excel file until last row is encountered
//
//    InternalDivision internalDivision;
//
//    if ( InternalDivision.SIS.getInternalDivision().equals(worksheet.getSheetName()) ) {
//      internalDivision = InternalDivision.SIS;
//    } else if ( InternalDivision.CID.getInternalDivision().equals(worksheet.getSheetName()) ) {
//      internalDivision = InternalDivision.CID;
//    } else if ( InternalDivision.CRD.getInternalDivision().equals(worksheet.getSheetName()) ) {
//      internalDivision = InternalDivision.CRD;
//    } else {
//      internalDivision = InternalDivision.NOT;
//    }
//    if ( InternalDivision.NOT.equals(internalDivision) ) {
//      redirectAttributes.addFlashAttribute("message", internalDivision.getInternalDivision());
//      return "redirect:/interviewManage/cidcrdsis";
//    }
//
//
//    while ( i < worksheet.getLastRowNum() ) {
//      HSSFRow row = worksheet.getRow(i++);
//
//      if ( i == 1 ) {
//        if ( !row.getCell(3).getRichStringCellValue().toString().equals("NIC") && !row.getCell(6).getRichStringCellValue().toString().equals("Result") ) {
//          redirectAttributes.addFlashAttribute("message", "Some one change the excel sheet please provide valid excel" +
//              " sheet");
//          return "redirect:/interviewManage/cidcrdsis";
//        }
//      } else {
//        ApplicantSisCrdCid applicantSisCrdCidToSave = new ApplicantSisCrdCid();
//        //get applicant using NIC
//        Applicant applicant = applicantService.findByNic(row.getCell(3).getRichStringCellValue().getString());
//        //get applicant result
//        PassFailed passFailed;
//        if ( PassFailed.PASS.getPassFailed().equals(row.getCell(6).getRichStringCellValue().toString()) ) {
//          passFailed = PassFailed.PASS;
//        } else {
//          passFailed = PassFailed.FAILED;
//        }
//        // get all applicant Sis Crd Cid result
//        List< ApplicantSisCrdCid > applicantSisCrdCids = applicantSisCrdCidService.findByApplicant(applicant);
//        // get all applicant Sis Crd Cid result size
//        if ( applicantSisCrdCids.size() == 2 ) {
//          if ( PassFailed.PASS.equals(passFailed) && PassFailed.PASS.equals(applicantSisCrdCids.get(0).getPassFailed()) && PassFailed.PASS.equals(applicantSisCrdCids.get(1).getPassFailed()) ) {
//            applicantSisCrdCidToSave.setApplicant(applicant);
//            applicantSisCrdCidToSave.setPassFailed(passFailed);
//            applicantSisCrdCidToSave.setInternalDivision(internalDivision);
//            applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
//            //all result would be passed applicant status needs to change and applicant is suitable to second interview
//            applicant.setApplicantStatus(ApplicantStatus.SND);
//            applicantService.persist(applicant);
//
//          } else {
//            applicantSisCrdCidToSave.setApplicant(applicant);
//            applicantSisCrdCidToSave.setPassFailed(passFailed);
//            applicantSisCrdCidToSave.setInternalDivision(internalDivision);
//            applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
//            //all result would be passed applicant status needs to change and applicant is not suitable to second
//            // interview
//            applicant.setApplicantStatus(ApplicantStatus.FSTR);
//            applicantService.persist(applicant);
//          }
//          // need to validate all result status is pass
//        } else {
//          applicantSisCrdCidToSave.setApplicant(applicant);
//          applicantSisCrdCidToSave.setPassFailed(passFailed);
//          applicantSisCrdCidToSave.setInternalDivision(internalDivision);
//          applicantSisCrdCidService.persist(applicantSisCrdCidToSave);
//        }
//      }
//    }
//    return "redirect:/interviewManage/cidcrdsis";
//  }

}

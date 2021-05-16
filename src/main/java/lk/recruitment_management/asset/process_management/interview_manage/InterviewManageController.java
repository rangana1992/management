package lk.recruitment_management.asset.process_management.interview_manage;

import io.micrometer.core.instrument.Gauge;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_file.service.ApplicantFilesService;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.enums.ApplicantGazetteInterviewStatus;
import lk.recruitment_management.asset.applicant_gazette_interview.service.ApplicantGazetteInterviewService;
import lk.recruitment_management.asset.applicant_gazette_interview_result.service.ApplicantGazetteInterviewResultService;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.ApplicantGazetteSisCrdCid;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.enums.InternalDivision;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.service.ApplicantGazetteSisCrdCidService;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.interview.entity.Enum.InterviewName;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.asset.interview.service.InterviewService;
import lk.recruitment_management.util.service.DateTimeAgeService;
import lk.recruitment_management.util.service.FileHandelService;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.enums.PassFailed;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/interviewManage" )
public class InterviewManageController {
  private final ApplicantService applicantService;
  private final FileHandelService fileHandelService;
  private final ServletContext context;
  private final ApplicantGazetteSisCrdCidService applicantGazetteSisCrdCidService;
  private final InterviewService interviewService;
  private final ApplicantGazetteInterviewService applicantGazetteInterviewService;
  private final GazetteService gazetteService;
  private final ApplicantGazetteService applicantGazetteService;
  private final ApplicantFilesService applicantFilesService;
  private final DateTimeAgeService dateTimeAgeService;
  private final ApplicantGazetteInterviewResultService applicantGazetteInterviewResultService;

  public InterviewManageController(ApplicantService applicantService, FileHandelService fileHandelService,
                                   ServletContext context,
                                   ApplicantGazetteSisCrdCidService applicantGazetteSisCrdCidService,
                                   InterviewService interviewService,
                                   ApplicantGazetteInterviewService applicantGazetteInterviewService,
                                   GazetteService gazetteService, ApplicantGazetteService applicantGazetteService,
                                   ApplicantFilesService applicantFilesService, DateTimeAgeService dateTimeAgeService
      , ApplicantGazetteInterviewResultService applicantGazetteInterviewResultService) {
    this.applicantService = applicantService;
    this.fileHandelService = fileHandelService;
    this.context = context;
    this.applicantGazetteSisCrdCidService = applicantGazetteSisCrdCidService;
    this.interviewService = interviewService;
    this.applicantGazetteInterviewService = applicantGazetteInterviewService;
    this.gazetteService = gazetteService;
    this.applicantGazetteService = applicantGazetteService;
    this.applicantFilesService = applicantFilesService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.applicantGazetteInterviewResultService = applicantGazetteInterviewResultService;
  }

  private String commonThing(Model model, List< ApplicantGazette > applicantGazettes,
                             String title, String uriPdf,
                             String btnTextPdf, String uriExcel, String btnTextExcel, boolean addStatus,
                             String resultEnter, ApplicantGazetteStatus applicantGazetteStatus) {
    model.addAttribute("headerTitle", title);
    model.addAttribute("uriPdf", uriPdf);
    model.addAttribute("btnTextPdf", btnTextPdf);
    model.addAttribute("uriExcel", uriExcel);
    model.addAttribute("btnTextExcel", btnTextExcel);
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("resultEnter", resultEnter);
    model.addAttribute("applicantGazettes", applicantGazettes);
    model.addAttribute("applicantGazetteStatus", applicantGazetteStatus);
    return "interviewSchedule/interview";
  }

  @GetMapping( "/manage" )
  private String gazette(Model model) {
    model.addAttribute("gazettes", gazetteService.findByGazetteStatus(GazetteStatus.IN));
    return "interviewSchedule/gazetteView";
  }

  @GetMapping( value = "/{interviewType}/{id}" )
  public void allExcel(@PathVariable( "interviewType" ) String interviewType, @PathVariable( "id" ) Integer id,
                       HttpServletRequest request,
                       HttpServletResponse response) {
    List< ApplicantGazette > applicantGazettes;

    Gazette gazette = gazetteService.findById(id);

    String sheetName;
    switch ( interviewType ) {
      case "firstInterviewExcel":
        applicantGazettes = applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.FST
            , gazette);
        sheetName = "First Interview Applicant List";
        break;
      case "secondInterviewExcel":
        applicantGazettes = applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.SND
            , gazette);
        sheetName = "Second Interview Applicant List";
        break;
      case "thirdInterviewExcel":
        applicantGazettes = applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.TND
            , gazette);
        sheetName = "Third Interview Applicant List";
        break;
      case "fourthInterviewExcel":
        applicantGazettes = applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.FTH
            , gazette);
        sheetName = "Fourth Interview Applicant List";
        break;
      case "SIS":
        applicantGazettes =
            applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.A, gazette);
        sheetName = InternalDivision.SIS.getInternalDivision();
        break;
      case "CID":
        applicantGazettes =
            applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.A, gazette);
        sheetName = InternalDivision.CID.getInternalDivision();
        break;
      case "CRD":
        applicantGazettes =
            applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.A, gazette);
        sheetName = InternalDivision.CRD.getInternalDivision();
        break;
      default:
        applicantGazettes = applicantGazetteService.findAll();
        sheetName = "No Applicant to show";
    }

    boolean isFlag = applicantService.createExcel(applicantGazettes, context, request, response, sheetName);
    if ( isFlag ) {
      String fullPath = request.getServletContext().getRealPath("/resources/report/" + sheetName + ".xls");
      fileHandelService.fileDownload(fullPath, response, sheetName + ".xls");
    }
  }

  //first interview gazette
  @GetMapping( "/firstInterview/{id}" )
  public String firstInterview(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    return commonThing(model,
                       applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.FST,
                                                                                      gazette), "First Interview",
                       "firstInterviewPdf/" + gazette.getId(), "First Interview Pdf",
                       "firstInterviewExcel/" + gazette.getId(), "First Interview Excel",
                       true, "firstResult", ApplicantGazetteStatus.FST);
  }

  //first interview pdf printing
  @GetMapping( "/firstInterviewPdf/{id}" )
  public String firstInterviewPdf(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("pdfFile", MvcUriComponentsBuilder
        .fromMethodName(InterviewManageController.class, "pdfPrint", id, ApplicantGazetteStatus.FST)
        .toUriString());
    model.addAttribute("redirectUrl", MvcUriComponentsBuilder
        .fromMethodName(InterviewManageController.class, "firstInterview",id, ApplicantGazetteStatus.FST)
        .toUriString());
    return "print/pdfSilentPrint";
  }


  //first interview result enter
  @GetMapping( "/firstResult/{id}/{date}" )
  public String firstInterviewResult(@PathVariable( "id" ) Integer id,
                                     @PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date
      , Model model, RedirectAttributes redirectAttributes) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    Interview interview = interviewService.findByInterviewName(InterviewName.FIRST);
    if ( commonApplicantGazetteMethod(date, model, redirectAttributes, applicantGazette, interview) )
      return "redirect:/interviewManage/firstInterview/" + applicantGazette.getGazette().getId();
    List< PassFailed > passFaileds = new ArrayList<>();
    passFaileds.add(PassFailed.PASS);
    passFaileds.add(PassFailed.FAILED);
    model.addAttribute("passFaileds", passFaileds);
    return "interviewSchedule/addApplicantInterviewResult";
  }


  @GetMapping( "/absent/firstResult/{id}" )
  public String firstAbsentInterviewResult(@PathVariable( "id" ) Integer id) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.FSTR);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/interviewManage/firstInterview/" + applicantGazette.getGazette().getId();
  }

  //first and second interview  result save
  @PostMapping( "/firstSecondResult/save" )
  public String firstInterviewResultSave(@ModelAttribute ApplicantGazetteInterview applicantGazetteInterview,
                                         BindingResult bindingResult, Model model) {
    ApplicantGazetteInterview applicantGazetteInterviewDb =
        applicantGazetteInterviewService.findById(applicantGazetteInterview.getId());
    Applicant applicant =
        applicantService.findById(applicantGazetteInterviewDb.getApplicantGazette().getApplicant().getId());
    Interview interview =
        interviewService.findById(applicantGazetteInterviewDb.getInterviewBoard().getInterview().getId());
    if ( bindingResult.hasErrors() ) {
      model.addAttribute("applicantDetail", applicant);
      model.addAttribute("addStatus", true);
      model.addAttribute("age", dateTimeAgeService.getAge(applicant.getDateOfBirth()));
      model.addAttribute("files", applicantFilesService.applicantFileDownloadLinks(applicant));
      model.addAttribute("interviews", interview);
      model.addAttribute("applicantGazetteInterview", applicantGazetteInterview);
      List< PassFailed > passFaileds = new ArrayList<>();
      passFaileds.add(PassFailed.PASS);
      passFaileds.add(PassFailed.FAILED);
      model.addAttribute("passFaileds", passFaileds);
      return "interviewSchedule/addApplicantInterviewResult";
    }

    applicantGazetteInterview.getApplicantGazetteInterviewResults().forEach(applicantGazetteInterviewResultService::persist);
    applicantGazetteInterviewDb = applicantGazetteInterviewService.persist(applicantGazetteInterview);
    ApplicantGazette applicantGazette =
        applicantGazetteService.findById(applicantGazetteInterviewDb.getApplicantGazette().getId());
    //if interview result failed of pass check
    ApplicantGazetteStatus applicantGazetteStatus;
    if ( interview.getInterviewName().equals(InterviewName.FIRST) ) {
      return "redirect:/interviewManage/firstInterview/" + applicantGazetteInterviewDb.getApplicantGazette().getGazette().getId();
    } else {

      if ( interview.getInterviewName().equals(InterviewName.SECOND) ) {
        if ( applicantGazetteInterviewDb.getPassFailed().equals(PassFailed.FAILED) ) {
          applicantGazetteStatus = ApplicantGazetteStatus.SNDR;
        } else {
          applicantGazetteStatus = ApplicantGazetteStatus.SNDP;
        }
      }
      if ( interview.getInterviewName().equals(InterviewName.THIRD) ) {
        if ( applicantGazetteInterviewDb.getPassFailed().equals(PassFailed.FAILED) ) {
          applicantGazetteStatus = ApplicantGazetteStatus.TNDR;
        } else {
          applicantGazetteStatus = ApplicantGazetteStatus.FSTP;
        }
      }
      if ( interview.getInterviewName().equals(InterviewName.FOURTH) ) {
        if ( applicantGazetteInterviewDb.getPassFailed().equals(PassFailed.FAILED) ) {
          applicantGazetteStatus = ApplicantGazetteStatus.FTHR;
        } else {
          applicantGazetteStatus = ApplicantGazetteStatus.FTHP;
          Gazette gazette = gazetteService.findById(applicantGazette.getGazette().getId());
          gazette.setGazetteStatus(GazetteStatus.CL);
          gazetteService.persist(gazette);
        }
        applicantGazette.setApplicantGazetteStatus(applicantGazetteStatus);
        applicantGazetteService.persist(applicantGazette);
      }

      return "redirect:/interviewManage/secondInterview/" + applicantGazetteInterviewDb.getApplicantGazette().getGazette().getId();
    }

  }

  //first and second interview common pdf
  @GetMapping( value = "/file/{id}/{applicantGazetteStatus}", produces = MediaType.APPLICATION_PDF_VALUE )
  public ResponseEntity< InputStreamResource > pdfPrint(@PathVariable( "id" ) Integer id, @PathVariable(
      "applicantGazetteStatus" ) ApplicantGazetteStatus applicantGazetteStatus) throws Exception {
    var headers = new HttpHeaders();

    headers.add("Content-Disposition", "inline; filename=interview.pdf");
    InputStreamResource pdfFile = new InputStreamResource(applicantService.createPDF(id, applicantGazetteStatus));
    return ResponseEntity
        .ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfFile);
  }


  //second interview gazette
  @GetMapping( "/secondInterview/{id}" )
  public String secondInterview(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    return commonThing(model,
                       applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.SND,
                                                                                      gazette), "Second " +
                           "Interview",
                       "secondInterviewPdf", "Second Interview Pdf",
                       "secondInterviewExcel", "Second Interview Excel", true, "secondResult",
                       ApplicantGazetteStatus.SND);

  }

  //second interview pdf printing
  @GetMapping( "/secondInterviewPdf/{id}" )
  public String secondInterviewPdf(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("pdfFile", MvcUriComponentsBuilder
        .fromMethodName(InterviewManageController.class, "pdfPrint", id, ApplicantGazetteStatus.SND)
        .toUriString());
    model.addAttribute("redirectUrl", MvcUriComponentsBuilder
        .fromMethodName(InterviewManageController.class, "secondInterview", id,ApplicantGazetteStatus.SND)
        .toUriString());
    return "print/pdfSilentPrint";
  }

  //second interview result enter
  @GetMapping( "/secondResult/{id}/{date}" )
  public String secondInterviewResult(@PathVariable( "id" ) Integer id,
                                      @PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date
      , Model model, RedirectAttributes redirectAttributes) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    Interview interview = interviewService.findByInterviewName(InterviewName.SECOND);
    return commonResultEnterPart(date, model, redirectAttributes, applicantGazette, interview);
  }

  @GetMapping( "/absent/secondResult/{id}" )
  public String secondAbsentInterviewResult(@PathVariable( "id" ) Integer id) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.SNDR);
    applicantGazetteService.persist(applicantGazette);
    return "redirect:/interviewManage/firstInterview/" + applicantGazette.getGazette().getId();
  }

  @GetMapping( "/thirdInterview/{id}" )
  public String thirdInterview(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    return commonThing(model,
                       applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.TND,
                                                                                      gazette), "Third " +
                           "Interview",
                       null, null,
                       "thirdInterviewExcel", "Third Interview Excel", false, "thirdResult",
                       ApplicantGazetteStatus.TND);
  }

  @GetMapping( "/thirdResult/{id}/{date}" )
  public String thirdInterviewResult(@PathVariable( "id" ) Integer id,
                                     @PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date
      , Model model, RedirectAttributes redirectAttributes) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    Interview interview = interviewService.findByInterviewName(InterviewName.THIRD);
    return commonResultEnterPart(date, model, redirectAttributes, applicantGazette, interview);
  }

  @GetMapping( "/fourthInterview/{id}" )
  public String fourthInterview(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);
    return commonThing(model,
                       applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.FTH,
                                                                                      gazette), "Fourth " +
                           "Interview",
                       null, null,
                       "fourthInterviewExcel", "Fourth Interview Excel", false, "fourthResult",
                       ApplicantGazetteStatus.FTH);
  }

  @GetMapping( "/fourthResult/{id}/{date}" )
  public String fourthInterviewResult(@PathVariable( "id" ) Integer id,
                                      @PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date
      , Model model, RedirectAttributes redirectAttributes) {
    ApplicantGazette applicantGazette = applicantGazetteService.findById(id);
    Interview interview = interviewService.findByInterviewName(InterviewName.FOURTH);
    return commonResultEnterPart(date, model, redirectAttributes, applicantGazette, interview);
  }

  private String commonResultEnterPart(@DateTimeFormat( pattern = "yyyy-MM-dd" ) @PathVariable( "date" ) LocalDate
                                           date, Model model, RedirectAttributes redirectAttributes,
                                       ApplicantGazette applicantGazette, Interview interview) {
    if ( commonApplicantGazetteMethod(date, model, redirectAttributes, applicantGazette, interview) )
      return "redirect:/interviewManage/firstInterview/" + applicantGazette.getGazette().getId();
    model.addAttribute("passFaileds", PassFailed.values());
    return "interviewSchedule/addApplicantInterviewResult";
  }

  private boolean commonApplicantGazetteMethod
      (@PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date, Model model, RedirectAttributes
          redirectAttributes, ApplicantGazette applicantGazette, Interview interview) {
    ApplicantGazetteInterview applicantGazetteInterview =
        applicantGazetteInterviewService.findByApplicantGazetteAndApplicantGazetteInterviewStatusAndInterviewDate(applicantGazette, ApplicantGazetteInterviewStatus.ACT, date);
    if ( applicantGazetteInterview == null ) {
      redirectAttributes.addFlashAttribute("message",
                                           "There is no interview on you provided date " + date.toString());
      return true;
    }


    model.addAttribute("applicantDetail", applicantGazette.getApplicant());
    model.addAttribute("addStatus", true);
    model.addAttribute("age", dateTimeAgeService.getAge(applicantGazette.getApplicant().getDateOfBirth()));
    model.addAttribute("files", applicantFilesService.applicantFileDownloadLinks(applicantGazette.getApplicant()));
    model.addAttribute("interviews", interview);
    model.addAttribute("applicantGazetteInterview", applicantGazetteInterview);
    return false;
  }

  @GetMapping( "/cidcrdsis/{id}" )
  public String cidCRDSIS(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("applicantGazettes", applicantGazetteService.findByGazette(gazetteService.findById(id)));
//form action
    model.addAttribute("formAction", "cidcrdsisResult");
    model.addAttribute("gazetteId", id);
    //cid
    model.addAttribute("uriCID", "CID/" + id);
    model.addAttribute("btnTextCID", "Get CID Excel");
    model.addAttribute("internalDivisionCID", InternalDivision.CID);
    //sis
    model.addAttribute("uriSIS", "SIS/" + id);
    model.addAttribute("btnTextSIS", "Get SIS Excel");
    model.addAttribute("internalDivisionSIS", InternalDivision.SIS);
    //crd
    model.addAttribute("uriCRD", "CRD/" + id);
    model.addAttribute("btnTextCRD", "Get CRD Excel");
    model.addAttribute("internalDivisionCRD", InternalDivision.CRD);
    return "interviewSchedule/interviewCIDSISCRD";
  }

  //todo :
  @PostMapping( "/cidcrdsisResult" )
  public String saveResult(@ModelAttribute ApplicantGazetteSisCrdCid applicantGazetteSisCrdCid,
                           RedirectAttributes redirectAttributes) throws IOException {


    HSSFWorkbook workbook = new HSSFWorkbook(applicantGazetteSisCrdCid.getMultipartFile().getInputStream());
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
      return "redirect:/interviewManage/cidcrdsis/" + applicantGazetteSisCrdCid.getGazetteId();
    }
    for ( int i = 0; i <= worksheet.getLastRowNum(); i++ ) {
      HSSFRow row = worksheet.getRow(i);
      if ( i == 0 ) {
        if ( !row.getCell(3).getRichStringCellValue().toString().equals("NIC") || !row.getCell(6)
            .getRichStringCellValue().toString().equals("Result") ) {
          redirectAttributes.addFlashAttribute("message", "Some one change the excel sheet please provide valid " +
              "excel" +
              " sheet");
          return "redirect:/interviewManage/cidcrdsis/" + applicantGazetteSisCrdCid.getGazetteId();
        }
      } else {
        ApplicantGazetteSisCrdCid applicantGazetteSisCrdCidToSave = new ApplicantGazetteSisCrdCid();
        //get applicant using NIC
        ApplicantGazette applicantGazette =
            applicantGazetteService.findByCode(row.getCell(1).getRichStringCellValue().getString());
        //get applicant result
        PassFailed passFailed = PassFailed.valueOf(row.getCell(6).getRichStringCellValue().toString());


        // get all applicant Sis Crd Cid result
        List< ApplicantGazetteSisCrdCid > applicantGazetteSisCrdCids =
            applicantGazetteSisCrdCidService.findByApplicantGazette(applicantGazette);

        // get all applicant Sis Crd Cid result size
        if ( applicantGazetteSisCrdCids.size() == 2 ) {
          if ( PassFailed.PASS.equals(passFailed)
              && PassFailed.PASS.equals(applicantGazetteSisCrdCids.get(0).getPassFailed())
              && PassFailed.PASS.equals(applicantGazetteSisCrdCids.get(1).getPassFailed()) ) {
            System.out.println("three values was passed");
            applicantGazetteSisCrdCidToSave.setApplicantGazette(applicantGazette);
            applicantGazetteSisCrdCidToSave.setPassFailed(passFailed);
            applicantGazetteSisCrdCidToSave.setInternalDivision(internalDivision);
            applicantGazetteSisCrdCidService.persist(applicantGazetteSisCrdCidToSave);
            //all result would be passed applicant status needs to change and applicant is suitable to second
            // interview
            applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.SND);
            applicantGazetteService.persist(applicantGazette);

          } else {
            applicantGazetteSisCrdCidToSave.setApplicantGazette(applicantGazette);
            applicantGazetteSisCrdCidToSave.setPassFailed(passFailed);
            applicantGazetteSisCrdCidToSave.setInternalDivision(internalDivision);
            applicantGazetteSisCrdCidService.persist(applicantGazetteSisCrdCidToSave);
            //all result would be passed applicant status needs to change and applicant is not suitable to second
            // interview
            applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.FSTR);
            applicantGazetteService.persist(applicantGazette);
          }
          // need to validate all result status is pass
        } else {
          System.out.println("applicant gazette " + applicantGazette.getCode());
          if ( !applicantGazetteSisCrdCids.isEmpty() ) {
            System.out.println(" there are applicant ");
            applicantGazetteSisCrdCidToSave =
                applicantGazetteSisCrdCidService.findByApplicantGazetteAndInternalDivision(applicantGazette,
                                                                                           internalDivision);
            if ( applicantGazetteSisCrdCidToSave == null ) {
              applicantGazetteSisCrdCidToSave = new ApplicantGazetteSisCrdCid();
            }
          }

          applicantGazetteSisCrdCidToSave.setApplicantGazette(applicantGazette);
          applicantGazetteSisCrdCidToSave.setPassFailed(passFailed);
          applicantGazetteSisCrdCidToSave.setInternalDivision(internalDivision);
          applicantGazetteSisCrdCidService.persist(applicantGazetteSisCrdCidToSave);
        }
      }
    }
    return "redirect:/interviewManage/cidcrdsis/" + applicantGazetteSisCrdCid.getGazetteId();
  }

}

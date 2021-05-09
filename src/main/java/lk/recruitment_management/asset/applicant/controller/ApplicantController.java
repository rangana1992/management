package lk.recruitment_management.asset.applicant.controller;


import com.twilio.rest.api.v2010.account.Application;
import lk.recruitment_management.asset.ag_office.controller.AgOfficeController;
import lk.recruitment_management.asset.ag_office.service.AgOfficeService;
import lk.recruitment_management.asset.applicant.entity.*;
import lk.recruitment_management.asset.applicant.entity.enums.*;
import lk.recruitment_management.asset.applicant_file.service.ApplicantFilesService;
import lk.recruitment_management.asset.applicant_degree_result.entity.ApplicantDegreeResult;
import lk.recruitment_management.asset.applicant_file.entity.ApplicantFiles;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.applicant_result.entity.ApplicantResult;
import lk.recruitment_management.asset.applicant_result.entity.enums.Attempt;
import lk.recruitment_management.asset.applicant_result.entity.enums.CompulsoryOLSubject;
import lk.recruitment_management.asset.applicant_result.entity.enums.StreamLevel;
import lk.recruitment_management.asset.applicant_result.entity.enums.SubjectResult;
import lk.recruitment_management.asset.common_asset.model.Enum.*;
import lk.recruitment_management.asset.common_asset.model.TwoDate;
import lk.recruitment_management.asset.common_asset.service.CommonService;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.district.controller.DistrictController;
import lk.recruitment_management.asset.district.service.DistrictService;
import lk.recruitment_management.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.recruitment_management.asset.grama_niladhari.service.GramaNiladhariService;
import lk.recruitment_management.asset.non_relative.entity.NonRelative;
import lk.recruitment_management.asset.police_station.controller.PoliceStationController;
import lk.recruitment_management.asset.police_station.Service.PoliceStationService;
import lk.recruitment_management.asset.user_management.entity.User;
import lk.recruitment_management.asset.user_management.service.UserService;
import lk.recruitment_management.util.service.DateTimeAgeService;
import lk.recruitment_management.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Controller
@RequestMapping( "/applicant" )
public class ApplicantController {
  private final ApplicantService applicantService;
  private final ApplicantFilesService applicantFilesService;
  private final DateTimeAgeService dateTimeAgeService;
  private final CommonService commonService;
  private final UserService userService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final DistrictService districtService;
  private final AgOfficeService agOfficeService;
  private final PoliceStationService policeStationService;
  private final GramaNiladhariService gramaNiladhariService;


  @Autowired
  public ApplicantController(ApplicantService applicantService, ApplicantFilesService applicantFilesService,
                             DateTimeAgeService dateTimeAgeService, CommonService commonService,
                             UserService userService, MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                             DistrictService districtService,
                             AgOfficeService agOfficeService, PoliceStationService policeStationService,
                             GramaNiladhariService gramaNiladhariService) {
    this.applicantService = applicantService;
    this.applicantFilesService = applicantFilesService;

    this.dateTimeAgeService = dateTimeAgeService;
    this.commonService = commonService;
    this.userService = userService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.districtService = districtService;

    this.agOfficeService = agOfficeService;
    this.policeStationService = policeStationService;
    this.gramaNiladhariService = gramaNiladhariService;
  }
//----> Applicant details management - start <----//

  // Common things for an applicant add and update
  private void commonThings(Model model) {
    model.addAttribute("title", Title.values());
    model.addAttribute("genders", Gender.values());
    model.addAttribute("applyingRanks", ApplyingRank.values());
    model.addAttribute("applicantStatus", ApplicantStatus.values());
    model.addAttribute("civilStatuses", CivilStatus.values());
    model.addAttribute("nationalities", Nationality.values());
    model.addAttribute("bloodGroup", BloodGroup.values());
    model.addAttribute("provinces", Province.values());
    model.addAttribute("districtURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                           .toUriString());
    model.addAttribute("agOfficeURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(AgOfficeController.class, "getAgOfficeByDistrict", "")
                           .toUriString());
    model.addAttribute("policeStationURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(PoliceStationController.class, "getPoliceStationByAgOffice", "")
                           .toUriString());
    model.addAttribute("gramaNiladariURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(GramaNiladhariController.class, "getGramaNiladhariByPolice", "")
                           .toUriString());
    model.addAttribute("attempts", Attempt.values());
    model.addAttribute("streamLevels", StreamLevel.values());
    model.addAttribute("compulsoryOLSubjects", CompulsoryOLSubject.values());
    model.addAttribute("subjectResults", SubjectResult.values());

  }

  //When scr called file will send to
  @GetMapping( "/file/{filename}" )
  public ResponseEntity< byte[] > downloadFile(@PathVariable( "filename" ) String filename) {
    ApplicantFiles file = applicantFilesService.findByNewID(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
        .body(file.getPic());
  }

  //Send all applicant data
  @RequestMapping
  public String applicantPage(Model model) {
    return commonApplicant(model, applicantService.findAll());
  }

  //Send on applicant details
  @GetMapping( value = "/{id}" )
  public String applicantView(@PathVariable( "id" ) Integer id, Model model) {
    Applicant applicant = applicantService.findById(id);
    model.addAttribute("applicantDetail", applicant);
    model.addAttribute("addStatus", false);
    model.addAttribute("age", dateTimeAgeService.getAge(applicant.getDateOfBirth()));
    model.addAttribute("files", applicantFilesService.applicantFileDownloadLinks(applicant));
    return "applicant/applicant-detail";
  }

  //Send applicant data edit
  @GetMapping( value = "/edit/{id}" )
  public String editApplicantForm(@PathVariable( "id" ) Integer id, Model model) {
    Applicant applicant = applicantService.findById(id);
    model.addAttribute("applicant", applicant);
    model.addAttribute("addStatus", false);
    model.addAttribute("file", applicantFilesService.applicantFileDownloadLinks(applicant));
    //district list
    model.addAttribute("districts", districtService.findAll());
    //ag office list
    model.addAttribute("agOffices", agOfficeService.findAll());
    //police station list url
    model.addAttribute("policeStations", policeStationService.findAll());
    //gramaniladari division list url
    model.addAttribute("gramaNiladharis", gramaNiladhariService.findAll());
    commonThings(model);
    return "applicant/editApplicant";
  }

  //Send an applicant add form
  @GetMapping( value = {"/add"} )
  public String applicantAddForm(Model model) {
    Applicant applicant = new Applicant();
    applicant.setEmail(SecurityContextHolder.getContext().getAuthentication().getName());

    model.addAttribute("addStatus", true);
    model.addAttribute("applicant", applicant);
    commonThings(model);
    return "applicant/addApplicant";
  }

  //Applicant add and update
  @PostMapping( value = {"/save", "/update"} )
  public String addApplicant(@Valid @ModelAttribute Applicant applicant, BindingResult result, Model model
                            ) {
    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("applicant", applicant);
      commonThings(model);
      return "applicant/addApplicant";
    }
    if ( applicant.getId() == null ) {
      Applicant lastApplicant = applicantService.lastApplicant();
      if ( lastApplicant == null ) {
        applicant.setCode("SLPA" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        applicant.setCode("SLPA" + makeAutoGenerateNumberService.numberAutoGen(lastApplicant.getCode().substring(4)).toString());
      }
    }


    try {
      applicant.setMobile(commonService.commonMobileNumberLengthValidator(applicant.getMobile()));
      applicant.setLand(commonService.commonMobileNumberLengthValidator(applicant.getLand()));
//
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      applicant.setEmail(authentication.getName());
      //set no relative to applicant
      if ( !applicant.getNonRelatives().isEmpty() ) {
        List< NonRelative > relatives = new ArrayList<>();
        applicant.getNonRelatives().forEach(x -> {
          x.setApplicant(applicant);
          relatives.add(x);
        });
        applicant.setNonRelatives(relatives);
      }
      //set degree result to applicant
      if ( !applicant.getApplicantDegreeResults().isEmpty() ) {
        List< ApplicantDegreeResult > applicantDegreeResults = new ArrayList<>();
        applicant.getApplicantDegreeResults().forEach(x -> {
          x.setApplicant(applicant);
          applicantDegreeResults.add(x);
        });
        applicant.setApplicantDegreeResults(applicantDegreeResults);
      }
      //applicant result
      if ( !applicant.getApplicantResults().isEmpty() ) {
        List< ApplicantResult > applicantResults = new ArrayList<>();
        applicant.getApplicantResults().forEach(x -> {
          if ( x.getSubjectResult() != null ) {
            x.setApplicant(applicant);
            applicantResults.add(x);
          }
        });
        applicant.setApplicantResults(applicantResults);
      }

      //after save applicant files and save applicant
      Applicant savedApplicant = applicantService.persist(applicant);

      //save applicant images file
      if ( applicant.getFile().getOriginalFilename() != null ) {
        ApplicantFiles applicantFiles = applicantFilesService.findByApplicant(savedApplicant);
        if ( applicantFiles != null ) {
          // update new contents
          applicantFiles.setPic(applicant.getFile().getBytes());
          // Save all to database
        } else {
          applicantFiles = new ApplicantFiles(applicant.getFile().getOriginalFilename(),
                                              applicant.getFile().getContentType(),
                                              applicant.getFile().getBytes(),
                                              applicant.getNic().concat("-" + LocalDateTime.now()),
                                              UUID.randomUUID().toString().concat("applicant"));
          applicantFiles.setApplicant(applicant);
        }
        applicantFilesService.persist(applicantFiles);
      }
      return "redirect:/home";

    } catch ( Exception e ) {
      ObjectError error = new ObjectError("applicant",
                                          "There is already in the system. <br>System message -->" + e.toString());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("applicant", applicant);
commonThings(model);
      return "applicant/addApplicant";
    }
  }

  //If need to applicant {but not applicable for this }
  @GetMapping( value = "/remove/{id}" )
  public String removeApplicant(@PathVariable Integer id) {
    applicantService.delete(id);
    return "redirect:/applicant";
  }

  //To search applicant any giving applicant parameter
  @GetMapping( value = "/search" )
  public String search(Model model, Applicant applicant) {
    model.addAttribute("applicantDetail", applicantService.search(applicant));
    return "applicant/applicant-detail";
  }

  //doto those are need to applicantGazette approve not

//  @GetMapping( value = "/approve/{id}" )
//  public String approve(@PathVariable Integer id) {
//    Applicant applicant = applicantService.findById(id);
//    applicant.setApplicantStatus(ApplicantStatus.A);
//    applicantService.persist(applicant);
//    return "redirect:/applicant";
//
//  }
//
//  @GetMapping( value = "/reject/{id}" )
//  public String reject(@PathVariable Integer id) {
//    Applicant applicant = applicantService.findById(id);
//    applicant.setApplicantStatus(ApplicantStatus.REJ);
//    applicantService.persist(applicant);
//    return "redirect:/applicant";
//  }

  private String commonApplicant(Model model, List< Applicant > applicants) {
    model.addAttribute("applicants", applicants);
    model.addAttribute("contendHeader", "Applicant Registration");
    model.addAttribute("applyingRanks", ApplyingRank.values());
    model.addAttribute("applicantStatuses", ApplicantStatus.values());
    model.addAttribute("addStatus", true);
    return "applicant/applicant";
  }

//  @PostMapping( "/all/search" )
//  public String getAllPaymentToPayBetweenTwoDate(@ModelAttribute TwoDate twoDate, Model model) {
//    return commonApplicant(model,
//                           applicantService.findByCreatedAtIsBetweenAndApplicantStatus(dateTimeAgeService
//                           .dateTimeToLocalDateStartInDay(twoDate.getStartDate()),
//                                                                                                    dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate())
//                             , twoDate.getApplicantStatus()));
//  }
}

package lk.recruitment.management.asset.applicant.controller;


import lk.recruitment.management.asset.ag_office.controller.AgOfficeController;
import lk.recruitment.management.asset.ag_office.service.AgOfficeService;
import lk.recruitment.management.asset.applicant.entity.*;
import lk.recruitment.management.asset.applicant.entity.Enum.*;
import lk.recruitment.management.asset.applicant.service.ApplicantFilesService;
import lk.recruitment.management.asset.common_asset.model.Enum.*;
import lk.recruitment.management.asset.common_asset.service.CommonService;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import lk.recruitment.management.asset.district.controller.DistrictController;
import lk.recruitment.management.asset.district.service.DistrictService;
import lk.recruitment.management.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.recruitment.management.asset.grama_niladhari.service.GramaNiladhariService;
import lk.recruitment.management.asset.police_station.controller.PoliceStationController;
import lk.recruitment.management.asset.police_station.Service.PoliceStationService;
import lk.recruitment.management.asset.user_management.service.UserService;
import lk.recruitment.management.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.w3c.dom.stylesheets.LinkStyle;

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
  private final DistrictService districtService;
  private final AgOfficeService agOfficeService;
  private final PoliceStationService policeStationService;
  private final GramaNiladhariService gramaNiladhariService;


  @Autowired
  public ApplicantController(ApplicantService applicantService, ApplicantFilesService applicantFilesService,
                             DateTimeAgeService dateTimeAgeService, CommonService commonService,
                             UserService userService, DistrictService districtService,
                             AgOfficeService agOfficeService, PoliceStationService policeStationService,
                             GramaNiladhariService gramaNiladhariService) {
    this.applicantService = applicantService;
    this.applicantFilesService = applicantFilesService;

    this.dateTimeAgeService = dateTimeAgeService;
    this.commonService = commonService;
    this.userService = userService;
    this.districtService = districtService;

    this.agOfficeService = agOfficeService;
    this.policeStationService = policeStationService;
    this.gramaNiladhariService = gramaNiladhariService;
  }
//----> Applicant details management - start <----//

  // Common things for an applicant add and update
  private String commonThings(Model model) {
    model.addAttribute("title", Title.values());
    model.addAttribute("gender", Gender.values());
    model.addAttribute("applyingRanks", ApplyingRank.values());
    model.addAttribute("civilStatus", CivilStatus.values());
    model.addAttribute("applicantStatus", ApplicantStatus.values());
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
    return "applicant/addApplicant";
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
    model.addAttribute("applicants",
                       applicantService.findAll()
                           .stream()
                           .filter(x -> x.getApplicantStatus().equals(ApplicantStatus.P) || x.getApplicantStatus().equals(ApplicantStatus.REJ))
                           .collect(Collectors.toList()));
    model.addAttribute("contendHeader", "Applicant Registration");
    return "applicant/applicant";
  }

  //Send on applicant details
  @GetMapping( value = "/{id}" )
  public String applicantView(@PathVariable( "id" ) Integer id, Model model) {
    Applicant applicant = applicantService.findById(id);
    model.addAttribute("applicantDetail", applicant);
    model.addAttribute("addStatus", false);
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
    //police station list
    model.addAttribute("policeStations", policeStationService.findAll());
    //gramaniladari division list
    model.addAttribute("gramaNiladharis", gramaNiladhariService.findAll());
    return commonThings(model);
  }

  //Send an applicant add form
  @GetMapping( value = {"/add"} )
  public String applicantAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("applicant", new Applicant());
    return commonThings(model);
  }

  //Applicant add and update
  @PostMapping( value = {"/save", "/update"} )
  public String addApplicant(@Valid @ModelAttribute Applicant applicant, BindingResult result, Model model
                            ) {

    result.getAllErrors().forEach(System.out::println);
    System.out.println(applicant.getGramaNiladhari().getName());
    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("applicant", applicant);
      return commonThings(model);
    }
    try {
      applicant.setMobile(commonService.commonMobileNumberLengthValidator(applicant.getMobile()));
      applicant.setLand(commonService.commonMobileNumberLengthValidator(applicant.getLand()));

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      applicant.setEmail(authentication.getName());
      //set no relative to applicant
      if ( !applicant.getNonRelatives().isEmpty() ) {
        System.out.println(" non relative");
        List< NonRelative > relatives = new ArrayList<>();
        applicant.getNonRelatives().forEach(x -> {
          x.setApplicant(applicant);
          relatives.add(x);
        });
        applicant.setNonRelatives(relatives);
      }
      //set degree result to applicant
      if ( !applicant.getApplicantDegreeResults().isEmpty() ) {
        System.out.println(" degree result");
        List< ApplicantDegreeResult > applicantDegreeResults = new ArrayList<>();
        applicant.getApplicantDegreeResults().forEach(x -> {
          x.setApplicant(applicant);
          applicantDegreeResults.add(x);
        });
        applicant.setApplicantDegreeResults(applicantDegreeResults);
      }
      //applicant result
      if ( !applicant.getApplicantResults().isEmpty() ) {
        System.out.println(" applicant result");
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
      return "redirect:/applicant/" + savedApplicant.getId();

    } catch ( Exception e ) {
      ObjectError error = new ObjectError("applicant",
                                          "There is already in the system. <br>System message -->" + e.toString());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("applicant", applicant);
      return commonThings(model);
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

  @GetMapping( value = "/approve/{id}" )
  public String approve(@PathVariable Integer id) {
    Applicant applicant = applicantService.findById(id);
    applicant.setApplicantStatus(ApplicantStatus.A);
    applicantService.persist(applicant);
    return "redirect:/applicant";

  }

  @GetMapping( value = "/reject/{id}" )
  public String reject(@PathVariable Integer id) {
    Applicant applicant = applicantService.findById(id);
    applicant.setApplicantStatus(ApplicantStatus.REJ);
    applicantService.persist(applicant);
    return "redirect:/applicant";
  }

}

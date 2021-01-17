package lk.recruitment.management.asset.applicant.controller;


import lk.recruitment.management.asset.ag_office.controller.AgOfficeController;
import lk.recruitment.management.asset.ag_office.service.AgOfficeService;
import lk.recruitment.management.asset.applicant.entity.ApplicantFiles;
import lk.recruitment.management.asset.applicant.entity.Enum.*;
import lk.recruitment.management.asset.applicant.service.ApplicantFilesService;
import lk.recruitment.management.asset.common_asset.model.Enum.*;
import lk.recruitment.management.asset.common_asset.service.CommonService;
import lk.recruitment.management.asset.applicant.entity.Applicant;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;


@Controller
@RequestMapping("/applicant")
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
                               UserService userService, DistrictService districtService, AgOfficeService agOfficeService, PoliceStationService policeStationService, GramaNiladhariService gramaNiladhariService) {
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
    @GetMapping("/file/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String filename) {
        ApplicantFiles file = applicantFilesService.findByNewID(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getPic());
    }

    //Send all applicant data
    @RequestMapping
    public String applicantPage(Model model) {
        model.addAttribute("applicants", applicantService.findAll());
        model.addAttribute("contendHeader", "Applicant Registration");
        return "applicant/applicant";
    }

    //Send on applicant details
    @GetMapping(value = "/{id}")
    public String applicantView(@PathVariable("id") Integer id, Model model) {
        Applicant applicant = applicantService.findById(id);
        model.addAttribute("applicantDetail", applicant);
        model.addAttribute("addStatus", false);
        model.addAttribute("files", applicantFilesService.applicantFileDownloadLinks(applicant));
        return "applicant/applicant-detail";
    }

    //Send applicant data edit
    @GetMapping(value = "/edit/{id}")
    public String editApplicantForm(@PathVariable("id") Integer id, Model model) {
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
    @GetMapping(value = {"/add"})
    public String applicantAddForm(Model model) {
        model.addAttribute("addStatus", true);
        model.addAttribute("applicant", new Applicant());
        return commonThings(model);
    }

    //Applicant add and update
    @PostMapping(value = {"/save", "/update"})
    public String addApplicant(@Valid @ModelAttribute Applicant applicant, BindingResult result, Model model
    ) {

        result.getAllErrors().forEach(System.out::println);
        System.out.println(applicant.getGramaNiladhari().getName());
        if (result.hasErrors()) {
            model.addAttribute("addStatus", true);
            model.addAttribute("applicant", applicant);
            return commonThings(model);
        }
        try {
            applicant.setMobile(commonService.commonMobileNumberLengthValidator(applicant.getMobile()));
            applicant.setLand(commonService.commonMobileNumberLengthValidator(applicant.getLand()));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            applicant.setEmail(authentication.getName());
            //after save applicant files and save applicant
            Applicant savedApplicant = applicantService.persist(applicant);


            //save applicant images file
            if (applicant.getFile().getOriginalFilename() != null) {
                ApplicantFiles applicantFiles = applicantFilesService.findByApplicant(savedApplicant);
                if (applicantFiles != null) {
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
            return "redirect:/applicant";

        } catch (Exception e) {
            ObjectError error = new ObjectError("applicant",
                    "There is already in the system. <br>System message -->" + e.toString());
            result.addError(error);
            model.addAttribute("addStatus", true);
            model.addAttribute("applicant", applicant);
            return commonThings(model);
        }
    }

    //If need to applicant {but not applicable for this }
    @GetMapping(value = "/remove/{id}")
    public String removeApplicant(@PathVariable Integer id) {
        applicantService.delete(id);
        return "redirect:/applicant";
    }

    //To search applicant any giving applicant parameter
    @GetMapping(value = "/search")
    public String search(Model model, Applicant applicant) {
        model.addAttribute("applicantDetail", applicantService.search(applicant));
        return "applicant/applicant-detail";
    }

//----> Applicant details management - end <----//
    //````````````````````````````````````````````````````````````````````````````//
//----> ApplicantWorkingPlace - details management - start <----//

    //Send form to add working place before find applicant
    @GetMapping(value = "/workingPlace")
    public String addApplicantWorkingPlaceForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        model.addAttribute("applicantDetailShow", false);
        return "applicantWorkingPlace/addApplicantWorkingPlace";
    }

    //Send a searched applicant to add working place
/*
    @PostMapping( value = "/workingPlace" )
    public String addWorkingPlaceApplicantDetails(@ModelAttribute( "applicant" ) Applicant applicant, Model model) {

        List< Applicant > applicants = applicantService.search(applicant);
        if ( applicants.size() == 1 ) {
            model.addAttribute("applicantDetailShow", true);
            model.addAttribute("applicantNotFoundShow", false);
            model.addAttribute("applicantDetail", applicants.get(0));
            model.addAttribute("files", applicantFilesService.applicantFileDownloadLinks(applicant).get(0));
            model.addAttribute("applicantWorkingPlaceHistoryObject", new ApplicantWorkingPlaceHistory());
            model.addAttribute("workingPlaceChangeReason", WorkingPlaceChangeReason.values());
            model.addAttribute("province", Province.values());
            model.addAttribute("districtUrl", MvcUriComponentsBuilder
                    .fromMethodName(WorkingPlaceRestController.class, "getDistrict", "")
                    .build()
                    .toString());
            model.addAttribute("stationUrl", MvcUriComponentsBuilder
                    .fromMethodName(WorkingPlaceRestController.class, "getStation", "")
                    .build()
                    .toString());
            return "applicantWorkingPlace/addApplicantWorkingPlace";
        }
        model.addAttribute("applicant", new Applicant());
        model.addAttribute("applicantDetailShow", false);
        model.addAttribute("applicantNotFoundShow", true);
        model.addAttribute("applicantNotFound", "There is not applicant in the system according to the provided details" +
                " \n Could you please search again !!");

        return "applicantWorkingPlace/addApplicantWorkingPlace";
    }

    @PostMapping( value = "/workingPlace/add" )
    public String addWorkingPlaceApplicant(@ModelAttribute( "applicantWorkingPlaceHistory" ) ApplicantWorkingPlaceHistory applicantWorkingPlaceHistory, Model model) {
        System.out.println(applicantWorkingPlaceHistory.toString());
        // -> need to write validation before the save working place
        //before saving set applicant current working palace
        WorkingPlace workingPlace = applicantWorkingPlaceHistory.getWorkingPlace();

        applicantWorkingPlaceHistory.setWorkingPlace(applicantWorkingPlaceHistory.getApplicant().getWorkingPlace());
        applicantWorkingPlaceHistory.getApplicant().setWorkingPlace(workingPlace);

        applicantWorkingPlaceHistory.setWorkingDuration(dateTimeAgeService.dateDifference(applicantWorkingPlaceHistory.getFrom_place(), applicantWorkingPlaceHistory.getTo_place()));
        applicantWorkingPlaceHistoryService.persist(applicantWorkingPlaceHistory);
        return "redirect:/applicant";
    }
*/

//----> ApplicantWorkingPlace - details management - end <----//

}
/*
 try {
            List< FileModel > storedFile = new ArrayList< FileModel >();

            for ( MultipartFile file : files ) {
                FileModel fileModel = fileRepository.findByName(file.getOriginalFilename());
                if ( fileModel != null ) {
                    // update new contents
                    fileModel.setPic(file.getBytes());
                } else {
                    fileModel = new FileModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
                }

                fileNames.add(file.getOriginalFilename());
                storedFile.add(fileModel);
            }

            // Save all Files to database
            fileRepository.saveAll(storedFile);

            model.addAttribute("message", "Files uploaded successfully!");
            model.addAttribute("files", fileNames);
        } catch ( Exception e ) {
            model.addAttribute("message", "Fail!");
            model.addAttribute("files", fileNames);
        }

* */

/*
 public String addApplicant(@Valid @ModelAttribute Applicant applicant, BindingResult result, Model model,
 RedirectAttributes redirectAttributes) {

        * String newApplicantNumber = "";
        String input;
        if (applicantService.lastApplicant() != null) {
            input = applicantService.lastApplicant().getNumber();
            int applicantNumber = Integer.valueOf(input.replaceAll("[^0-9]+", "")).intValue() + 1;

            if ((applicantNumber < 10) && (applicantNumber > 0)) {
                newApplicantNumber = "KL000" + applicantNumber;
            }
            if ((applicantNumber < 100) && (applicantNumber > 10)) {
                newApplicantNumber = "KL00" + applicantNumber;
            }
            if ((applicantNumber < 1000) && (applicantNumber > 100)) {
                newApplicantNumber = "KL0" + applicantNumber;
            }
            if (applicantNumber > 10000) {
                newApplicantNumber = "KL" + applicantNumber;
            }
        } else {
            newApplicantNumber = "KL0001";
            input = "KL0000";
        }


        if (dateTimeAgeService.getAge(applicant.getDateOfBirth()) < 18) {
            ObjectError error = new ObjectError("dateOfBirth", "Applicant must be 18 old ");
            result.addError(error);
        }
        if (result.hasErrors()) {
                System.out.println("i m here");
                model.addAttribute("addStatus", true);
            if (applicantService.lastApplicant() != null) {
                model.addAttribute("lastApplicant", applicantService.lastApplicant().getPayRoleNumber());
            }


                model.addAttribute("addStatus", true);
                CommonThings(model);
                redirectAttributes.addFlashAttribute("applicant", applicant);
                redirectAttributes.addFlashAttribute("files", applicant.getFiles());
                return "applicant/addApplicant";
                }

      if (applicantService.isApplicantPresent(applicant)) {
            System.out.println("already on ");
            User user = userService.findById(userService.findByApplicantId(applicant.getId()));
            if (applicant.getApplicantStatus() != ApplicantStatus.WORKING) {
                user.setEnabled(false);
                applicantService.persist(applicant);
            }
            System.out.println("update working");
            user.setEnabled(true);
            applicantService.persist(applicant);
            return "redirect:/applicant";
        }
        if (applicant.getId() != null) {
            redirectAttributes.addFlashAttribute("message", "Successfully Add but Email was not sent.");
            redirectAttributes.addFlashAttribute("alertStatus", false);

            applicantService.persist(applicant);
        }


        System.out.println("save no id");
                System.out.println("Applicant come "+applicant.toString());
                //applicantService.persist(applicant);
                return "redirect:/applicant";
                }

 */
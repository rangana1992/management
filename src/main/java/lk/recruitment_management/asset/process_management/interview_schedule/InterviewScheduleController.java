package lk.recruitment_management.asset.process_management.interview_schedule;

import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.enums.ApplicantGazetteInterviewStatus;
import lk.recruitment_management.asset.applicant_gazette_interview.service.ApplicantGazetteInterviewService;
import lk.recruitment_management.asset.common_asset.model.InterviewSchedule;
import lk.recruitment_management.asset.common_asset.model.InterviewScheduleList;
import lk.recruitment_management.asset.district.entity.District;
import lk.recruitment_management.asset.district.service.DistrictService;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.interview_board.entity.InterviewBoard;
import lk.recruitment_management.asset.interview_board.entity.enums.InterviewBoardStatus;
import lk.recruitment_management.asset.interview_board.service.InterviewBoardService;
import lk.recruitment_management.util.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/interviewSchedule" )
public class InterviewScheduleController {
  private final ApplicantService applicantService;
  private final InterviewBoardService interviewBoardService;
  private final GazetteService gazetteService;
  private final ApplicantGazetteInterviewService applicantGazetteInterviewService;
  private final ApplicantGazetteService applicantGazetteService;
  private final EmailService emailService;

  private final DistrictService districtService;

  public InterviewScheduleController(ApplicantService applicantService, InterviewBoardService interviewBoardService,
                                     GazetteService gazetteService,
                                     ApplicantGazetteInterviewService applicantGazetteInterviewService,
                                     ApplicantGazetteService applicantGazetteService, EmailService emailService, DistrictService districtService) {
    this.applicantService = applicantService;
    this.interviewBoardService = interviewBoardService;
    this.gazetteService = gazetteService;
    this.applicantGazetteInterviewService = applicantGazetteInterviewService;
    this.applicantGazetteService = applicantGazetteService;
    this.emailService = emailService;
    this.districtService = districtService;
  }


  @GetMapping( "/add" )
  public String add(Model model) {
    List< Gazette > gazettes =
        gazetteService.findAll().stream().filter(x -> x.getGazetteStatus().equals(GazetteStatus.AC)).collect(Collectors.toList());

    model.addAttribute("gazettes", gazettes);
    return "interviewSchedule/addNewInterviewSchedule";
  }

  @GetMapping( "/add/{id}" )
  public String form(@PathVariable( "id" ) Integer id, Model model) {
    Gazette gazette = gazetteService.findById(id);

    model.addAttribute("totalApplicantCount",
                       applicantGazetteService.countByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.A,
                                                                                       gazette));
    model.addAttribute("interviewBoard", interviewBoardService.findAll()
        .stream()
        .filter(x -> x.getInterviewBoardStatus().equals(InterviewBoardStatus.ACT))
        .collect(Collectors.toList()));
    List< ApplicantGazetteStatus > applicantGazetteStatuses = new ArrayList<>();
    applicantGazetteStatuses.add(ApplicantGazetteStatus.FST);
    applicantGazetteStatuses.add(ApplicantGazetteStatus.SND);
    applicantGazetteStatuses.add(ApplicantGazetteStatus.TND);
    applicantGazetteStatuses.add(ApplicantGazetteStatus.FTH);
    model.addAttribute("gazette", gazette);
    model.addAttribute("interviewBoardNumbers", applicantGazetteStatuses);
    model.addAttribute("interviewSchedule", new InterviewSchedule());
    return "interviewSchedule/addInterviewSchedule";
  }

  @PostMapping
  public String dateCount(@ModelAttribute InterviewSchedule interviewSchedule, Model model) {
    Gazette gazette = gazetteService.findById(interviewSchedule.getId());
    List< ApplicantGazette > applicantGazettes = new ArrayList<>();

    for ( District district : districtService.findAll() ) {
      for ( ApplicantGazette applicantGazette :
          applicantGazetteService.findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus.A,
                                                                         gazette) ) {
        if ( applicantGazette.getApplicant().getGramaNiladhari().getPoliceStation().getAgOffice().getDistrict().equals(district) ) {
          applicantGazettes.add(applicantGazette);
        }
      }
    }
    int startCount = 0;
    for ( InterviewScheduleList interviewScheduleList : interviewSchedule.getInterviewScheduleLists() ) {
      int endCount = interviewScheduleList.getCount() ;
      InterviewBoard interviewBoard = interviewBoardService.findById(interviewScheduleList.getInterviewBoardId());
      interviewBoard.setMessage(interviewSchedule.getMassage());

      HashSet< String > emails = new HashSet<>();
      for ( ApplicantGazette applicantGazette : applicantGazettes.subList(startCount, endCount) ) {
        applicantGazette.setApplicantGazetteStatus(interviewSchedule.getInterviewNumber());
        //new applicant interview
        ApplicantGazetteInterview applicantGazetteInterview = new ApplicantGazetteInterview();

        applicantGazetteInterview.setInterviewBoard(interviewBoard);
        ApplicantGazette applicantGazetteDB = applicantGazetteService.persist(applicantGazette);
        emails.add(applicantGazetteDB.getApplicant().getEmail());
        //save  applicant and set to applicant interview
        applicantGazetteInterview.setApplicantGazette(applicantGazetteDB);
        applicantGazetteInterview.setInterviewDate(interviewScheduleList.getDate());
        applicantGazetteInterview.setApplicantGazetteInterviewStatus(ApplicantGazetteInterviewStatus.ACT);
        applicantGazetteInterviewService.persist(applicantGazetteInterview);
      }
      interviewBoard = interviewBoardService.persist(interviewBoard);
      for ( String email : emails ) {
        emailService.sendEmail(email, "Regarding " + interviewBoard.getName(), interviewSchedule.getMassage());
      }
      startCount = endCount - 1;
    }

    gazette.setGazetteStatus(GazetteStatus.IN);
    gazetteService.persist(gazette);

    return "redirect:/interviewSchedule/add";
  }

  @GetMapping( "/deactivate/{id}" )
  public String deactivate(@PathVariable Integer id, Model model) {

    ApplicantGazetteInterview applicantGazetteInterview = applicantGazetteInterviewService.findById(id);


    ApplicantGazette applicantGazette = applicantGazetteInterview.getApplicantGazette();
    applicantGazette.setApplicantGazetteStatus(ApplicantGazetteStatus.A);
    applicantGazetteService.persist(applicantGazette);

    applicantGazetteInterview.setApplicantGazetteInterviewStatus(ApplicantGazetteInterviewStatus.CL);
    applicantGazetteInterviewService.persist(applicantGazetteInterview);

    model.addAttribute("applicantInterviews",
                       applicantGazetteInterviewService.findAll()
                           .stream()
                           .filter(x -> x.getApplicantGazetteInterviewStatus().equals(ApplicantGazetteInterviewStatus
                                                                                          .ACT))
                           .collect(Collectors.toList()));
    return "redirect:/interviewSchedule/add";
  }

}

package lk.recruitment.management.asset.process_management;

import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import lk.recruitment.management.asset.applicant_interview.entity.ApplicantInterview;
import lk.recruitment.management.asset.applicant_interview.entity.enums.ApplicantInterviewStatus;
import lk.recruitment.management.asset.applicant_interview.service.ApplicantInterviewService;
import lk.recruitment.management.asset.common_asset.model.InterviewSchedule;
import lk.recruitment.management.asset.common_asset.model.InterviewScheduleList;
import lk.recruitment.management.asset.district.entity.District;
import lk.recruitment.management.asset.district.service.DistrictService;
import lk.recruitment.management.asset.interview_board.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.asset.interview_board.service.InterviewBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/interviewSchedule" )
public class InterviewScheduleController {
  private final ApplicantService applicantService;
  private final InterviewBoardService interviewBoardService;
  private final ApplicantInterviewService applicantInterviewService;

  private final DistrictService districtService;

  public InterviewScheduleController(ApplicantService applicantService, InterviewBoardService interviewBoardService,
                                     ApplicantInterviewService applicantInterviewService,
                                     DistrictService districtService) {
    this.applicantService = applicantService;
    this.interviewBoardService = interviewBoardService;
    this.applicantInterviewService = applicantInterviewService;
    this.districtService = districtService;
  }

  @GetMapping
  public String form(Model model) {
    model.addAttribute("totalApplicantCount", applicantService.countByApplicantStatus(ApplicantStatus.A));
    model.addAttribute("interviewBoard", interviewBoardService.findAll().
                           stream()
                           .filter(x -> x.getInterviewBoardStatus().equals(InterviewBoardStatus.ACT))
                           .collect(Collectors.toList())
                      );
    List< ApplicantStatus > applicantStatuses = new ArrayList<>();
    applicantStatuses.add(ApplicantStatus.FST);
    applicantStatuses.add(ApplicantStatus.SND);
    applicantStatuses.add(ApplicantStatus.TND);
    applicantStatuses.add(ApplicantStatus.FTH);
    model.addAttribute("interviewBoardNumbers", applicantStatuses);
    model.addAttribute("interviewSchedule", new InterviewSchedule());
    return "interviewSchedule/addInterviewSchedule";
  }

  @PostMapping
  public String dateCount(@ModelAttribute InterviewSchedule interviewSchedule, Model model) {

    List< Applicant > applicants = new ArrayList<>();

    for ( District district : districtService.findAll() ) {
      for ( Applicant applicant : applicantService.findByApplicantStatus(ApplicantStatus.A) ) {
        if ( applicant.getGramaNiladhari().getPoliceStation().getAgOffice().getDistrict().equals(district) ) {
          applicants.add(applicant);
        }
      }
    }
    int startCount = 0;
    for ( InterviewScheduleList interviewScheduleList : interviewSchedule.getInterviewScheduleLists() ) {
      int endCount = interviewScheduleList.getCount() + 1;

      for ( Applicant applicant : applicants.subList(startCount, endCount) ) {
        applicant.setApplicantStatus(interviewSchedule.getInterviewNumber());
        //new applicant interview
        ApplicantInterview applicantInterview = new ApplicantInterview();

        applicantInterview.setInterviewBoard(interviewBoardService.findById(interviewScheduleList.getInterviewBoardId()));
        //save  applicant and set to applicant interview
        applicantInterview.setApplicant(applicantService.persist(applicant));
        applicantInterview.setInterviewDate(interviewScheduleList.getDate());
        applicantInterview.setApplicantInterviewStatus(ApplicantInterviewStatus.ACT);
        applicantInterviewService.persist(applicantInterview);
      }

      startCount = endCount - 1;
    }

    model.addAttribute("applicantInterviews",
                       applicantInterviewService.findAll().stream().filter(x -> x.getApplicantInterviewStatus().equals(ApplicantInterviewStatus.ACT)).collect(Collectors.toList()));
    return "interviewSchedule/interviewSchedule";
  }

  @GetMapping( "/deactivate/{id}" )
  public String deactivate(@PathVariable Integer id, Model model) {

    ApplicantInterview applicantInterview = applicantInterviewService.findById(id);

    Applicant applicant = applicantInterview.getApplicant();
    applicant.setApplicantStatus(ApplicantStatus.REJ);
    applicantService.persist(applicant);

    applicantInterview.setApplicantInterviewStatus(ApplicantInterviewStatus.CL);
    applicantInterviewService.persist(applicantInterview);

    model.addAttribute("applicantInterviews",
                       applicantInterviewService.findAll().stream().filter(x -> x.getApplicantInterviewStatus().equals(ApplicantInterviewStatus.ACT)).collect(Collectors.toList()));
    return "interviewSchedule/interviewSchedule";
  }

}

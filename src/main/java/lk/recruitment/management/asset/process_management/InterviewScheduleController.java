package lk.recruitment.management.asset.process_management;

import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import lk.recruitment.management.asset.applicant_interview.service.ApplicantInterviewService;
import lk.recruitment.management.asset.common_asset.model.InterviewSchedule;
import lk.recruitment.management.asset.common_asset.model.InterviewScheduleList;
import lk.recruitment.management.asset.interview_board.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.asset.interview_board.service.InterviewBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.Collator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/interviewSchedule" )
public class InterviewScheduleController {
  private final ApplicantService applicantService;
  private final InterviewBoardService interviewBoardService;
  private final ApplicantInterviewService applicantInterviewService;

  public InterviewScheduleController(ApplicantService applicantService, InterviewBoardService interviewBoardService,
                                     ApplicantInterviewService applicantInterviewService) {
    this.applicantService = applicantService;
    this.interviewBoardService = interviewBoardService;
    this.applicantInterviewService = applicantInterviewService;
  }

  @GetMapping
  public String form(Model model) {
    model.addAttribute("totalApplicantCount", applicantService.accordingToApplicantStatus(ApplicantStatus.A));
    model.addAttribute("interviewBoard", interviewBoardService.findAll().
                           stream()
                           .filter(x -> x.getInterviewBoardStatus().equals(InterviewBoardStatus.ACT))
                           .collect(Collectors.toList())
                      );
    model.addAttribute("interviewSchedule", new InterviewSchedule());
    return "interviewSchedule/interviewSchedule";
  }

  @PostMapping
  public String dateCount(@ModelAttribute InterviewSchedule interviewSchedule) {

    interviewSchedule.getInterviewScheduleLists().forEach(x -> System.out.println(x.toString()));
    return "";
  }

}

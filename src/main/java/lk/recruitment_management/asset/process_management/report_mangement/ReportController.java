package lk.recruitment_management.asset.process_management.report_mangement;

import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_file.service.ApplicantFilesService;
import lk.recruitment_management.asset.applicant_gazette.service.ApplicantGazetteService;
import lk.recruitment_management.asset.applicant_gazette_interview.service.ApplicantGazetteInterviewService;
import lk.recruitment_management.asset.applicant_gazette_interview_result.service.ApplicantGazetteInterviewResultService;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.service.ApplicantGazetteSisCrdCidService;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import lk.recruitment_management.asset.interview.service.InterviewService;
import lk.recruitment_management.util.service.DateTimeAgeService;
import lk.recruitment_management.util.service.FileHandelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;

@Controller
@RequestMapping("/report")
public class ReportController {
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

  public ReportController(ApplicantService applicantService, FileHandelService fileHandelService, ServletContext context, ApplicantGazetteSisCrdCidService applicantGazetteSisCrdCidService, InterviewService interviewService, ApplicantGazetteInterviewService applicantGazetteInterviewService, GazetteService gazetteService, ApplicantGazetteService applicantGazetteService, ApplicantFilesService applicantFilesService, DateTimeAgeService dateTimeAgeService, ApplicantGazetteInterviewResultService applicantGazetteInterviewResultService) {
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


}

package lk.recruitment_management.asset.applicant_gazette_interview.dao;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.enums.ApplicantGazetteInterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApplicantGazetteInterviewDao extends JpaRepository< ApplicantGazetteInterview, Integer > {
  List< ApplicantGazetteInterview > findByApplicantGazette(ApplicantGazette applicantGazette);

  ApplicantGazetteInterview findByApplicantGazetteAndApplicantGazetteInterviewStatusAndInterviewDate(ApplicantGazette applicantGazette, ApplicantGazetteInterviewStatus applicantGazetteInterviewStatus, LocalDate interviewDate);
}

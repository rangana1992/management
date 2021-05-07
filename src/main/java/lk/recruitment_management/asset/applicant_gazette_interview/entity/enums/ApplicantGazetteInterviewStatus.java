package lk.recruitment_management.asset.applicant_gazette_interview.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicantGazetteInterviewStatus {
  ACT("Active"),
  CL("Close");

  private final String applicantGazetteInterviewStatus;
}

package lk.recruitment_management.asset.applicant_interview.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicantInterviewStatus {
  ACT("Active"),
  CL("Close");

  private final String applicantInterviewStatus;
}

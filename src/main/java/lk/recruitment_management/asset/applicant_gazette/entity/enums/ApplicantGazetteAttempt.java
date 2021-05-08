package lk.recruitment_management.asset.applicant_gazette.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicantGazetteAttempt {
  FA("First Attempt"),
  SA("Second Attempt"),
  TA("Third Attempt");
  private final String applicantGazzetAttempt;
}

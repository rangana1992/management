package lk.recruitment_management.asset.applicant_gazzet.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public enum ApplicantGazzetAttempt {
  FA("First Attempt"),
  SA("Second Attempt"),
  TA("Third Attempt");
  private final String applicantGazzetAttempt;
}

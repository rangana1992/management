package lk.recruitment_management.asset.applicant_gazette_interview.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassFailed {
  PA("Pass"),
  FL("Failed");
  private final String passFailed;
}

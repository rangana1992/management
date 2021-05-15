package lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassFailed {
  PASS("Pass"),
  FAILED("Failed"),
  ABSENT("Absent");
  private final String passFailed;
}

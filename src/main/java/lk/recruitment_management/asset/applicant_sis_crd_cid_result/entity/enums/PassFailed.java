package lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassFailed {
  PASS("PASS"),
  FAILED("FAILED"),
  ABSENT("ABSENT");
  private final String passFailed;
}

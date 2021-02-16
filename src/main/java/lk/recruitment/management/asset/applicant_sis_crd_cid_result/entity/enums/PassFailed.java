package lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassFailed {
  PASS("Pass"),
  FAILED("Failed");
  private final String passFailed;
}

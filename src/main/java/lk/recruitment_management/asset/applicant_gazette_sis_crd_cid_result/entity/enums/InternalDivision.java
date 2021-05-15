package lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalDivision {
  SIS("State Intelligence S"),
  CRD("Crime Record D"),
  CID("Criminal Investigation D"),
  NOT("Not Match with this which requested report");

  private final String internalDivision;
}

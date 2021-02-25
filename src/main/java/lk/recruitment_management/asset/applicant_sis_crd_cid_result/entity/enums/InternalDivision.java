package lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalDivision {
  SIS("State Intelligence Service"),
  CRD("Crime Record Division"),
  CID("Criminal Investigation Department");

  private final String internalDivision;
}

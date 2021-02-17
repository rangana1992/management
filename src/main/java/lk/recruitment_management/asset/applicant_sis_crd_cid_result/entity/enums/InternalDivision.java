package lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalDivision {
  SIS("SIS"),
  CRD("CRD"),
  CID("CID");

  private final String internalDivision;
}

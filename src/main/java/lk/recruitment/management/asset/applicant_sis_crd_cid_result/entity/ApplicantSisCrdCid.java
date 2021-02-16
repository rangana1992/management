package lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity.enums.InternalDivision;
import lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity.enums.PassFailed;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ApplicantSisCrdCid" )
public class ApplicantSisCrdCid extends AuditEntity {

  @Enumerated( EnumType.STRING)
  private PassFailed passFailed;

  @Enumerated( EnumType.STRING)
  private InternalDivision internalDivision;

  @ManyToOne
  private Applicant applicant;
}

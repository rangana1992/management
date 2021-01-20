package lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ApplicantSisCrdCid" )
public class ApplicantSisCrdCid extends AuditEntity {

  private String result;

  @ManyToOne
  private Applicant applicant;
}

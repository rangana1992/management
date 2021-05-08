package lk.recruitment_management.asset.applicant_gazette.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteAttempt;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.util.audit.AuditEntity;
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
@JsonFilter("ApplicantGazzet")
public class ApplicantGazette extends AuditEntity {

  @Enumerated( EnumType.STRING)
  private ApplicantGazetteAttempt applicantGazetteAttempt;

  @ManyToOne
  private Applicant applicant;

  @ManyToOne
  private Gazette gazette;



}

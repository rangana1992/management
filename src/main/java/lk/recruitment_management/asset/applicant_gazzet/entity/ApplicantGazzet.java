package lk.recruitment_management.asset.applicant_gazzet.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazzet.entity.enums.ApplicantGazzetAttempt;
import lk.recruitment_management.asset.gazzet.entity.Gazzet;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;

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
public class ApplicantGazzet extends AuditEntity {

  @Enumerated( EnumType.STRING)
  private ApplicantGazzetAttempt applicantGazzetAttempt;

  @ManyToOne
  private Applicant applicant;

  @ManyToOne
  private Gazzet gazzet;



}

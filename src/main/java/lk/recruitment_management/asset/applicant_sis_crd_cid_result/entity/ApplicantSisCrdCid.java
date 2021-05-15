package lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums.InternalDivision;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.enums.PassFailed;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

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
  private ApplicantGazette applicantGazette;

  @Transient
  private MultipartFile multipartFile;

  @Transient
  private int id;
}

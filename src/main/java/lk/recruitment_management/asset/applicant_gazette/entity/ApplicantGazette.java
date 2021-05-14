package lk.recruitment_management.asset.applicant_gazette.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.ApplicantSisCrdCid;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ApplicantGazette" )
public class ApplicantGazette extends AuditEntity {

  @Column( nullable = false, unique = true )
  private String code;

  @Enumerated( EnumType.STRING )
  private ApplyingRank applyingRank;

  @Enumerated( EnumType.STRING )
  private ApplicantGazetteStatus applicantGazetteStatus;

  @ManyToOne
  private Applicant applicant;

  @ManyToOne
  private Gazette gazette;

  @OneToMany( mappedBy = "applicantGazette" )
  private List< ApplicantGazetteInterview > applicantGazetteInterviews;

  @OneToMany( mappedBy = "applicantGazette" )
  private List< ApplicantSisCrdCid > applicantSisCrdCids;


}

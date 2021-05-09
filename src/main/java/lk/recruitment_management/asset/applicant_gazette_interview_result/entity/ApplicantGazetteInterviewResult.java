package lk.recruitment_management.asset.applicant_gazette_interview_result.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment_management.util.audit.AuditEntity;
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
@JsonFilter( "ApplicantGazetteInterviewResult" )
public class ApplicantGazetteInterviewResult extends AuditEntity {

  private String result;

  @ManyToOne
  private InterviewParameter interviewParameter;

  @ManyToOne
  private ApplicantGazetteInterview applicantGazetteInterview;

}

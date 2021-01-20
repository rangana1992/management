package lk.recruitment.management.asset.applicant_interview_result.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant_interview.entity.ApplicantInterview;
import lk.recruitment.management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ApplicantInterviewResult" )
public class ApplicantInterviewResult extends AuditEntity {

  private String result;

  @ManyToOne
  private InterviewParameter interviewParameter;

  @ManyToOne
  private ApplicantInterview applicantInterview;

}

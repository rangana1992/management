package lk.recruitment_management.asset.applicant_interview.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_interview.entity.enums.ApplicantInterviewStatus;
import lk.recruitment_management.asset.applicant_interview_result.entity.ApplicantInterviewResult;
import lk.recruitment_management.asset.interview_board.entity.InterviewBoard;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ApplicantInterview")
public class ApplicantInterview extends AuditEntity {

  @Enumerated( EnumType.STRING)
  private ApplicantInterviewStatus applicantInterviewStatus;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate interviewDate;

  @ManyToOne
  private InterviewBoard interviewBoard;

  @ManyToOne
  private Applicant applicant;

  @OneToMany(mappedBy = "applicantInterview")
  private List< ApplicantInterviewResult > applicantInterviewResults;

}

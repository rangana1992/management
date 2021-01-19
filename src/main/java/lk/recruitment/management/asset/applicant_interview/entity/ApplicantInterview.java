package lk.recruitment.management.asset.applicant_interview.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant_interview.entity.enums.ApplicantInterviewStatus;
import lk.recruitment.management.asset.interview.entity.Interview;
import lk.recruitment.management.asset.interview_board.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.asset.interview_board.entity.InterviewBoard;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ApplicantInterview")
public class ApplicantInterview extends AuditEntity {

  private int count;

  @Enumerated( EnumType.STRING)
  private ApplicantInterviewStatus applicantInterviewStatus;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate interviewDate;

  @ManyToOne
  private InterviewBoard interviewBoard;

  @ManyToOne
  private Applicant applicant;

  @ManyToOne
  private Interview interview;
}

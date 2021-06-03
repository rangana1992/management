package lk.recruitment_management.asset.interview_board.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.asset.employee.entity.Employee;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.asset.interview_board.entity.enums.InterviewBoardStatus;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "InterviewBoard" )
public class InterviewBoard extends AuditEntity {

  private String name;

  @Enumerated( EnumType.STRING )
  private InterviewBoardStatus interviewBoardStatus;

  @ManyToOne
  private Interview interview;

  @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, fetch = FetchType.EAGER )
  @Fetch( FetchMode.SUBSELECT )
  @JoinTable( name = "interview_board_employee",
      joinColumns = @JoinColumn( name = "interview_board_id" ),
      inverseJoinColumns = @JoinColumn( name = "employee_id" ) )
  private List< Employee > employees;
}

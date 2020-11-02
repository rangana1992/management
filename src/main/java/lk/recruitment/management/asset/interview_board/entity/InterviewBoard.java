package lk.recruitment.management.asset.interview_board.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.employee.entity.Employee;
import lk.recruitment.management.asset.interview_board.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.util.audit.AuditEntity;
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
@JsonFilter("InterviewBoard")
public class InterviewBoard extends AuditEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private InterviewBoardStatus interviewBoardStatus;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch( FetchMode.SUBSELECT)
    @JoinTable(name = "interview_board_employee",
            joinColumns = @JoinColumn(name = "interview_board_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;
}

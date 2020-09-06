package lk.recruitment.management.asset.interviewBoard.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.employee.entity.Employee;
import lk.recruitment.management.asset.interviewBoard.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
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

    @OneToMany
    private List<Employee> employees;
}

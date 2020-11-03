package lk.recruitment.management.asset.interview.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.interview.entity.Enum.InterviewStatus;
import lk.recruitment.management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment.management.util.audit.AuditEntity;
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
@JsonFilter("Interview")
public class Interview extends AuditEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private InterviewStatus interviewStatus;

    @ManyToMany
    @JoinTable(name = "interview_interview_parameter",
            joinColumns = @JoinColumn(name = "interview_id"),
            inverseJoinColumns = @JoinColumn(name = "interview_parameter_id"))
    private List<InterviewParameter> interviewParameters;
}

package lk.recruitment_management.asset.interview_parameter.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("InterviewParameter")
public class InterviewParameter extends AuditEntity {

    private String name;

    private String max;

    private String min;

    @ManyToMany(mappedBy = "interviewParameters")
    private List<Interview> interviews;
}

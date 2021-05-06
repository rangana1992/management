package lk.recruitment_management.asset.grievances.entity;

import com.fasterxml.jackson.annotation.JsonFilter;

import lk.recruitment_management.asset.grievances.entity.enums.GrievancesStatus;
import lk.recruitment_management.asset.grievances.entity.enums.SolutionType;
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
@JsonFilter("Grievance")
public class Grievance extends AuditEntity {


    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private GrievancesStatus grievancesStatus;

    @Enumerated(EnumType.STRING)
    private SolutionType solutionType;

    @OneToMany(mappedBy = "grievance", cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<GrievanceStateChange> grievanceStateChange;

    @Transient
    private String remark;

}

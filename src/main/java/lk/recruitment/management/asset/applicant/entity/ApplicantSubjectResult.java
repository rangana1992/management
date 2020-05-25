package lk.recruitment.management.asset.applicant.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Enum.Result;
import lk.recruitment.management.asset.subject.entity.Subject;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("PoliceStation")
@ToString
public class ApplicantSubjectResult extends AuditEntity {
    private String indexNo ;
    private String year ;
    @ManyToOne
    private Applicant applicant ;
    @ManyToOne
    private Subject subject ;
    @Enumerated(EnumType.STRING)
    private Result result ;


}

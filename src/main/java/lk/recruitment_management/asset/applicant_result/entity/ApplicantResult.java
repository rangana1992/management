package lk.recruitment_management.asset.applicant_result.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_result.entity.enums.Attempt;
import lk.recruitment_management.asset.applicant_result.entity.enums.CompulsoryOLSubject;
import lk.recruitment_management.asset.applicant_result.entity.enums.StreamLevel;
import lk.recruitment_management.asset.applicant_result.entity.enums.SubjectResult;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ApplicantResult")
public class ApplicantResult extends AuditEntity {
    private String indexNumber;

    private String subjectName;

    private String mark;

    private String year;

    @Enumerated(EnumType.STRING)
    private Attempt attempt;

    @Enumerated(EnumType.STRING)
    private StreamLevel streamLevel;

    @Enumerated(EnumType.STRING)
    private CompulsoryOLSubject compulsoryOLSubject;

    @Enumerated(EnumType.STRING)
    private SubjectResult subjectResult;

    @ManyToOne
    private Applicant applicant;


}

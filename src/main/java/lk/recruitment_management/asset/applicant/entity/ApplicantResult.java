package lk.recruitment_management.asset.applicant.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.asset.applicant.entity.Enum.Attempt;
import lk.recruitment_management.asset.applicant.entity.Enum.CompulsoryOLSubject;
import lk.recruitment_management.asset.applicant.entity.Enum.StreamLevel;
import lk.recruitment_management.asset.applicant.entity.Enum.SubjectResult;
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
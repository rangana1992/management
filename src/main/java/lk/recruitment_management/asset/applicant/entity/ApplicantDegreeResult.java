package lk.recruitment_management.asset.applicant.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ApplicantDegreeResult")
public class ApplicantDegreeResult extends AuditEntity {
    private String degreeName;
    private String universityName;
    private String year;

    @ManyToOne
    private Applicant applicant;
}

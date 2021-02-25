package lk.recruitment_management.asset.applicant.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("NonRelative")
public class NonRelative extends AuditEntity {

    @NotEmpty
    @Column(nullable = false)
    private String name;

    private String address;

    @Size(max = 10, message = "Mobile number length should be contained 10 and 9")
    private String mobile;

    private String land;

    @ManyToOne
    private Applicant applicant;

}

package lk.recruitment_management.asset.gazette.entity;

import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gazette extends AuditEntity {

  @Column( nullable = false, length = 45, unique = true )
  private String name;

  private String summary;

  private int age;

  @Enumerated( EnumType.STRING )
  private GazetteStatus gazetteStatus;

  @OneToMany( mappedBy = "gazette" )
  private List< ApplicantGazette > applicantGazettes;


}

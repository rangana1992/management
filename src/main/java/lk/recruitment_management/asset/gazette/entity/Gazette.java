package lk.recruitment_management.asset.gazette.entity;


import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Gazette {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @Column( nullable = false, length = 45, unique = true )
  private String name;

  @Enumerated( EnumType.STRING )
  private GazetteStatus gazetteStatus;

  @OneToMany( mappedBy = "gazzet" )
  private List< ApplicantGazette > applicantGazettes;


}

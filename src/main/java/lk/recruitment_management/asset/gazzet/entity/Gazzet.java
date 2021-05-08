package lk.recruitment_management.asset.gazzet.entity;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazzet.entity.ApplicantGazzet;
import lk.recruitment_management.asset.gazzet.entity.enums.GazzetStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Gazzet {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @Column( nullable = false, length = 45, unique = true )
  private String name;

  @Enumerated( EnumType.STRING )
  private GazzetStatus gazzetStatus;

  @OneToMany( mappedBy = "gazzet" )
  private List< ApplicantGazzet > applicantGazzets;


}

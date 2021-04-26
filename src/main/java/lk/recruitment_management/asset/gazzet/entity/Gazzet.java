package lk.recruitment_management.asset.gazzet.entity;


import lk.recruitment_management.asset.applicant.entity.Applicant;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45, unique = true)
    private String name;


    @ManyToMany(mappedBy = "authors")
    private List< Applicant > applicants;


}

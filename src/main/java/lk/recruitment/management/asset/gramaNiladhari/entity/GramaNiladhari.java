package lk.recruitment.management.asset.gramaNiladhari.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.policeStation.entity.PoliceStation;
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
@JsonFilter("GramaNiladhari")
public class GramaNiladhari {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    @ManyToOne
    private PoliceStation policeStation;

    @OneToMany(mappedBy = "gramaNiladhari")
    private List<Applicant> applicants;




}
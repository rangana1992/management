package lk.recruitment.management.asset.district.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.commonAsset.model.Enum.Province;
import lk.recruitment.management.asset.workingPlace.entity.WorkingPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("District")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 13, message = "Your name length should be 13")
    private String name;

    @Enumerated(EnumType.STRING)
    private Province province;

    @OneToMany(mappedBy = "district")
    private List<WorkingPlace> workingPlaces;
}
package lk.recruitment.management.asset.police_station.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.ag_office.entity.AgOffice;
import lk.recruitment.management.asset.grama_niladhari.entity.GramaNiladhari;
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
@JsonFilter("PoliceStation")
public class PoliceStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 60, message = "Your Stream length should be 13")
    private String name;
    private String address;
    private String land;
    private String email;

    @ManyToOne
    private AgOffice agOffice;

    @OneToMany (mappedBy = "policeStation")
    private List<GramaNiladhari> gramaNiladharis;


}

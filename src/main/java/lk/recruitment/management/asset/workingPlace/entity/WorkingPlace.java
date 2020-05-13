package lk.recruitment.management.asset.workingPlace.entity;

import com.fasterxml.jackson.annotation.JsonFilter;

import lk.recruitment.management.asset.district.entity.District;
import lk.recruitment.management.asset.employee.entity.Employee;
import lk.recruitment.management.asset.workingPlace.entity.Enum.AirportType;
import lk.recruitment.management.asset.workingPlace.entity.Enum.ShortName;
import lk.recruitment.management.asset.workingPlace.entity.Enum.WorkingPlaceSection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("WorkingPlace")
public class WorkingPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 10, message = "Your name length should be 10")
    private String name;

    private String address;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ShortName shortName;

    @Enumerated(EnumType.STRING)
    private AirportType airportType;

    @Enumerated(EnumType.STRING)
    private WorkingPlaceSection workingPlaceSection;

    @ManyToOne
    private District district;

    @OneToMany(mappedBy = "workingPlace")
    private List<Employee> employees;

}
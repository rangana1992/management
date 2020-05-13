package lk.recruitment.management.asset.designation.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.designation.entity.Enum.CategoryType;
import lk.recruitment.management.asset.designation.entity.Enum.SalaryScale;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Designation")
public class Designation extends AuditEntity {


   // @Size(min = 2, max = 60, message = "Your name length should be 13")
    private String name;

    private String shortName;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Enumerated (EnumType.STRING)
    private SalaryScale salaryScale;

    private String remark;



  /*  @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate completeDate;*/
    /*@Enumerated(EnumType.STRING)
    private Province province;*/

//Subject(name) , Description , participant , Remark, priority
/*    @OneToMany(mappedBy = "district")
    private List<WorkingPlace> workingPlaces;*/
}

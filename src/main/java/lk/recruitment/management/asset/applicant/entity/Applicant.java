package lk.recruitment.management.asset.applicant.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplyingRank;
import lk.recruitment.management.asset.applicant.entity.Enum.Nationality;
import lk.recruitment.management.asset.commonAsset.model.Enum.CivilStatus;
import lk.recruitment.management.asset.commonAsset.model.Enum.Gender;
import lk.recruitment.management.asset.commonAsset.model.Enum.Title;
import lk.recruitment.management.asset.gramaNiladhari.entity.GramaNiladhari;
import lk.recruitment.management.asset.policeStation.Entity.PoliceStation;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Applicant")
@ToString
public class Applicant extends AuditEntity {

    @NotNull
    @Column(nullable = false)
    private String nameInFullName;


    @NotNull
    @Column(nullable = false)
    private String nameWithInitial;

    @NotNull
    @Column(nullable = false)
    private String nic;

    @Enumerated(EnumType.STRING)
    private ApplyingRank applyingRank;


    @NotNull
    @Column(nullable = false)
    private String height;

    @NotNull
    @Column(nullable = false)
    private String weight;

    @NotNull
    @Column(nullable = false)
    private String chest;

    @Size(max = 10, message = "Mobile number length should be contained 10 and 9")
    private String mobile;


    private String land;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private CivilStatus civilStatus;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ManyToOne
    private GramaNiladhari gramaNiladhari;

    @Transient
    private MultipartFile file;

    @OneToMany(mappedBy ="applicant" )
    private List<ApplicantSubjectResult> applicantSubjectResults;

}

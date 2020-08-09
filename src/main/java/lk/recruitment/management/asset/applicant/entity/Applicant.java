package lk.recruitment.management.asset.applicant.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplyingRank;
import lk.recruitment.management.asset.applicant.entity.Enum.Nationality;
import lk.recruitment.management.asset.commonAsset.model.Enum.CivilStatus;
import lk.recruitment.management.asset.commonAsset.model.Enum.Gender;
import lk.recruitment.management.asset.gramaNiladhari.entity.GramaNiladhari;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Applicant")
public class Applicant extends AuditEntity {

    @NotEmpty
    @Column(nullable = false)
    private String nameInFullName;


    @NotEmpty
    @Column(nullable = false)
    private String nameWithInitial;

    @NotEmpty
    @Column(nullable = false)
    private String nic;

    @Enumerated(EnumType.STRING)
    private ApplyingRank applyingRank;


    @NotEmpty
    @Column(nullable = false)
    private String height;

    @NotEmpty
    @Column(nullable = false)
    private String weight;

    @NotEmpty
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

    @Enumerated(EnumType.STRING)
    private ApplicantStatus applicantStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ManyToOne
    private GramaNiladhari gramaNiladhari;

    @OneToMany(mappedBy ="applicant" )
    private List<ApplicantResult> applicantResults;

    @OneToMany
    private List<NonRelative> nonRelatives;

    @Transient
    private MultipartFile file;

    @Transient
    private MultipartFile nicImage;

    @Transient
    private MultipartFile birthCertificateImage;

    @Transient
    private MultipartFile gramaNilahdariImage;

    @Transient
    private List<MultipartFile> educationalImages;

    @Transient
    private List<MultipartFile> sportImages;



}

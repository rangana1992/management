package lk.recruitment_management.asset.common_asset.model;

import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TwoDate {
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate startDate;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate endDate;

    @Enumerated( EnumType.STRING)
    private ApplyingRank applyingRank;

    @Enumerated(EnumType.STRING)
    private ApplicantGazetteStatus applicantGazetteStatus;


}

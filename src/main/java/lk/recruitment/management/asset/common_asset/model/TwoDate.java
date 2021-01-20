package lk.recruitment.management.asset.common_asset.model;

import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplyingRank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwoDate {
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate startDate;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate endDate;

    @Enumerated( EnumType.STRING)
    private ApplyingRank applyingRank;

    @Enumerated(EnumType.STRING)
    private ApplicantStatus applicantStatus;


}

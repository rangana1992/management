package lk.recruitment_management.asset.common_asset.model;

import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InterviewSchedule {
  private int id;

  private String massage;

  @Enumerated( EnumType.STRING )
  private ApplicantGazetteStatus interviewNumber;

  private List< InterviewScheduleList > interviewScheduleLists;
}

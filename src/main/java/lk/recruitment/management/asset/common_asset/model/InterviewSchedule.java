package lk.recruitment.management.asset.common_asset.model;

import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
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

  @Enumerated( EnumType.STRING )
  private ApplicantStatus interviewNumber;

  private List< InterviewScheduleList > interviewScheduleLists;
}
